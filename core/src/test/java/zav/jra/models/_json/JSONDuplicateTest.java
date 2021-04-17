package zav.jra.models._json;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zav.jra.mock.LinkMock;
import zav.jra.models.AbstractLink;
import zav.jra.models.Duplicate;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONDuplicateTest extends AbstractJSONTest{
    static String content;
    static Duplicate duplicate;

    @BeforeAll
    public static void setUpAll() throws IOException {
        content = getContent("Duplicates.json");
        duplicate = JSONDuplicate.fromJson(content, LinkMock::from);
    }

    @Test
    public void testFromJson(){
        AbstractLink source = duplicate.getSource();

        assertThat(source.getAuthor()).contains("securimancer");
        assertThat(source.getAuthorFlairCssClass()).isEmpty();
        assertThat(source.getAuthorFlairText()).isEmpty();
        assertThat(source.getClicked()).isFalse();
        assertThat(source.getDomain()).isEqualTo("self.redditdev");
        assertThat(source.getHidden()).isFalse();
        assertThat(source.getIsSelf()).isTrue();
        assertThat(source.getLinkFlairCssClass()).isEmpty();
        assertThat(source.getLinkFlairText()).contains("Reddit API");
        assertThat(source.getLocked()).isFalse();
        assertThat(source.getMedia()).isEmpty();
        assertThat(source.getMediaEmbed()).isNotNull();
        assertThat(source.getNumberOfComments()).isEqualTo(44);
        assertThat(source.getOver18()).isFalse();
        assertThat(source.getPermalink()).isEqualTo("/r/redditdev/comments/kvzaot/oauth2_api_changes_upcoming/");
        assertThat(source.getSaved()).isFalse();
        assertThat(source.getScore()).isEqualTo(38);
        assertThat(source.getSelftext()).contains("bla bla bla OAuth2 bla bla bla Modernizing");
        assertThat(source.getSelftextHtml()).contains("<!-- SC_OFF --><div class=\"md\"><p>bla bla bla OAuth2 bla bla bla Modernizing</p>\n</div><!-- SC_ON -->");
        assertThat(source.getSubreddit()).isEqualTo("redditdev");
        assertThat(source.getSubredditId()).isEqualTo("t5_2qizd");
        assertThat(source.getThumbnail()).isEmpty();
        assertThat(source.getTitle()).isEqualTo("OAuth2 API Changes Upcoming");
        assertThat(source.getUrl()).isEqualTo("https://www.reddit.com/r/redditdev/comments/kvzaot/oauth2_api_changes_upcoming/");
        assertThat(source.getEdited()).map(OffsetDateTime::toEpochSecond).contains(1610636014L);
        assertThat(source.getDistinguished()).contains("admin");
        assertThat(source.getStickied()).isTrue();
        assertThat(source.getSpoiler()).isFalse();
        assertThat(source.getCreated()).isEqualTo(1.610510216E9);
        assertThat(source.getCreatedUtc().toEpochSecond()).isEqualTo(1610481416L);
        assertThat(source.getUpvotes()).isEqualTo(38);
        assertThat(source.getDownvotes()).isEqualTo(0);
        assertThat(source.getLikes()).isEmpty();

        List<AbstractLink> duplicates = duplicate.getDuplicates();
        assertThat(duplicates).hasSize(1);
        AbstractLink duplicate = duplicates.get(0);

        assertThat(duplicate.getAuthor()).contains("Leading-Foundation12");
        assertThat(duplicate.getAuthorFlairCssClass()).isEmpty();
        assertThat(duplicate.getAuthorFlairText()).isEmpty();
        assertThat(duplicate.getClicked()).isFalse();
        assertThat(duplicate.getDomain()).isEqualTo("self.redditdev");
        assertThat(duplicate.getHidden()).isFalse();
        assertThat(duplicate.getIsSelf()).isFalse();
        assertThat(duplicate.getLinkFlairCssClass()).isEmpty();
        assertThat(duplicate.getLinkFlairText()).isEmpty();
        assertThat(duplicate.getLocked()).isFalse();
        assertThat(duplicate.getMedia()).isEmpty();
        assertThat(duplicate.getMediaEmbed()).isNotNull();
        assertThat(duplicate.getNumberOfComments()).isEqualTo(0);
        assertThat(duplicate.getOver18()).isTrue();
        assertThat(duplicate.getPermalink()).isEqualTo("/r/u_Leading-Foundation12/comments/kzpca1/0auth/");
        assertThat(duplicate.getSaved()).isFalse();
        assertThat(duplicate.getScore()).isEqualTo(1);
        assertThat(duplicate.getSelftext()).isEmpty();
        assertThat(duplicate.getSelftextHtml()).isEmpty();
        assertThat(duplicate.getSubreddit()).isEqualTo("u_Leading-Foundation12");
        assertThat(duplicate.getSubredditId()).isEqualTo("t5_3qqsfs");
        assertThat(duplicate.getThumbnail()).contains("nsfw");
        assertThat(duplicate.getTitle()).isEqualTo("0Auth");
        assertThat(duplicate.getUrl()).isEqualTo("/r/redditdev/comments/kvzaot/oauth2_api_changes_upcoming/");
        assertThat(duplicate.getEdited()).isEmpty();
        assertThat(duplicate.getDistinguished()).isEmpty();
        assertThat(duplicate.getStickied()).isFalse();
        assertThat(duplicate.getSpoiler()).isFalse();
        assertThat(duplicate.getCreated()).isEqualTo(1.610981491E9);
        assertThat(duplicate.getCreatedUtc().toEpochSecond()).isEqualTo(1610952691L);
        assertThat(duplicate.getUpvotes()).isEqualTo(1);
        assertThat(duplicate.getDownvotes()).isEqualTo(0);
        assertThat(duplicate.getLikes()).isEmpty();
    }
}
