package vartas.jra._json;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.Account;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONAccountTest extends AbstractJSONTest{
    static Account account;

    @BeforeAll
    public static void setUpAll() throws IOException {
        account = JSONAccount.fromJson(new Account(null), from("Account.json"));
        //Test the serialization methods as well
        account = account.toThing().toAccount(null);
    }

    @AfterAll
    public static void tearDownAll(){
        account = null;
    }

    @Test
    public void testGetCommentKarma(){
        assertThat(account.getCommentKarma()).isEqualTo(1704);
    }

    @Test
    public void testGetHasMail(){
        assertThat(account.getHasMail()).isEmpty();
    }

    @Test
    public void testGetHasModMail(){
        assertThat(account.getHasModMail()).isEmpty();
    }

    @Test
    public void testGetHasVerifiedMail(){
        assertThat(account.getHasVerifiedEmail()).isTrue();
    }

    @Test
    public void testGetId(){
        assertThat(account.getId()).contains("10i8ax");
    }

    @Test
    public void testGetInboxCount(){
        assertThat(account.getInboxCount()).isEmpty();
    }

    @Test
    public void testGetIsFriend(){
        assertThat(account.getIsFriend()).isFalse();
    }

    @Test
    public void testGetIsGoldMember(){
        assertThat(account.getIsGoldMember()).isFalse();
    }

    @Test
    public void testGetIsMod(){
        assertThat(account.getIsMod()).isTrue();
    }

    @Test
    public void testGetLinkKarma(){
        assertThat(account.getLinkKarma()).isEqualTo(25);
    }

    @Test
    public void testGetModHash(){
        assertThat(account.getModHash()).isEmpty();
    }

    @Test
    public void testGetName(){
        assertThat(account.getName()).isEqualTo("Zavarov");
    }

    @Test
    public void testGetOver18(){
        assertThat(account.getOver18()).isEmpty();
    }

    @Test
    public void testGetHasSubscribed(){
        assertThat(account.getHasSubscribed()).isTrue();
    }

    @Test
    public void testGetAwarderKarma(){
        assertThat(account.getAwarderKarma()).contains(0);
    }

    @Test
    public void testGetAwardeeKarma(){
        assertThat(account.getAwardeeKarma()).contains(0);
    }

    @Test
    public void testGetIconImage(){
        assertThat(account.getIconImage()).isEqualTo("https://styles.redditmedia.com/t5_3oght/styles/profileIcon_eqa5rauhv5911.png?width=256&height=256&crop=256:256,smart&frame=1&s=1f2729185f04d7993bfe3f1a339e7575e88ff3b3");
    }

    @Test
    public void testGetTotalKarma(){
        assertThat(account.getTotalKarma()).isEqualTo(1729);
    }

    @Test
    public void testGetCreated(){
        assertThat(account.getCreated()).isEqualTo(1.471235015E9);
    }

    @Test
    public void testGetCreatedUtc(){
        assertThat(account.getCreatedUtc().toEpochSecond()).isEqualTo(1471206215L);
    }

    @Test
    public void testGetVerified(){
        assertThat(account.getVerified()).isTrue();
    }

    @Test
    public void testGetIsEmployee(){
        assertThat(account.getIsEmployee()).isFalse();
    }

    @Test
    public void testGetAcceptChats(){
        assertThat(account.getAcceptChats()).contains(false);
    }

    @Test
    public void testGetAcceptPrivateMessages(){
        assertThat(account.getAcceptPrivateMessages()).contains(true);
    }
}
