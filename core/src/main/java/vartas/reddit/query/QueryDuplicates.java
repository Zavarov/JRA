package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.types.Thing;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class QueryDuplicates<T> extends QueryThing<T,QueryDuplicates<T>> {
    private static final String ARTICLE = "article";
    private static final String CROSSPOSTS_ONLY = "crossposts_only";
    private static final String SORT = "sort";
    private static final String SUBREDDIT = "sr";

    public QueryDuplicates(Function<Thing, T> transformer, Client client, Endpoint endpoint, Object... args) {
        super(transformer, client, endpoint, args);
    }

    @Override
    protected QueryDuplicates<T> getRealThis() {
        return this;
    }

    public QueryDuplicates<T> setArticle(@Nonnull String article){
        args.put(ARTICLE, article);
        return this;
    }

    public QueryDuplicates<T> setCrossPostsOnly(boolean state){
        args.put(CROSSPOSTS_ONLY, state);
        return this;
    }

    public QueryDuplicates<T> setSort(@Nonnull Sort sort){
        args.put(SORT, sort);
        return this;
    }

    public QueryDuplicates<T> setSubreddit(@Nonnull String subreddit){
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
