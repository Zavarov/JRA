package vartas.jra.types;

import com.google.common.collect.ImmutableList;
import org.json.JSONArray;
import vartas.jra.types.$factory.CommentTypeFactory;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentType extends CommentTypeTOP{
    public static final String APPROVED_BY = "approved_by";
    public static final String AUTHOR = "author";
    public static final String AUTHOR_FLAIR_CSS_CLASS = "author_flair_css_class";
    public static final String AUTHOR_FLAIR_TEXT = "author_flair_text";
    public static final String BANNED_BY = "banned_by";
    public static final String BODY = "body";
    public static final String BODY_HTML = "body_html";
    public static final String EDITED = "edited";
    public static final String GILDED = "gilded";
    public static final String LINK_AUTHOR = "link_author";
    public static final String LINK_ID = "link_id";
    public static final String LINK_TITLE = "link_title";
    public static final String LINK_URL = "link_url";
    public static final String NUM_REPORTS = "num_reports";
    public static final String PARENT_ID = "parent_id";
    public static final String REPLIES = "replies";
    public static final String SAVED = "saved";
    public static final String SCORE = "score";
    public static final String SCORE_HIDDEN = "score_hidden";
    public static final String SUBREDDIT = "subreddit";
    public static final String SUBREDDIT_ID = "subreddit_id";
    public static final String DISTINGUISHED = "distinguished";

    @Override
    public CommentType getRealThis() {
        return this;
    }

    //---------------------------------------------------------------------------------------------------------------
    //
    //  Accessing JSON attributes
    //
    //---------------------------------------------------------------------------------------------------------------

    @Override
    public Optional<String> getApprovedBy() {
        return Optional.ofNullable(getSource().optString(APPROVED_BY, null));
    }

    @Override
    public String getAuthor() {
        return getSource().getString(AUTHOR);
    }

    @Override
    public Optional<String> getAuthorFlairCssClass() {
        String value = getSource().optString(AUTHOR_FLAIR_CSS_CLASS);
        return value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    @Override
    public Optional<String> getAuthorFlairText() {
        String value = getSource().optString(AUTHOR_FLAIR_TEXT);
        return value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    @Override
    public Optional<String> getBannedBy() {
        return Optional.ofNullable(getSource().optString(BANNED_BY, null));
    }

    @Override
    public String getBody() {
        return getSource().getString(BODY);
    }

    @Override
    public String getBodyHtml() {
        return getSource().getString(BODY_HTML);
    }

    @Override
    public Optional<OffsetDateTime> getEdited() {
        //Returns false if not edited, occasionally true for very old comments
        //Otherwise the edit-date in UTC
        if(getSource().get(EDITED) instanceof Boolean)
            return Optional.empty();
        else
            return Optional.of(getSource().getLong(EDITED))
                    .map(Instant::ofEpochSecond)
                    .map(instant -> OffsetDateTime.ofInstant(instant, ZoneOffset.UTC));
    }

    @Override
    public int getGilded() {
        return getSource().getInt(GILDED);
    }

    @Override
    public Optional<String> getLinkAuthor() {
        String value = getSource().optString(LINK_AUTHOR);
        return value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    @Override
    public String getLinkId() {
        return getSource().getString(LINK_ID);
    }

    @Override
    public Optional<String> getLinkTitle() {
        String value = getSource().optString(LINK_AUTHOR);
        return value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    @Override
    public Optional<String> getLinkUrl() {
        String value = getSource().optString(LINK_URL);
        return value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    @Override
    public Optional<Integer> getNumberOfReports() {
        if(getSource().isNull(NUM_REPORTS))
            return Optional.empty();
        else
            return Optional.of(getSource().getInt(NUM_REPORTS));
    }

    @Override
    public String getParentId() {
        return getSource().getString(PARENT_ID);
    }

    @Override
    public List<CommentType> getReplies() {
        JSONArray node = getSource().optJSONArray(REPLIES);
        List<CommentType> comments = new ArrayList<>();

        if(node != null)
            for(int i = 0 ; i < node.length() ; ++i)
                comments.add(CommentTypeFactory.create(CommentType::new, node.getJSONObject(i)));

        return ImmutableList.copyOf(comments);
    }

    @Override
    public boolean isSaved() {
        return getSource().getBoolean(SAVED);
    }

    @Override
    public int getScore() {
        return getSource().getInt(SCORE);
    }

    @Override
    public boolean isScoreHidden() {
        return getSource().getBoolean(SCORE_HIDDEN);
    }

    @Override
    public String getSubreddit() {
        return getSource().getString(SUBREDDIT);
    }

    @Override
    public String getSubredditId() {
        return getSource().getString(SUBREDDIT_ID);
    }

    @Override
    public Optional<String> getDistinguished() {
        return Optional.ofNullable(getSource().optString(DISTINGUISHED, null));
    }
}
