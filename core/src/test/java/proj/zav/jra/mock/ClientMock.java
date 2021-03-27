package proj.zav.jra.mock;

import okhttp3.*;
import proj.zav.jra.AbstractClient;
import proj.zav.jra.Token;
import proj.zav.jra._factory.TokenFactory;
import proj.zav.jra._factory.UserAgentFactory;
import proj.zav.jra.exceptions.ForbiddenException;
import proj.zav.jra.exceptions.NotFoundException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.HttpURLConnection;

public class ClientMock extends AbstractClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public String json = null;
    public int code = HttpURLConnection.HTTP_OK;

    public ClientMock(){
        setUserAgent(UserAgentFactory.create("linux", "Client", "proj.zav.jra", "Zavarov"));
        setToken(TokenFactory.create(Token::new, "12345", 60));
    }

    @Override
    public synchronized void login(@Nonnull Duration duration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response request(Request request) throws IOException {
        return execute(request);
    }
    @Override
    public Response execute(Request request) throws IOException {
        assert json != null;

        switch(code){
            case HttpURLConnection.HTTP_FORBIDDEN:
                throw new ForbiddenException();
            case HttpURLConnection.HTTP_NOT_FOUND:
                throw new NotFoundException();
            case HttpURLConnection.HTTP_OK:
                ResponseBody body = ResponseBody.create(json, JSON);
                return new Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_0)
                        .message("")
                        .code(code)
                        .body(body).build();
            default:
                throw new IllegalArgumentException("Invalid HTTP Code " + code);
        }
    }
}
