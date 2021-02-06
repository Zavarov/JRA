package vartas.jra._json;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.Comment;

import java.io.IOException;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONCommentTest extends AbstractJSONTest{
    static Comment comment;
    @BeforeAll
    public static void setUpAll() throws IOException {
        comment = JSONComment.fromJson(new Comment(), from("Comment.json"));
        //Test the serialization methods as well
        comment = comment.toThing().toComment();

    }

    @Test
    public void testGetApprovedBy(){
        assertThat(comment.getApprovedBy()).isEmpty();
    }

    @Test
    public void testGetAuthor(){
        assertThat(comment.getAuthor()).isEqualTo("Zavarov");
    }

    @Test
    public void testGetAuthorFlairCssClass(){
        assertThat(comment.getAuthorFlairCssClass()).isEmpty();
    }

    @Test
    public void testGetAuthorFlairText(){
        assertThat(comment.getAuthorFlairText()).isEmpty();
    }

    @Test
    public void testGetBannedBy(){
        assertThat(comment.getBannedBy()).isEmpty();
    }

    @Test
    public void testGetBody(){
        assertThat(comment.getBody()).isEqualTo("Hello There");
    }

    @Test
    public void testGetBodyHtml(){
        assertThat(comment.getBodyHtml()).isEqualTo("&lt;div class=\"md\"&gt;&lt;p&gt;Hello There&lt;/p&gt;\n&lt;/div&gt;");
    }

    @Test
    public void testGetEdited(){
        assertThat(comment.getEdited()).map(OffsetDateTime::toEpochSecond).contains(1612370213L);
    }

    @Test
    public void testGetGilded(){
        assertThat(comment.getGilded()).isEqualTo(0);
    }

    @Test
    public void testGetLinkAuthor(){
        assertThat(comment.getLinkAuthor()).isEmpty();
    }

    @Test
    public void testGetLinkId(){
        assertThat(comment.getLinkId()).isEqualTo("t3_lbrhyc");
    }

    @Test
    public void testGetLinkTitle(){
        assertThat(comment.getLinkTitle()).isEmpty();
    }

    @Test
    public void testGetLinkUrl(){
        assertThat(comment.getLinkUrl()).isEmpty();
    }

    @Test
    public void testGetNumberOfReports(){
        assertThat(comment.getNumberOfReports()).isEmpty();
    }

    @Test
    public void testGetParentId(){
        assertThat(comment.getParentId()).isEqualTo("t3_lbrhyc");
    }

    @Test
    public void testGetReplies(){
        assertThat(comment.getReplies()).hasSize(1);
    }

    @Test
    public void testGetSaved(){
        assertThat(comment.getSaved()).isFalse();
    }

    @Test
    public void testGetScore(){
        assertThat(comment.getScore()).isEqualTo(1);
    }

    @Test
    public void testGetScoreHidden(){
        assertThat(comment.getScoreHidden()).isFalse();
    }

    @Test
    public void testtGetSubreddit(){
        assertThat(comment.getSubreddit()).isEqualTo("Zavarov");
    }

    @Test
    public void testGetDistinguished(){
        assertThat(comment.getDistinguished()).isEmpty();
    }

    @Test
    public void testGetCreated(){
        assertThat(comment.getCreated()).isEqualTo(1612398794);
    }

    @Test
    public void testGetCreatedUtc(){
        assertThat(comment.getCreatedUtc().toEpochSecond()).isEqualTo(1612369994);
    }

    @Test
    public void testGetUpvoted(){
        assertThat(comment.getUpvotes()).isEqualTo(1);
    }

    @Test
    public void testGetDownvotes(){
        assertThat(comment.getDownvotes()).isEqualTo(0);
    }

    @Test
    public void testGetLikes(){
        assertThat(comment.getLikes()).isEmpty();
    }
}
