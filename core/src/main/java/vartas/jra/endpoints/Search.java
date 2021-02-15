package vartas.jra.endpoints;

import vartas.jra.AbstractClient;
import vartas.jra.models.Listing;
import vartas.jra.models.Thing;
import vartas.jra.models._json.JSONListing;
import vartas.jra.query.QueryGet;

import javax.annotation.Nonnull;

public abstract class Search {
    /**
     * Provides access to the search function for links.
     * @return A list of things.
     * @see Endpoint#GET_SEARCH
     */
    @Nonnull
    public static QueryGet<Listing<Thing>> getSearch(AbstractClient client) {
        return new QueryGet<>(
                JSONListing::fromThing,
                client,
                Endpoint.GET_SEARCH
        );
    }

    /**
     * Provides access to the search function for links.
     * @return A list of things.
     * @see Endpoint#GET_SUBREDDIT_SEARCH
     */
    @Nonnull
    public static QueryGet<Listing<Thing>> getSearch(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                JSONListing::fromThing,
                client,
                Endpoint.GET_SUBREDDIT_SEARCH,
                subreddit
        );
    }
}
