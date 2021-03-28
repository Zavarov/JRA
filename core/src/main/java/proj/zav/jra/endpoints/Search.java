package proj.zav.jra.endpoints;

import proj.zav.jra.AbstractClient;
import proj.zav.jra.models.Listing;
import proj.zav.jra.models.Thing;
import proj.zav.jra.models._json.JSONListing;
import proj.zav.jra.query.QueryGet;

import javax.annotation.Nonnull;

public abstract class Search {
    /**
     * Provides access to the search function for links.
     * @param client The client performing the request.
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
     * @param client The client performing the request.
     * @param subreddit The subreddit within which is searched.
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
