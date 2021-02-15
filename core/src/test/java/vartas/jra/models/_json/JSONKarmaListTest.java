package vartas.jra.models._json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.models.Karma;
import vartas.jra.models.KarmaList;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONKarmaListTest extends AbstractJSONTest{
    static String content;
    static KarmaList karmaList;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("KarmaList.json");
        karmaList = JSONKarmaList.fromThing(content);
    }

    @Test
    public void testFromJson(){
        assertThat(karmaList.sizeData()).isEqualTo(1);

        Karma value = karmaList.getData(0);

        assertThat(value.getSubreddit()).isEqualTo("bananapics");
        assertThat(value.getLinkKarma()).isEqualTo(42);
        assertThat(value.getCommentKarma()).isEqualTo(69);
    }

    @Test
    public void testToJson(){
        JSONObject root = JSONKarmaList.toJson(karmaList, new JSONObject());
        JSONArray data = root.getJSONArray(JSONKarmaList.KEY);

        assertThat(data.length()).isEqualTo(1);

        Karma value = JSONKarma.fromJson(new Karma(), data.getJSONObject(0));

        assertThat(value.getSubreddit()).isEqualTo("bananapics");
        assertThat(value.getLinkKarma()).isEqualTo(42);
        assertThat(value.getCommentKarma()).isEqualTo(69);
    }
}
