package vartas.jra.models._json;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.models.Preferences;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONPreferencesTest extends AbstractJSONTest{
    static String content;
    static Preferences preferences;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("Preferences.json");
        preferences = JSONPreferences.fromJson(content);
    }

    @Test
    public void testFromJson(){
        assertThat(preferences.getDefaultThemeSubreddit()).isNotNull();
    }

    @Test
    public void testToJson(){
        JSONObject data = JSONPreferences.toJson(preferences, new JSONObject());

        assertThat(data.opt("default_theme_sr")).isNotNull();
    }
}
