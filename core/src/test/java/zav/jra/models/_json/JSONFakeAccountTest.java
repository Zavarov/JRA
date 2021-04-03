package zav.jra.models._json;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zav.jra.models.FakeAccount;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONFakeAccountTest extends AbstractJSONTest{
    static String content;
    static FakeAccount account;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("FakeAccount.json");
        account = JSONFakeAccount.fromJson(new FakeAccount(), content);
    }

    @Test
    public void testFromJson(){
        assertThat(account.getData("date")).isEqualTo(BigDecimal.valueOf(1234567890));
        assertThat(account.getData("name")).isEqualTo("Bernkastel");
        assertThat(account.getData("rel_id")).isEqualTo("r9_abcdefg");
        assertThat(account.getData("id")).isEqualTo("t2_124567");
    }

    @Test
    public void testToJson(){
        JSONObject data = JSONFakeAccount.toJson(account, new JSONObject());

        assertThat(data.get("date")).isEqualTo(BigDecimal.valueOf(1234567890));
        assertThat(data.get("name")).isEqualTo("Bernkastel");
        assertThat(data.get("rel_id")).isEqualTo("r9_abcdefg");
        assertThat(data.get("id")).isEqualTo("t2_124567");
    }
}
