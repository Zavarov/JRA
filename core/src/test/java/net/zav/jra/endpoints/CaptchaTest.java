package net.zav.jra.endpoints;

import net.zav.jra.AbstractTest;
import net.zav.jra.exceptions.ForbiddenException;
import net.zav.jra.mock.ClientMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CaptchaTest extends AbstractTest {
    ClientMock client;

    @BeforeEach
    public void setUp() {
        TARGET_PATH = JSON_PATH.resolve("Captcha");
        client = new ClientMock();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @SuppressWarnings("deprecation")
    public void testNeedsCaptcha() throws IOException {
        client.json = readJson("Boolean.json");
        client.code = HttpURLConnection.HTTP_FORBIDDEN;

        assertThatThrownBy(() -> Captcha.needsCaptcha(client).query()).isInstanceOf(ForbiddenException.class);
    }
}
