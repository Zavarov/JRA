package vartas.jra.models._json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.models.FakeAccount;
import vartas.jra.models.UserList;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONUserListTest extends AbstractJSONTest{
    static String content;
    static UserList userList;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("UserList.json");
        userList = JSONUserList.fromThing(content);
    }

    @Test
    public void testFromJson(){
        assertThat(userList.sizeData()).isEqualTo(1);

        FakeAccount value = userList.getData(0);

        assertThat(value.getData("date")).isEqualTo(1.234567890E9);
        assertThat(value.getData("name")).isEqualTo("Bernkastel");
        assertThat(value.getData("rel_id")).isEqualTo("r9_abcdefg");
        assertThat(value.getData("id")).isEqualTo("t2_124567");
    }

    @Test
    public void testToJson(){
        JSONObject root = JSONUserList.toJson(userList, new JSONObject());
        JSONArray data = root.getJSONArray(JSONUserList.KEY);

        assertThat(data.length()).isEqualTo(1);

        FakeAccount value = JSONFakeAccount.fromJson(new FakeAccount(), data.getJSONObject(0));

        assertThat(value.getData("date")).isEqualTo(1.234567890E9);
        assertThat(value.getData("name")).isEqualTo("Bernkastel");
        assertThat(value.getData("rel_id")).isEqualTo("r9_abcdefg");
        assertThat(value.getData("id")).isEqualTo("t2_124567");
    }
}
