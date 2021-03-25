package net.zav.jra.models._json;

import net.zav.jra.models.TrendingSubreddits;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONTrendingSubredditTest extends AbstractJSONTest {
    static String content;
    static TrendingSubreddits trending;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("TrendingSubreddits.json");
        trending = JSONTrendingSubreddits.fromJson(new TrendingSubreddits(), content);
    }

    @Test
    public void testFromJson() {
        assertThat(trending.getCommentCount()).isEqualTo(4);
        assertThat(trending.getCommentUrl()).isEqualTo("/r/trendingsubreddits/comments/l5xdvw/trending_subreddits_for_20210127_rsmallstreetbets/");
        assertThat(trending.getSubredditNames()).containsExactlyInAnyOrder("smallstreetbets","bluejackets","japan","biggreenegg","BigMouth");
    }

    @Test
    public void testToJson() {
        JSONObject value = JSONTrendingSubreddits.toJson(trending, new JSONObject());

        assertThat(value.getInt("comment_count")).isEqualTo(4);
        assertThat(value.getString("comment_url")).isEqualTo("/r/trendingsubreddits/comments/l5xdvw/trending_subreddits_for_20210127_rsmallstreetbets/");
        assertThat(value.getJSONArray("subreddit_names")).containsExactlyInAnyOrder("smallstreetbets","bluejackets","japan","biggreenegg","BigMouth");
    }
}
