package net.zav.jra.http;

import net.zav.jra.AbstractClient;
import net.zav.jra.mock.ClientMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class APIAuthenticationTest {
    APIAuthentication api;
    ClientMock client;

    @BeforeEach
    public void setUp(){
        String url = "https://www.foo.com";
        String credentials = "credentials";

        client = new ClientMock();

        api = new APIAuthentication.Builder(url, credentials, client)
                .addParameter("key", "value")
                .addScope(AbstractClient.Scope.ANY)
                .build();
    }

    @Test
    public void testPost() throws IOException, InterruptedException {
        client.json = "{}";
        assertThat(api.post()).isEqualTo(client.json);
    }
}
