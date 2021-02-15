package vartas.jra.models._json;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.mock.LinkMock;
import vartas.jra.models.AbstractLink;

import java.io.IOException;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONAbstractLinkTest extends AbstractJSONTest{
    static String content;
    static AbstractLink link;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("Link.json");
        link = JSONAbstractLink.fromJson(new LinkMock(), content);
    }

    @Test
    public void testFromJson(){
        assertThat(link.getCreatedUtc().toEpochSecond()).isEqualTo(1610481416L);
        assertThat(link.getEdited()).map(OffsetDateTime::toEpochSecond).contains(1610636014L);
        assertThat(link.getMedia()).isNotNull();
        assertThat(link.getMediaEmbed()).isNotNull();
        assertThat(link.getSelftextHtml()).contains("<!-- SC_OFF --><div class=\"md\"><p>bla bla bla OAuth2 bla bla bla Modernizing</p>\n</div><!-- SC_ON -->");
    }

    @Test
    public void testToJson(){
        JSONObject data = JSONAbstractLink.toJson(link, new JSONObject());

        assertThat(data.get("created_utc")).isEqualTo(1.610481416E9);
        assertThat(data.get("edited")).isEqualTo(1610636014L);
        assertThat(data.get("media")).isNotNull();
        assertThat(data.get("media_embed")).isNotNull();
        assertThat(data.getString("selftext_html")).isEqualTo("<!-- SC_OFF --><div class=\"md\"><p>bla bla bla OAuth2 bla bla bla Modernizing</p>\n</div><!-- SC_ON -->");
    }
}
