package net.zav.jra.http;

import com.google.common.net.HttpHeaders;
import net.zav.jra.AbstractClient;
import net.zav.jra.endpoints.Endpoint;
import okhttp3.*;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Nonnull
public class APIRequest {
    /**
     * The protocol used for OAuth requests.
     */
    @Nonnull
    public static final String HTTPS = "https";
    /**
     * The host accepting all OAuth2 requests.
     */
    @Nonnull
    public static final String OAUTH2 = "oauth.reddit.com";
    /**
     * The endpoint used for all requests that don't require OAuth2.
     */
    @Nonnull
    public static final String WWW = "www.reddit.com";
    /**
     * The underlying client instance used to perform the request.
     */
    @Nonnull
    private final AbstractClient client;
    /**
     * The content of the request. May contain serialized JSON objects e.g. to submit new submissions.
     */
    @Nullable
    private final RequestBody body;
    /**
     * The host address of the URL.
     */
    @Nonnull
    private final String host;
    /**
     * Additional parameter appended to the URL. Those may contain additional information, such as the index when
     * requesting stickied posts.
     */
    @Nonnull
    private final Map<?, ?> query;
    /**
     * The endpoint the request is aimed at.
     */
    @Nonnull
    private final Endpoint endpoint;
    /**
     * Additional arguments for the {@link Endpoint}. E.g. a  subreddit name.
     */
    @Nonnull
    private final Object[] args;

    private APIRequest(
            @Nonnull AbstractClient client,
            @Nullable RequestBody body,
            @Nonnull String host,
            @Nonnull Map<?, ?> query,
            @Nonnull Endpoint endpoint,
            @Nonnull Object... args
    ){
        this.client = client;
        this.body = body;
        this.host = host;
        this.query = query;
        this.endpoint = endpoint;
        this.args = args;
    }

    @Nonnull
    private String url(){
        HttpUrl.Builder builder = new HttpUrl.Builder().scheme(HTTPS).host(host);

        //Append the endpoint URL
        endpoint.getPath(args).forEach(builder::addPathSegment);

        //Append any additional parameter
        query.forEach((k,v) -> builder.addQueryParameter(Objects.toString(k), Objects.toString(v)));

        return builder.build().toString();
    }

    @Nonnull
    private Request.Builder builder(){
        assert client.isPresentToken();

        String url = url();

        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader(HttpHeaders.USER_AGENT, client.getUserAgent().toString());

        if(Objects.equals(host, OAUTH2))
            builder.addHeader(HttpHeaders.AUTHORIZATION, "bearer " + client.orElseThrowToken().getAccessToken());

        return builder;
    }

    @Nonnull
    private String execute(Request request) throws InterruptedException, IOException {
        Response response = client.request(request);
        ResponseBody body = response.body();

        assert body != null;

        return body.string();
    }

    @Nonnull
    public String get() throws InterruptedException, IOException {
        Request request = builder().get().build();
        return execute(request);
    }

    @Nonnull
    public String delete() throws InterruptedException, IOException {
        Request request = body == null ? builder().delete().build() : builder().delete(body).build();
        return execute(request);
    }

    @Nonnull
    public String put() throws InterruptedException, IOException {
        assert body != null;
        Request request = builder().put(body).build();
        return execute(request);
    }

    @Nonnull
    public String post() throws InterruptedException, IOException {
        assert body != null;
        Request request = builder().post(body).build();
        return execute(request);
    }

    @Nonnull
    public String patch() throws InterruptedException, IOException {
        assert body != null;
        Request request = builder().patch(body).build();
        return execute(request);
    }

    @Nonnull
    public static class Builder{
        @Nonnull
        private final AbstractClient client;
        @Nullable
        private RequestBody body = null;
        @Nonnull
        private String host = OAUTH2;
        @Nonnull
        private Map<?, ?> params = new HashMap<>();
        @Nullable
        private Endpoint endpoint = null;
        @Nonnull
        private Object[] args = new Object[0];

        @Nonnull
        public Builder(@Nonnull AbstractClient client){
            this.client = client;
        }

        @Nonnull
        public Builder setBody(@Nonnull Map<?, ?> body, BodyType type){
            switch(type){
                case JSON:
                    JSONObject node = new JSONObject(body);

                    MediaType json = MediaType.parse("application/json; charset=utf-8");

                    return setBody(RequestBody.create(node.toString(), json));
                case FORM:
                    FormBody.Builder builder = new FormBody.Builder();

                    body.forEach((k,v) -> builder.add(k.toString(), v.toString()));

                    return setBody(builder.build());
                default:
                    throw new UnsupportedOperationException("Unknown body type: "+ type);
            }
        }

        @Nonnull
        public Builder setBody(@Nonnull RequestBody body){
            this.body = body;
            return this;
        }

        @Nonnull
        public Builder setHost(@Nonnull String host){
            this.host = host;
            return this;
        }

        @Nonnull
        public Builder setParams(@Nonnull Map<?,?> params){
            this.params = params;
            return this;
        }

        @Nonnull
        public Builder setEndpoint(@Nonnull Endpoint endpoint){
            this.endpoint = endpoint;
            return this;
        }

        @Nonnull
        public Builder setArgs(@Nonnull Object... args){
            this.args = args;
            return this;
        }

        @Nonnull
        public APIRequest build(){
            assert endpoint != null;

            return new APIRequest(
                    client,
                    body,
                    host,
                    params,
                    endpoint,
                    args
            );
        }
    }

    public enum BodyType{
        JSON,
        FORM
    }
}
