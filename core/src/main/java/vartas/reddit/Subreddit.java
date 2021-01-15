package vartas.reddit;

import de.se_rwth.commons.Joiners;
import org.json.JSONArray;
import org.json.JSONObject;
import vartas.reddit.$factory.LinkFactory;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.query.*;
import vartas.reddit.types.$factory.ListingFactory;
import vartas.reddit.types.$factory.RulesFactory;
import vartas.reddit.types.$factory.ThingFactory;
import vartas.reddit.types.Listing;
import vartas.reddit.types.Rules;
import vartas.reddit.types.Thing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class Subreddit extends SubredditTOP{
    private final Client client;

    public Subreddit(Client client){
        this.client = client;
    }

    @Override
    public Subreddit getRealThis() {
        return this;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Listings                                                                                                    //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public QueryComments getComments(){
        return new QueryComments();
    }

    @Override
    public QueryControversial<Link> getControversialLinks(){
        return new QueryControversial<>(
                Thing.THING2LINK,
                client,
                Endpoint.GET_SUBREDDIT_CONTROVERSIAL,
                getDisplayName()
        );
    }

    @Override
    public QueryHot<Link> getHotLinks(){
        return new QueryHot<>(
                Thing.THING2LINK,
                client,
                Endpoint.GET_SUBREDDIT_HOT,
                getDisplayName()
        );
    }

    @Override
    public QueryNew<Link> getNewLinks(){
        return new QueryNew<>(
                Thing.THING2LINK,
                client,
                Endpoint.GET_SUBREDDIT_NEW,
                getDisplayName()
        );
    }

    @Override
    public Link getRandomLink() throws IOException, HttpException, InterruptedException {
        JSONArray response = new JSONArray(client.get(Endpoint.GET_RANDOM, getDisplayName()));
        Thing thing;

        //For some reasons we get two JSON objects.
        //Both are listings, however only the first one contains a random submission.
        //The other one only contains junk data.
        //Maybe it's some kind of meta data?
        assert response.length() == 2;

        thing = ThingFactory.create(Thing::new, response.getJSONObject(0));

        //Did we really receive a listing
        assert Thing.Kind.Listing.matches(thing);

        Listing listing = ListingFactory.create(Listing::new, thing.getData());
        List<Thing> children = listing.getChildren();

        //Reddit should've only returned a single submission
        assert children.size() == 1;

        thing = children.get(0);

        //Did we really receive a submission
        assert Thing.Kind.Link.matches(thing);

        return LinkFactory.create(Link::new, thing.getData());
    }

    @Override
    public QueryRising<Link> getRisingLinks(){
        return new QueryRising<>(
                Thing.THING2LINK,
                client,
                Endpoint.GET_SUBREDDIT_RISING,
                getDisplayName()
        );
    }

    @Override
    public QueryTop<Link> getTopLinks(){
        return new QueryTop<>(
                Thing.THING2LINK,
                client,
                Endpoint.GET_SUBREDDIT_TOP,
                getDisplayName()
        );
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Rules getRules() throws InterruptedException, IOException, HttpException {
        JSONObject data = new JSONObject(client.get(Endpoint.GET_SUBREDDIT_ABOUT_RULES, getDisplayName()));
        return RulesFactory.create(Rules::new, data);
    }

    @Override
    public Query getSearch() {
        return new Query(this);
    }

    /**
     * @deprecated The sidebar endpoint seems to be deprecated in favor of #getDescription()
     */
    @Override
    @Deprecated()
    public String getSidebar() {
        return getDescription();
    }

    @Override
    public Link getSticky(int index) throws IOException, HttpException, InterruptedException {
        //Reddit only allows at most two submissions to be stickied
        assert index == 1 || index == 2;

        Map<String, Integer> args = new HashMap<>();
        args.put("num", index);

        JSONArray response = new JSONArray(client.get(args, Endpoint.GET_SUBREDDIT_STICKY, getDisplayName()));
        Thing thing;

        //For some reasons we get two JSON objects.
        //Both are listings, however only the first one contains a random submission.
        //The other one only contains junk data.
        //Maybe it's some kind of meta data?
        assert response.length() == 2;

        thing = ThingFactory.create(Thing::new, response.getJSONObject(0));

        //Did we really receive a listing?
        assert Thing.Kind.Listing.matches(thing);

        Listing listing = ListingFactory.create(Listing::new, thing.getData());

        //We should've only received a single submission
        //In case no submission exist, the request should've failed earlier with a 404
        assert listing.getChildren().size() == 1;

        thing = listing.getChildren().get(0);

        //Did we really receive a submission?
        assert Thing.Kind.Link.matches(thing);

        return LinkFactory.create(Link::new, thing.getData());
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Utility Classes                                                                                                   //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * The query is used to specify the parameters of the search endpoint.<p>
     *
     * @see #getSearch()
     * @see Endpoint#GET_SUBREDDIT_SEARCH
     */
    public static class Query{
        /**
         * The full name of the last {@link Thing} before the first element in the query.<p>
         * Default is {@code null}.
         */
        protected static final String AFTER = "after";
        /**
         * The full name of the first {@link Thing} after the last element in the query.<p>
         * Default is {@code null}.
         */
        protected static final String BEFORE = "before";
        /**
         * A string no longer than 5 characters. Setting this parameter may result in an internal server error.
         */
        protected static final String CATEGORY = "category";
        /**
         * A non-negative integer.<p>
         * Default is {@code 0}.
         */
        protected static final String COUNT = "count";
        /**
         * Results may contain hits from multiple subreddits.<p>
         * Default is {@code false}.
         */
        protected static final String INCLUDE_FACETS = "include_facets";
        /**
         * The total number of requested things. Has to be a number between [0,100].<p>
         * Default is {@code 25}.
         */
        protected static final String LIMIT = "limit";
        /**
         * A string no longer than 512 characters.
         */
        protected static final String QUERY = "q";
        /**
         * Limit results to this subreddit.<p>
         * Default is {@code true}.
         */
        protected static final String RESTRICT_SUBREDDIT = "restrict_sr";
        /**
         * The String "all".
         */
        protected static final String SHOW = "show";
        /**
         * The order in which the things are listed.
         * @see Sort
         */
        protected static final String SORT = "sort";
        /**
         * Expand subreddits.<p>
         * Default is {@code false}.
         */
        protected static final String EXPAND_SUBREDDIT = "sr_detail";
        /**
         * The time period limiting the age of the things. The age is relative to the time the query is made.
         * @see TimePeriod
         */
        protected static final String TIME_PERIOD = "t";
        /**
         * A list of result types.
         * @see Type
         */
        protected static final String TYPE = "type";
        /**
         * The entry point for communication with the endpoint.
         */
        private final Subreddit subreddit;
        /**
         * A map containing all customizable parameters of the query.
         */
        private final Map<String, Object> args = new HashMap<>();

        public Query(Subreddit subreddit){
            this.subreddit = subreddit;
        }

        public Query setAfter(@Nullable String after){
            args.put(AFTER, after);
            return this;
        }

        public Query setBefore(@Nullable String before){
            args.put(BEFORE, before);
            return this;
        }

        public Query setCategory(@Nonnull String category){
            assert category.length() <= 5;
            args.put(CATEGORY, category);
            return this;
        }

        public Query setCount(int count){
            assert count >= 0;
            args.put(COUNT, count);
            return this;
        }

        public Query includeFacets(boolean state){
            args.put(INCLUDE_FACETS, state);
            return this;
        }

        public Query setLimit(int limit){
            assert limit >= 0 && limit < 100;
            args.put(LIMIT, limit);
            return this;
        }

        public Query setQuery(@Nonnull String query){
            assert query.length() <= 512;
            args.put(QUERY, query);
            return this;
        }

        public Query restrictSubreddit(boolean state){
            args.put(RESTRICT_SUBREDDIT, state);
            return this;
        }

        public Query show(@Nullable String show){
            assert show == null || show.equals("all");
            args.put(SHOW, show);
            return this;
        }

        public Query setSort(@Nonnull Sort sort){
            args.put(SORT, sort);
            return this;
        }

        public Query expandSubreddits(boolean state){
            args.put(EXPAND_SUBREDDIT, state);
            return this;
        }

        public Query setTimePeriod(@Nonnull TimePeriod timePeriod){
            args.put(TIME_PERIOD, timePeriod);
            return this;
        }

        public Query setTypes(@Nonnull Type... types){
            args.put(TYPE, Joiners.COMMA.join(types));
            return this;
        }

        public List<Thing> query() throws IOException, HttpException, InterruptedException{
            assert args.containsKey(QUERY);
            assert args.containsKey(SORT);
            assert args.containsKey(TIME_PERIOD);

            //Default values
            args.putIfAbsent(AFTER, null);
            args.putIfAbsent(BEFORE, null);
            args.putIfAbsent(INCLUDE_FACETS, false);
            args.putIfAbsent(RESTRICT_SUBREDDIT, true);

            JSONObject response = new JSONObject(
                    subreddit.client.get(args, Endpoint.GET_SUBREDDIT_SEARCH, subreddit.getDisplayName())
            );

            Thing thing = ThingFactory.create(Thing::new, response);

            //We expect a listing containing the result of the search.
            assert Thing.Kind.Listing.matches(thing);

            Listing listing = ListingFactory.create(Listing::new, thing.getData());
            return Collections.unmodifiableList(listing.getChildren());
        }

        public enum Type{
            SUBREDDIT("sr"),
            LINK("link"),
            USER("user");

            private final String name;

            Type(String name){
                this.name = name;
            }

            @Override
            public String toString(){
                return name;
            }
        }

        public enum Sort{
            RELEVANCE,
            HOT,
            TOP,
            NEW,
            COMMENTS;

            @Override
            public String toString(){
                return name().toLowerCase(Locale.ENGLISH);
            }
        }

        public enum TimePeriod{
            HOUR,
            DAY,
            WEEK,
            MONTH,
            YEAR,
            ALL;

            @Override
            public String toString(){
                return name().toLowerCase(Locale.ENGLISH);
            }
        }
    }
}
