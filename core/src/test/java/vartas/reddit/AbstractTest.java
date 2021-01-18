package vartas.reddit;

import org.assertj.core.api.Condition;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import vartas.reddit.types.Karma;
import vartas.reddit.types.Thing;
import vartas.reddit.types.Trophy;
import vartas.reddit.types.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractTest {
    private static final NegativeOptionalNumber NEGATIVE_OPTIONAL_NUMBER = new NegativeOptionalNumber();
    private static final NegativeNumber NEGATIVE_NUMBER = new NegativeNumber();
    private static final InvalidOptionalString INVALID_OPTIONAL_STRING = new InvalidOptionalString();
    private static final InvalidString INVALID_STRING = new InvalidString();
    protected static final String SUBREDDIT_NAME = "RedditDev";

    protected static JSONObject getConfig() throws IOException {
        String content = Files.readString(Paths.get("src", "test", "resources", "config.json"));
        return new JSONObject(content);
    }

    protected static Client getScript(String version) throws IOException {
        JSONObject config = getConfig();

        String platform = config.getString("platform");
        String author = config.getString("name");
        String id = config.getString("id");
        String secret = config.getString("secret");
        String account = config.getString("account");
        String password = config.getString("password");

        return new ScriptClient(account,password,platform,author,version,id,secret);
    }

    protected static Client getUserless(String version) throws IOException {
        JSONObject config = getConfig();

        String platform = config.getString("platform");
        String author = config.getString("name");
        String id = config.getString("id");
        String secret = config.getString("secret");

        return new UserlessClient(platform,author,version,id,secret);
    }

    protected static void check(Thing thing){
        switch(thing.getKind()){
            case "t1":
                check(thing.toComment());
                break;
            case "t3":
                check(thing.toLink());
                break;
        }
    }

    protected static void check(Trophy trophy){
        LoggerFactory.getLogger(trophy.getClass()).info(trophy.toString());

        assertThat(trophy.getIcon70()).doesNotHave(INVALID_STRING);
        trophy.getGrantedAt(); //TODO Test
        assertThat(trophy.getName()).doesNotHave(INVALID_STRING);
        assertThat(trophy.getDescription()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(trophy.getId()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(trophy.getUrl()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(trophy.getIcon40()).doesNotHave(INVALID_STRING);
        assertThat(trophy.getAwardId()).doesNotHave(INVALID_OPTIONAL_STRING);
    }

    protected static void check(Karma karma){
        LoggerFactory.getLogger(karma.getClass()).info(karma.toString());

        assertThat(karma.getCommentKarma()).isNotNull();
        assertThat(karma.getLinkKarma()).isNotNull();
        assertThat(karma.getSubreddit()).doesNotHave(INVALID_STRING);
    }

    protected static void check(User user){
        LoggerFactory.getLogger(user.getClass()).info(user.toString());

        assertThat(user.getDate()).isNotNull();
        assertThat(user.getName()).isNotNull();
        assertThat(user.getRelativeId()).isNotNull();
        assertThat(user.getId()).isNotNull();
    }

    protected static void check(Link link){
        LoggerFactory.getLogger(link.getClass()).info(link.toString());

        assertThat(link.getAuthor()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(link.getAuthorFlairCssClass()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(link.getAuthorFlairText()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(link.isClicked()).isNotNull();
        assertThat(link.getDomain()).doesNotHave(INVALID_STRING);
        assertThat(link.isHidden()).isNotNull();
        assertThat(link.isSelf()).isNotNull();
        assertThat(link.getLinkFlairCssClass()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(link.getLinkFlairText()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(link.isLocked()).isNotNull();
        assertThat(link.getMedia()).isNotNull();
        assertThat(link.getMediaEmbed()).isNotNull();
        assertThat(link.getNumberOfComments()).doesNotHave(NEGATIVE_NUMBER);
        assertThat(link.isNsfw()).isNotNull();
        assertThat(link.getPermalink()).doesNotHave(INVALID_STRING);
        assertThat(link.isSaved()).isNotNull();
        assertThat(link.getScore()).isNotNull();
        assertThat(link.getSelftext()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(link.getSelftextHtml()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(link.getSubreddit()).doesNotHave(INVALID_STRING);
        assertThat(link.getSubredditId()).doesNotHave(INVALID_STRING);
        assertThat(link.getThumbnail()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(link.getTitle()).doesNotHave(INVALID_STRING);
        assertThat(link.getUrl()).doesNotHave(INVALID_STRING);
        link.getEdited();   //TODO Test
        assertThat(link.getDistinguished()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(link.isStickied()).isNotNull();
        assertThat(link.isSpoiler()).isNotNull();
    }

    protected static void check(Comment comment){
        LoggerFactory.getLogger(comment.getClass()).info(comment.toString());

        assertThat(comment.getApprovedBy()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(comment.getAuthor()).doesNotHave(INVALID_STRING);
        assertThat(comment.getAuthorFlairCssClass()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(comment.getAuthorFlairText()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(comment.getBannedBy()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(comment.getBody()).doesNotHave(INVALID_STRING);
        assertThat(comment.getBodyHtml()).doesNotHave(INVALID_STRING);
        comment.getEdited();   //TODO Test
        assertThat(comment.getGilded()).doesNotHave(NEGATIVE_NUMBER);
        assertThat(comment.getLinkAuthor()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(comment.getLinkId()).doesNotHave(INVALID_STRING);
        assertThat(comment.getLinkTitle()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(comment.getLinkUrl()).doesNotHave(INVALID_OPTIONAL_STRING);
        assertThat(comment.getNumberOfReports()).doesNotHave(NEGATIVE_OPTIONAL_NUMBER);
        assertThat(comment.getParentId()).doesNotHave(INVALID_STRING);
        comment.getReplies();   //TODO Test
        assertThat(comment.isSaved()).isNotNull();
        assertThat(comment.getScore()).isNotNull();
        assertThat(comment.isScoreHidden()).isNotNull();
        assertThat(comment.getSubreddit()).doesNotHave(INVALID_STRING);
        assertThat(comment.getDistinguished()).doesNotHave(INVALID_OPTIONAL_STRING);

    }

    private static class NegativeOptionalNumber extends Condition<Optional<Integer>>{
        public NegativeOptionalNumber(){
            super(x -> x.map(y -> y < 0).orElse(false), "Negative Optional Number");
        }
    }

    private static class NegativeNumber extends Condition<Integer>{
        public NegativeNumber(){
            super(x -> x < 0, "Negative Number");
        }
    }

    private static class InvalidOptionalString extends Condition<Optional<String>>{
        public InvalidOptionalString(){
            super(x -> x.map(String::isBlank).orElse(false), "Invalid Optional String");
        }
    }

    private static class InvalidString extends Condition<String>{
        public InvalidString(){
            super(x -> Objects.nonNull(x) && x.isBlank(), "Invalid String");
        }
    }
}
