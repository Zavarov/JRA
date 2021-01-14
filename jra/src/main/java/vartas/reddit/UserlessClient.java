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

public class UserlessClient extends JraClient{
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

    protected Request build(Endpoint endpoint, RequestBody body){
        String credentials =Base64.getEncoder().encodeToString((id+":"+secret).getBytes(StandardCharsets.UTF_8));

        return new Request.Builder()
                .url(endpoint.toString())
                .addHeader(HttpHeaders.AUTHORIZATION, "Basic "+credentials)
                .addHeader(HttpHeaders.USER_AGENT, userAgent.toString())
                .post(body)
                .build();
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Login                                                                                                       //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public synchronized void login() throws IOException, HttpException, RateLimiterException, InterruptedException {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", GrantType.USERLESS.toString())
                .add("device_id", deviceId)
                .add("duration", Duration.PERMANENT.toString())
                .build();

        Request request = build(Endpoint.ACCESS_TOKEN, body);
        Response response = execute(request);
        ResponseBody data = response.body();

        //Call.execute() supposedly always returns a non-null response with non-null body
        assert data != null;

        //data.string() automatically closes the response
        token = JSONToken.fromJson(new Token(), new JSONObject(data.string()));
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Logout                                                                                                      //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public synchronized void logout() throws IOException, HttpException, InterruptedException, RateLimiterException {
        assert token != null;

        revokeRefreshToken();
        revokeAccessToken();
        token = null;
    }

    private void revokeRefreshToken() throws IOException, HttpException, InterruptedException, RateLimiterException {
        assert token != null;

        if(token.isEmptyRefreshToken())
            return;

        RequestBody body = new FormBody.Builder()
                .add("token", token.orElseThrowRefreshToken())
                .add("token_type_hint", TokenType.REFRESH_TOKEN.toString())
                .build();

        request(build(Endpoint.REVOKE_TOKEN, body)).close();
    }

    private void revokeAccessToken() throws IOException, HttpException, InterruptedException, RateLimiterException {
        assert token != null;

        RequestBody body = new FormBody.Builder()
                .add("token", token.getAccessToken())
                .add("token_type_hint", TokenType.ACCESS_TOKEN.toString())
                .build();

        request(build(Endpoint.REVOKE_TOKEN, body)).close();
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Refresh                                                                                                     //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    protected synchronized void refresh() throws IOException, HttpException, RateLimiterException, InterruptedException {
        assert token != null && token.isPresentRefreshToken();

        RequestBody body = new FormBody.Builder()
                .add("grant_type", GrantType.REFRESH.toString())
                .add("refresh_token", token.orElseThrowRefreshToken())
                .build();

        Response response = request(build(Endpoint.ACCESS_TOKEN, body));
        ResponseBody data = response.body();

        assert data != null;

        //On February 15th 2021, the refresh response will contain a new refresh token.
        //Until then, we reuse the initial token.
        //@see https://redd.it/kvzaot
        String refreshToken = token.orElseThrowRefreshToken();

        token = JSONToken.fromJson(new Token(), new JSONObject(data.string()));
        //#TODO Remove after February 15th 2021
        if(token.isEmptyRefreshToken())
            token.setRefreshToken(refreshToken);
    }
}
