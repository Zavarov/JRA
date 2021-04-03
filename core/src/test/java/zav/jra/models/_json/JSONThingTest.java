package zav.jra.models._json;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zav.jra.models.Kind;
import zav.jra.models.Thing;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONThingTest extends AbstractJSONTest{
    static String content;
    static Thing thing;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("Thing.json");
        thing = JSONThing.fromJson(content);
    }

    @Test
    public void testFromJson(){
        assertThat(thing.getName()).contains("t6_abcdef");
        assertThat(thing.getKind()).isEqualTo(Kind.Award);
        assertThat(thing.getId()).contains("abcdef");
        assertThat(thing.getData()).isEqualTo("content");
    }

    @Test
    public void testToJson(){
        JSONObject data = JSONThing.toJson(thing, new JSONObject());

        assertThat(data.get("name")).isEqualTo("t6_abcdef");
        assertThat(data.get("kind")).isEqualTo("t6");
        assertThat(data.get("id")).isEqualTo("abcdef");
        assertThat(data.get("data")).isEqualTo("content");
    }
}
