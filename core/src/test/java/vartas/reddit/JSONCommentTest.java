package vartas.reddit;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONCommentTest {
    static Path commentPath = Paths.get("src", "test", "resources", "Comment.json");
    static Path submissionPath = Paths.get("src", "test", "resources", "Submission.json");
    static Submission submission;
    static Comment comment;
    @BeforeAll
    public static void setUpAll() throws IOException {
        submission = new Submission(new JSONObject(Files.readString(submissionPath)));
        comment = new Comment(submission.getId(), new JSONObject(Files.readString(commentPath)));
    }

    @Test
    public void testGetSubmission(){
        assertThat(comment.getSubmission()).isEqualTo(submission.getId());
    }

    @Test
    public void testGetQualifiedPermaLink(){
        assertThat(comment.getQualifiedPermaLink()).isEqualTo("https://www.reddit.com/comments/s/-/c/");
    }
}
