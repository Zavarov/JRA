package vartas.jra.http;

import com.google.common.base.Joiner;
import com.google.common.net.HttpHeaders;
import okhttp3.*;
import vartas.jra.Client;
import vartas.jra.exceptions.HttpException;

import java.io.IOException;
import java.util.*;

public class APIAuthentication {
    private final String url;
    private final String credentials;
    private final Client client;
    private final Map<String, String> params = new HashMap<>();
    private final List<Client.Scope> scopes = new ArrayList<>();

    private APIAuthentication(String url, String credentials, Client client){
        this.url = url;
        this.credentials = credentials;
        this.client = client;
    }

    private RequestBody body(){
        FormBody.Builder builder = new FormBody.Builder();

        params.forEach(builder::add);

        if(!scopes.isEmpty())
            builder.add("scope", Joiner.on(',').join(scopes));

        return builder.build();
    }

    public String post() throws InterruptedException, IOException, HttpException {
         Request request = new Request.Builder()
                .url(url)
                .addHeader(HttpHeaders.AUTHORIZATION, "Basic "+credentials)
                .addHeader(HttpHeaders.USER_AGENT, client.getUserAgent().toString())
                .post(body())
                .build();

        //Call execute directly to avoid checking the non-existent token for validity
        Response response = client.execute(request);

        ResponseBody data = response.body();

        //Call.execute() supposedly always returns a non-null response with non-null body
        assert data != null;

        //Close the response
        return data.string();
    }

    public static class Builder{
        private final APIAuthentication source;

        public Builder(String url, String credentials, Client client){
            source = new APIAuthentication(url, credentials, client);
        }

        public Builder addParameter(String key, Object value){
            source.params.put(key, value.toString());
            return this;
        }

        public Builder addScope(Client.Scope... scope){
            source.scopes.addAll(Arrays.asList(scope));
            return this;
        }

        public APIAuthentication build(){
            return source;
        }
    }
}
