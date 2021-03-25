package net.zav.jra.endpoints;

import net.zav.jra.AbstractClient;
import net.zav.jra.models.Listing;
import net.zav.jra.models.Thing;
import net.zav.jra.models._json.JSONListing;
import net.zav.jra.query.QueryGet;

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
