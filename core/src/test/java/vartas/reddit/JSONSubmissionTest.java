package vartas.reddit;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONSubmissionTest {
    Path path = Paths.get("src", "test", "resources", "Submission.json");
    Submission submission;

    @BeforeEach
    public void setUp() throws IOException {
        submission = new Submission(new JSONObject(Files.readString(path)));
    }

    @Test
    public void testGetAllComments(){
        assertThat(submission.getAllComments()).hasSize(3);
    }

    @Test
    public void testGetShortLink(){
        assertThat(submission.getShortLink()).isEqualTo("https://redd.it/s");
    }

    @Test
    public void testGetQualifiedTitle(){
        assertThat(submission.getQualifiedTitle()).isEqualTo("Title");
    }

    @Test
    public void testGetQualifiedPermaLink(){
        assertThat(submission.getQualifiedPermaLink()).isEqualTo("https://www.reddit.com/comments/s/-/");
    }
}
