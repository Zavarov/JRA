package vartas.reddit.query;

import vartas.reddit.Comment;
import vartas.reddit.Query;

import java.util.List;

public class QueryComments extends Query<Comment, QueryComments> {
    private static final String ARTICLE = "article";
    private static final String COMMENT = "comment";
    private static final String CONTEXT = "context";
    private static final String DEPTH = "depth";
    private static final String LIMIT = "limit";
    private static final String SHOW_EDITS = "showedits";
    private static final String SHOW_MEDIA = "showmedia";
    private static final String SHOW_MORE = "showmore";
    private static final String SORT = "sort";
    private static final String EXPAND_SUBREDDITS = "sr_detail";
    private static final String THREADED = "threaded";
    private static final String truncate = "truncate";

    @Override
    protected QueryComments getRealThis() {
        return this;
    }

    @Override
    public List<Comment> query() {
        throw new UnsupportedOperationException();
    }
}
