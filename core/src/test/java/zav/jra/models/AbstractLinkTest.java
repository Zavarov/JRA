package zav.jra.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zav.jra.AbstractTest;
import zav.jra.mock.LinkMock;

import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractLinkTest extends AbstractTest {
    AbstractLink link;

    @BeforeEach
    public void setUp() throws IOException {
        TARGET_PATH = Paths.get("src", "test", "resources");
        link = LinkMock.from(readJson("Link.json"));
    }

    @Test
    public void testGetPermalink(){
        assertThat(AbstractLink.getPermalink(link)).isEqualTo("https://www.reddit.com/r/redditdev/comments/miqx0k/on_the_new_rate_limit_for_editing_comments/");
    }

    @Test
    public void testGetQualifiedTitleOver18(){
        link.setOver18(true);
        link.setSpoiler(false);

        assertThat(AbstractLink.getQualifiedTitle(link)).isEqualTo("[Reddit API] On the new rate limit for editing comments [NSFW]");
    }

    @Test
    public void testGetQualifiedTitleSpoiler(){
        link.setOver18(false);
        link.setSpoiler(true);

        assertThat(AbstractLink.getQualifiedTitle(link)).isEqualTo("[Reddit API] On the new rate limit for editing comments [Spoiler]");
    }

    @Test
    public void testGetQualifiedTitleSpoilerAndOver18(){
        link.setOver18(true);
        link.setSpoiler(true);

        assertThat(AbstractLink.getQualifiedTitle(link)).isEqualTo("[Reddit API] On the new rate limit for editing comments [Spoiler] [NSFW]");
    }

    @Test
    public void testGetQualifiedTitle(){
        link.setOver18(false);
        link.setSpoiler(false);

        assertThat(AbstractLink.getQualifiedTitle(link)).isEqualTo("[Reddit API] On the new rate limit for editing comments");
    }

    @Test
    public void testGetShortLink(){
        assertThat(AbstractLink.getShortLink(link)).isEqualTo("https://redd.it/miqx0k");
    }
}
