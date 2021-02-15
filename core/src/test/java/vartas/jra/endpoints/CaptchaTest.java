package vartas.jra.endpoints;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.AbstractClient;
import vartas.jra.AbstractTest;
import vartas.jra.exceptions.ForbiddenException;
import vartas.jra.exceptions.HttpException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CaptchaTest extends AbstractTest {
    static AbstractClient client;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(CaptchaTest.class.getSimpleName());
        client.login(AbstractClient.Duration.TEMPORARY);
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @SuppressWarnings("deprecation")
    public void testNeedsCaptcha() {
        assertThatThrownBy(() -> Captcha.needsCaptcha(client).query()).isInstanceOf(ForbiddenException.class);
    }
}
