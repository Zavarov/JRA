package proj.zav.jra.models._json;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import proj.zav.jra.models.FakeAccount;
import proj.zav.jra.models.Messaging;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONMessagingTest extends AbstractJSONTest{
    static String content;
    static Messaging messaging;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("Messaging.json");
        messaging = JSONMessaging.fromJson(content);
    }

    @Test
    public void testFromJson(){
        List<FakeAccount> accounts;
        FakeAccount account;

        accounts = messaging.getBlocked();
        assertThat(accounts).hasSize(1);
        account = accounts.get(0);

        assertThat(account.getData("date")).isEqualTo(1612556744);
        assertThat(account.getData("name")).isEqualTo("Jimbo");
        assertThat(account.getData("rel_id")).isEqualTo("r9_aabbcc");
        assertThat(account.getData("id")).isEqualTo("t2_ccddee");

        accounts = messaging.getTrusted();
        assertThat(accounts).hasSize(1);
        account = accounts.get(0);

        assertThat(account.getData("date")).isEqualTo(1522543721);
        assertThat(account.getData("name")).isEqualTo("Citrine");
        assertThat(account.getData("rel_id")).isEqualTo("r9_ddeeff");
        assertThat(account.getData("id")).isEqualTo("t2_ffgghh");
    }
}
