package vartas.jra.mock;

import vartas.jra.AbstractClient;
import vartas.jra.Token;
import vartas.jra.UserAgent;
import vartas.jra._json.JSONToken;
import vartas.jra.exceptions.HttpException;
import vartas.jra.exceptions.RateLimiterException;
import vartas.jra.http.APIAuthentication;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ClientMock extends AbstractClient {
    private final String account;    @Nonnull
    private final String password;

    public ClientMock(
            @Nonnull UserAgent userAgent,
            @Nonnull String id,
            @Nonnull String secret,
            @Nonnull String account,
            @Nonnull String password,
            @Nonnull Scope... scope
    ){
        super(userAgent, id, secret, scope);
        this.account = account;
        this.password = password;
    }

    @Override
    public synchronized void login(@Nonnull Duration duration) throws IOException, HttpException, RateLimiterException, InterruptedException {
        APIAuthentication request = new APIAuthentication.Builder(ACCESS_TOKEN, credentials, this)
                .addParameter("grant_type", GrantType.PASSWORD)
                .addParameter("username", account)
                .addParameter("password", password)
                .addParameter("duration", duration)
                .addScope(scope)
                .build();

        String response = request.post();

        setToken(JSONToken.fromJson(new Token(), response));
    }
}
