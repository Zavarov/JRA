package proj.zav.jra.models._json;

import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import proj.zav.jra.models.Kind;
import proj.zav.jra.models.Listing;
import proj.zav.jra.models.Thing;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONListingTest extends AbstractJSONTest{
    static String content;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("Listing.json");
    }

    @Test
    public void testFromJson(){
        Listing<Thing> listing = JSONListing.fromJson(content, JSONThing::fromJson);
        List<Thing> children = Lists.newArrayList(listing);
        assertThat(children).hasSize(1);
        Thing child = children.get(0);

        assertThat(child.getData()).isNotNull();
        assertThat(child.getKind()).isEqualTo(Kind.Unknown);
    }

    @Test
    public void testToJson(){
        Listing<Thing> listing = JSONListing.fromJson(content, JSONThing::fromJson);
        JSONObject data = JSONListing.toJson(listing, new JSONObject());
        assertThat(data.has("children")).isTrue();
        JSONArray children = data.getJSONArray("children");
        assertThat(children.length()).isEqualTo(1);
        JSONObject child = children.getJSONObject(0);

        assertThat(child.get("data")).isNotNull();
        assertThat(child.getString("kind")).isEqualTo("Unknown");
    }

    @Test
    public void testFromThing(){
        Listing<Thing> listing = JSONListing.fromThing(content);
        List<Thing> children = Lists.newArrayList(listing);
        assertThat(children).hasSize(1);
        Thing child = children.get(0);

        assertThat(child.getData()).isNotNull();
        assertThat(child.getKind()).isEqualTo(Kind.Unknown);
    }

    @Test
    public void testToThing(){
        Listing<Thing> listing = JSONListing.fromThing(content);
        JSONObject data = JSONListing.toJson(listing, new JSONObject());
        assertThat(data.has("children")).isTrue();
        JSONArray children = data.getJSONArray("children");
        assertThat(children.length()).isEqualTo(1);
        JSONObject child = children.getJSONObject(0);

        assertThat(child.get("data")).isNotNull();
        assertThat(child.getString("kind")).isEqualTo("Unknown");
    }
}
