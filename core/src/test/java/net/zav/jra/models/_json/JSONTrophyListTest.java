package net.zav.jra.models._json;

import net.zav.jra.models.Trophy;
import net.zav.jra.models.TrophyList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONTrophyListTest extends AbstractJSONTest{
    static String content;
    static TrophyList trophyList;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("TrophyList.json");
        trophyList = JSONTrophyList.fromThing(content);
    }

    @Test
    public void testFromJson(){
        assertThat(trophyList.sizeData()).isEqualTo(2);
        Trophy value;

        value = trophyList.getData(0);

        assertThat(value.getIcon70()).isEqualTo("https://www.redditstatic.com/awards2/3_year_club-70.png");
        assertThat(value.getIcon40()).isEqualTo("https://www.redditstatic.com/awards2/3_year_club-40.png");
        assertThat(value.getGrantedAt()).map(OffsetDateTime::toEpochSecond).contains(1592849930L);
        assertThat(value.getUrl()).isEmpty();
        assertThat(value.getName()).isEqualTo("Three-Year Club");
        assertThat(value.getAwardId()).isEmpty();
        assertThat(value.getId()).isEmpty();
        assertThat(value.getDescription()).isEmpty();

        value = trophyList.getData(1);

        assertThat(value.getIcon70()).isEqualTo("https://www.redditstatic.com/awards2/verified_email-70.png");
        assertThat(value.getIcon40()).isEqualTo("https://www.redditstatic.com/awards2/verified_email-40.png");
        assertThat(value.getGrantedAt()).isEmpty();
        assertThat(value.getUrl()).isEmpty();
        assertThat(value.getName()).isEqualTo("Verified Email");
        assertThat(value.getAwardId()).contains("o");
        assertThat(value.getId()).contains("1ridbv");
        assertThat(value.getDescription()).isEmpty();
    }

    @Test
    public void testToJson(){
        JSONObject root = JSONTrophyList.toJson(trophyList, new JSONObject());
        JSONArray data = root.getJSONArray(JSONTrophyList.KEY);
        Trophy value;

        assertThat(data.length()).isEqualTo(2);

        value = JSONTrophy.fromJson(new Trophy(), data.getJSONObject(0));

        assertThat(value.getIcon70()).isEqualTo("https://www.redditstatic.com/awards2/3_year_club-70.png");
        assertThat(value.getIcon40()).isEqualTo("https://www.redditstatic.com/awards2/3_year_club-40.png");
        assertThat(value.getGrantedAt()).map(OffsetDateTime::toEpochSecond).contains(1592849930L);
        assertThat(value.getUrl()).isEmpty();
        assertThat(value.getName()).isEqualTo("Three-Year Club");
        assertThat(value.getAwardId()).isEmpty();
        assertThat(value.getId()).isEmpty();
        assertThat(value.getDescription()).isEmpty();

        value = JSONTrophy.fromJson(new Trophy(), data.getJSONObject(1));

        assertThat(value.getIcon70()).isEqualTo("https://www.redditstatic.com/awards2/verified_email-70.png");
        assertThat(value.getIcon40()).isEqualTo("https://www.redditstatic.com/awards2/verified_email-40.png");
        assertThat(value.getGrantedAt()).isEmpty();
        assertThat(value.getUrl()).isEmpty();
        assertThat(value.getName()).isEqualTo("Verified Email");
        assertThat(value.getAwardId()).contains("o");
        assertThat(value.getId()).contains("1ridbv");
        assertThat(value.getDescription()).isEmpty();
    }
}
