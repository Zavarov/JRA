package net.zav.jra.models._json;

import net.zav.jra.mock.PreferencesMock;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONAbstractPreferencesTest extends AbstractJSONTest{
    static String content;
    static PreferencesMock preferences;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("Preferences.json");
        preferences = JSONAbstractPreferences.fromJson(content, PreferencesMock::new);
    }

    @Test
    public void testFromJson(){
        Assertions.assertThat(preferences.getDefaultThemeSubreddit()).isNotNull();
    }

    @Test
    public void testToJson(){
        JSONObject data = JSONAbstractPreferences.toJson(preferences, new JSONObject());

        assertThat(data.opt("default_theme_sr")).isNotNull();
    }
}
