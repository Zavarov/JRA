package vartas.reddit;

import com.google.common.net.HttpHeaders;
import okhttp3.*;
import org.json.JSONObject;
import vartas.reddit.$json.JSONToken;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.exceptions.RateLimiterException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class UserlessClient extends Client{
    @Nonnull
    private final String id;
    @Nonnull
    private final String secret;

    public UserlessClient(
            @Nonnull String platform,
            @Nonnull String author,
            @Nonnull String version,
            @Nonnull String id,
            @Nonnull String secret
    ){
        super(platform, version, author);
        this.id = id;
        this.secret = secret;
    }

    protected Request build(String url, RequestBody body){
        String credentials = Base64.getEncoder().encodeToString((id+":"+secret).getBytes(StandardCharsets.UTF_8));

        return new Request.Builder()
                .url(url)
                .addHeader(HttpHeaders.AUTHORIZATION, "Basic "+credentials)
                .addHeader(HttpHeaders.USER_AGENT, getUserAgent().toString())
                .post(body)
                .build();
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Login                                                                                                       //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public synchronized void login(Duration duration) throws IOException, HttpException, RateLimiterException, InterruptedException {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", GrantType.USERLESS.toString())
                .add("device_id", uuid)
                .add("duration", duration.toString())
                .build();

        Request request = build(ACCESS_TOKEN, body);
        //Call execute directly to avoid checking the non-existent token for validity
        Response response = execute(request);
        ResponseBody data = response.body();

        //Call.execute() supposedly always returns a non-null response with non-null body
        assert data != null;

        //data.string() automatically closes the response
        setToken(JSONToken.fromJson(new Token(), new JSONObject(data.string())));
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Logout                                                                                                      //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public synchronized void logout() throws IOException, HttpException, InterruptedException, RateLimiterException {
        assert isPresentToken();

        revokeRefreshToken();
        revokeAccessToken();
        setToken(Optional.empty());
    }

    private void revokeRefreshToken() throws IOException, HttpException, InterruptedException, RateLimiterException {
        assert isPresentToken();

        if(orElseThrowToken().isEmptyRefreshToken())
            return;

        RequestBody body = new FormBody.Builder()
                .add("token", orElseThrowToken().orElseThrowRefreshToken())
                .add("token_type_hint", TokenType.REFRESH_TOKEN.toString())
                .build();

        request(build(REVOKE_TOKEN, body)).close();
    }

    private void revokeAccessToken() throws IOException, HttpException, InterruptedException, RateLimiterException {
        assert isPresentToken();

        RequestBody body = new FormBody.Builder()
                .add("token", orElseThrowToken().getAccessToken())
                .add("token_type_hint", TokenType.ACCESS_TOKEN.toString())
                .build();

        request(build(REVOKE_TOKEN, body)).close();
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Refresh                                                                                                     //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    protected synchronized void refresh() throws IOException, HttpException, RateLimiterException, InterruptedException {
        assert isPresentToken() && orElseThrowToken().isPresentRefreshToken();

        RequestBody body = new FormBody.Builder()
                .add("grant_type", GrantType.REFRESH.toString())
                .add("refresh_token", orElseThrowToken().orElseThrowRefreshToken())
                .build();

        Response response = request(build(ACCESS_TOKEN, body));
        ResponseBody data = response.body();

        assert data != null;

        //On February 15th 2021, the refresh response will contain a new refresh token.
        //Until then, we reuse the initial token.
        //@see https://redd.it/kvzaot
        String refreshToken = orElseThrowToken().orElseThrowRefreshToken();

        setToken(JSONToken.fromJson(new Token(), new JSONObject(data.string())));
        //#TODO Remove after February 15th 2021
        if(orElseThrowToken().isEmptyRefreshToken())
            orElseThrowToken().setRefreshToken(refreshToken);
    }
}
