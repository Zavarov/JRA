package vartas.reddit;

import com.google.common.net.HttpHeaders;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.concurrent.TimedSemaphore;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.reddit.$factory.SubredditFactory;
import vartas.reddit.$factory.UserAgentFactory;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.exceptions.RateLimiterException;
import vartas.reddit.types.$factory.ThingFactory;
import vartas.reddit.types.Thing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Nonnull
public abstract class JraClient extends Client {
    @Nonnull
    TimedSemaphore rateLimiter = new TimedSemaphore(1, TimeUnit.MINUTES, 60);
    @Nonnull
    protected final String deviceId = UUID.randomUUID().toString();
    @Nonnull
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    @Nonnull
    protected final UserAgent userAgent;
    @Nonnull
    protected final OkHttpClient http;
    @Nullable
    protected Token token;

    public JraClient(@Nonnull String platform, @Nonnull String version, @Nonnull String author){
        userAgent = UserAgentFactory.create(platform, getClass().getPackage().getName(), version, author);
        http = new OkHttpClient();
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Request Builder                                                                                             //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    protected Request get(Endpoint endpoint, Object... args){
        assert token != null;

        String url = Endpoint.HOST + String.format(endpoint.toString(), args);

        return new Request.Builder()
                .url(url)
                .addHeader(HttpHeaders.AUTHORIZATION, "bearer " + token.getAccessToken())
                .addHeader(HttpHeaders.USER_AGENT, userAgent.toString())
                .get()
                .build();
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Http Calls                                                                                                  //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    protected synchronized Response request(Request request) throws IOException, HttpException, InterruptedException, RateLimiterException {
        return execute(request);
    }

    protected Response execute(Request request) throws InterruptedException, IOException, HttpException, RateLimiterException {
        //Wait if we're making too many requests at once
        rateLimiter.acquire();

        log.info("--> {}", request);
        Response response = http.newCall(request).execute();
        log.info("<-- {}", response);

        if(response.code() == 429)
            throw new RateLimiterException();
        if(!response.isSuccessful())
            throw new HttpException(response.code(), response.message());

        return response;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    API Calls                                                                                                   //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public Optional<Subreddit> getSubreddit(String name) throws HttpException, IOException, InterruptedException {
        assert token != null;

        Response response = request(get(Endpoint.GET_SUBREDDIT_ABOUT, name));
        ResponseBody body = response.body();

        assert body != null;

        Thing thing = ThingFactory.create(Thing::new, new JSONObject(body.string()));

        //In case a subreddit with the specified name doesn't exist, the return thing may be arbitrary
        if(Kind.Subreddit.matches(thing.getKind()))
            return Optional.of(SubredditFactory.create(JraSubreddit::new, thing.getData()));
        else
            return Optional.empty();

    }
}
