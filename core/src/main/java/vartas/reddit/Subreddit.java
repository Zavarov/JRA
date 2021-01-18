package vartas.reddit;

import org.json.JSONObject;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.query.*;
import vartas.reddit.types.$factory.RulesFactory;
import vartas.reddit.types.Rules;
import vartas.reddit.types.Thing;

import java.io.IOException;

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
    public QueryComments getComments(String article){
        return new QueryComments(
                client,
                Endpoint.GET_SUBREDDIT_COMMENTS,
                getDisplayName(),
                article
        );
    }

    @Override
    public QueryControversial<Link> getControversialLinks(){
        return new QueryControversial<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_CONTROVERSIAL,
                getDisplayName()
        );
    }

    @Override
    public QueryHot<Link> getHotLinks(){
        return new QueryHot<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_HOT,
                getDisplayName()
        );
    }

    @Override
    public QueryNew<Link> getNewLinks(){
        return new QueryNew<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_NEW,
                getDisplayName()
        );
    }

    @Override
    public QueryRandom getRandomLink() {
        return new QueryRandom(
                client,
                Endpoint.GET_SUBREDDIT_RANDOM,
                getDisplayName()
        );
    }

    @Override
    public QueryRising<Link> getRisingLinks(){
        return new QueryRising<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_RISING,
                getDisplayName()
        );
    }

    @Override
    public QueryTop<Link> getTopLinks(){
        return new QueryTop<>(
                Thing::toLink,
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
    public QuerySearchSubreddit getSearch() {
        return new QuerySearchSubreddit(client, getDisplayName());
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
    public QuerySticky getSticky(){
        return new QuerySticky(client, getDisplayName());
    }
}
