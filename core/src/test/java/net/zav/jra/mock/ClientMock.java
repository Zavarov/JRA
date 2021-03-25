package net.zav.jra.mock;

import net.zav.jra.AbstractClient;
import net.zav.jra.Token;
import net.zav.jra.UserAgent;
import net.zav.jra._factory.AbstractClientFactory;
import net.zav.jra._json.JSONToken;
import net.zav.jra.exceptions.HttpException;
import net.zav.jra.exceptions.RateLimiterException;
import net.zav.jra.http.APIAuthentication;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ClientMock extends AbstractClient {
    private final String account;    @Nonnull
    private final String password;

    public ClientMock(
            @Nonnull UserAgent userAgent,
            @Nonnull String id,
            @Nonnull String secret,
            @Nonnull String account,
            @Nonnull String password
    ){
        AbstractClientFactory.create(() -> this, userAgent, Base64.getEncoder().encodeToString((id+":"+secret).getBytes(StandardCharsets.UTF_8)));
        this.account = account;
        this.password = password;
    }

    @Override
    public synchronized void login(@Nonnull Duration duration) throws IOException, HttpException, RateLimiterException, InterruptedException {
        APIAuthentication request = new APIAuthentication.Builder(ACCESS_TOKEN, getCredentials(), this)
                .addParameter("grant_type", GrantType.PASSWORD)
                .addParameter("username", account)
                .addParameter("password", password)
                .addParameter("duration", duration)
                .build();

        String response = request.post();

        setToken(JSONToken.fromJson(new Token(), response));
    }
}
