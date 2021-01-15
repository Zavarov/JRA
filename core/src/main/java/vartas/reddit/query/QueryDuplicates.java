package vartas.reddit.query;

import javax.annotation.Nonnull;

public class QueryDuplicates extends QueryLink<QueryDuplicates> {
    private static final String ARTICLE = "article";
    private static final String CROSSPOSTS_ONLY = "crossposts_only";
    private static final String SORT = "sort";
    private static final String SUBREDDIT = "sr";

    @Override
    protected QueryDuplicates getRealThis() {
        return this;
    }

    public QueryDuplicates setArticle(@Nonnull String article){
        args.put(ARTICLE, article);
        return this;
    }

    public QueryDuplicates setCrossPostsOnly(boolean state){
        args.put(CROSSPOSTS_ONLY, state);
        return this;
    }

    public QueryDuplicates setSort(@Nonnull Sort sort){
        args.put(SORT, sort);
        return this;
    }

    public QueryDuplicates setSubreddit(@Nonnull String subreddit){
        args.put(SUBREDDIT, subreddit);
        return this;
    }

    public enum Sort{
        NUMBER_OF_COMMENTS("num_comments"),
        NEW("new");

        private final String name;
        Sort(String name){
            this.name = name;
        }

        @Override
        public String toString(){
            return name;
        }
    }
}
