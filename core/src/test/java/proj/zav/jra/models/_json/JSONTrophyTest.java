package zav.jra.models._json;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zav.jra.models.Trophy;

import java.io.IOException;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONTrophyTest extends AbstractJSONTest{
    static String content;
    static Trophy trophy;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("Trophy.json");
        trophy = JSONTrophy.fromThing(content);
    }

    @Test
    public void testFromJson(){
        assertThat(trophy.getIcon70()).isEqualTo("https://www.redditstatic.com/awards2/3_year_club-70.png");
        assertThat(trophy.getIcon40()).isEqualTo("https://www.redditstatic.com/awards2/3_year_club-40.png");
        assertThat(trophy.getGrantedAt()).map(OffsetDateTime::toEpochSecond).contains(1592849930L);
        assertThat(trophy.getUrl()).isEmpty();
        assertThat(trophy.getName()).isEqualTo("Three-Year Club");
        assertThat(trophy.getAwardId()).isEmpty();
        assertThat(trophy.getId()).isEmpty();
        assertThat(trophy.getDescription()).isEmpty();
    }

    @Test
    public void testToJson(){
        JSONObject data = JSONTrophy.toJson(trophy, new JSONObject());

        assertThat(data.get("icon_70")).isEqualTo("https://www.redditstatic.com/awards2/3_year_club-70.png");
        assertThat(data.get("icon_40")).isEqualTo("https://www.redditstatic.com/awards2/3_year_club-40.png");
        assertThat(data.get("granted_at")).isEqualTo(1592849930.0);
        assertThat(data.isNull("url")).isTrue();
        assertThat(data.get("name")).isEqualTo("Three-Year Club");
        assertThat(data.isNull("award_id")).isTrue();
        assertThat(data.isNull("id")).isTrue();
        assertThat(data.isNull("description")).isTrue();
    }
}
