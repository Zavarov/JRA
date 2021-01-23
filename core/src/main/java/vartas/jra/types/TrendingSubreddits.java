package vartas.jra.types;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrendingSubreddits extends TrendingSubredditsTOP{
    private static final String SUBREDDIT_NAMES = "subreddit_names";
    private static final String COMMENT_COUNT = "comment_count";
    private static final String COMMENT_URL = "comment_url";

    @Override
    public int getCommentCount() {
        return getSource().getInt(COMMENT_COUNT);
    }

    @Override
    public String getCommentUrl() {
        return getSource().getString(COMMENT_URL);
    }

    @Override
    public List<String> getSubredditNames() {
        JSONArray data = getSource().getJSONArray(SUBREDDIT_NAMES);
        List<String> result = new ArrayList<>(data.length());

        for(int i = 0 ; i < data.length() ; ++i)
            result.add(data.getString(i));

        return Collections.unmodifiableList(result);
    }

    @Override
    public TrendingSubreddits getRealThis() {
        return this;
    }
}
