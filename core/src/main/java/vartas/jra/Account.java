package vartas.jra;

import org.json.JSONObject;
import vartas.jra.query.QueryMany;
import vartas.jra.types.Thing;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class Account extends AccountTOP {
    /**
     * A reference to the client required for communicating with the account-specific endpoints.
     */
    @Nonnull
    private final Client client;
    @Nonnull
    private final JSONObject source;

    /**
     * Creates a new instance of an account.
     * @param client The client communicating with the endpoints.
     */
    @Nonnull
    public Account(@Nonnull Client client, @Nonnull JSONObject source){
        this.client = client;
        this.source = source;
    }

    @Override
    public QueryMany<Thing> getComments() {
        return new QueryMany<>(Function.identity(), client, Endpoint.GET_USER_USERNAME_COMMENTS, getName());
    }

    @Override
    public QueryMany<Thing> getDownvoted() {
        return new QueryMany<>(Function.identity(), client, Endpoint.GET_USER_USERNAME_DOWNVOTED, getName());
    }

    @Override
    public QueryMany<Thing> getGilded() {
        return new QueryMany<>(Function.identity(), client, Endpoint.GET_USER_USERNAME_GILDED, getName());
    }

    @Override
    public QueryMany<Thing> getHidden() {
        return new QueryMany<>(Function.identity(), client, Endpoint.GET_USER_USERNAME_HIDDEN, getName());
    }

    @Override
    public QueryMany<Thing> getOverview() {
        return new QueryMany<>(Function.identity(), client, Endpoint.GET_USER_USERNAME_OVERVIEW, getName());
    }

    @Override
    public QueryMany<Thing> getSaved() {
        return new QueryMany<>(Function.identity(), client, Endpoint.GET_USER_USERNAME_SAVED, getName());
    }

    @Override
    public QueryMany<Thing> getSubmitted() {
        return new QueryMany<>(Function.identity(), client, Endpoint.GET_USER_USERNAME_SUBMITTED, getName());
    }

    @Override
    public QueryMany<Thing> getTrophies() {
        return new QueryMany<>(Function.identity(), client, Endpoint.GET_USER_USERNAME_TROHPIES, getName());
    }

    @Override
    public QueryMany<Thing> getUpvoted() {
        return new QueryMany<>(Function.identity(), client, Endpoint.GET_USER_USERNAME_UPVOTED, getName());
    }

    @Override
    public JSONObject getSource() {
        return source;
    }

    @Override
    public Account getRealThis() {
        return this;
    }
}
