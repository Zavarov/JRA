package vartas.reddit;

import javax.annotation.Nonnull;

public abstract class Comment extends CommentTOP{
    @Nonnull
    private static final String QUALIFIED_PERMALINK = "https://www.reddit.com/comments/%s/-/%s/";

    @Nonnull
    @Override
    public String getQualifiedPermaLink() {
        return String.format(QUALIFIED_PERMALINK, getSubmission().getId(), getId());
    }

    @Nonnull
    @Override
    public Comment getRealThis() {
        return this;
    }
}
