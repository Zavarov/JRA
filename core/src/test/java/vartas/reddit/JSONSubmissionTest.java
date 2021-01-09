package vartas.reddit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vartas.reddit.$json.JSONSubmission;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONSubmissionTest {
    Path path = Paths.get("src", "test", "resources", "Submission.json");
    Submission submission;

    @BeforeEach
    public void setUp() throws IOException {
        submission = JSONSubmission.fromJson(new Submission(), path);
    }

    @Test
    public void testGetShortLink(){
        assertThat(submission.getShortLink()).isEqualTo("https://redd.it/s");
    }

    @Test
    public void testGetQualifiedTitle(){
        assertThat(submission.getQualifiedTitle()).isEqualTo("Title");
        submission.setNsfw(true);
        assertThat(submission.getQualifiedTitle()).isEqualTo("Title [NSFW]");
        submission.setSpoiler(true);
        assertThat(submission.getQualifiedTitle()).isEqualTo("Title [Spoiler] [NSFW]");
    }

    @Test
    public void testGetQualifiedPermaLink(){
        assertThat(submission.getQualifiedPermaLink()).isEqualTo("https://www.reddit.com/comments/s/-/");
    }
}
