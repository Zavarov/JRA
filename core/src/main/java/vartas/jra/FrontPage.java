package vartas.jra;

import vartas.jra.query.QueryMany;
import vartas.jra.query.QueryOne;
import vartas.jra.types.Thing;

import javax.annotation.Nonnull;

public class FrontPage extends FrontPageTOP{
    private final Client client;

    public FrontPage(Client client){
        this.client = client;
    }

    @Override
    public FrontPage getRealThis() {
        return this;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Listings                                                                                                    //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Links sorted by {code best} have the highest ration between upvotes and downvotes.
     * @return A list of links.
     * @see Endpoint#GET_BEST
     */
    @Override
    @Nonnull
    public QueryMany<Link> getBestLinks() {
        return new QueryMany<>(
                source -> Thing.from(source).toLink(),
                client,
                Endpoint.GET_BEST
        );
    }

    /**
     * Links sorted by {code controversial} have recently received a high amount of upvotes <b>and</b> downvotes.
     * @see Endpoint#GET_CONTROVERSIAL
     * @return A list of links.
     */
    @Override
    @Nonnull
    public QueryMany<Link> getControversialLinks() {
        return new QueryMany<>(
                source -> Thing.from(source).toLink(),
                client,
                Endpoint.GET_CONTROVERSIAL
        );
    }

    /**
     * Links sorted by {code hot} have recently received a high amount of upvotes and/or comments.
     * @return A list of links.
     * @see Endpoint#GET_HOT
     */
    @Override
    @Nonnull
    public QueryMany<Link> getHotLinks() {
        return new QueryMany<>(
                source -> Thing.from(source).toLink(),
                client,
                Endpoint.GET_HOT
        );
    }

    /**
     * Links sorted by {code new} are ordered according to their submission date.
     * @return A list of links.
     * @see Endpoint#GET_NEW
     */
    @Override
    @Nonnull
    public QueryMany<Link> getNewLinks() {
        return new QueryMany<>(
                source -> Thing.from(source).toLink(),
                client,
                Endpoint.GET_NEW
        );
    }

    /**
     * The {@link Link} is chosen randomly from the {@code top} links.
     * @return A randomly fetched link.
     * @see #getTopLinks()
     * @see Endpoint#GET_RANDOM
     */
    @Override
    @Nonnull
    public QueryOne<Submission> getRandomSubmission() {
        return new QueryOne<>(
                Submission::from,
                client,
                Endpoint.GET_RANDOM
        );
    }

    /**
     * Links sorted by {code rising} have received a high amount of upvotes and/or comments right now.
     * @return A list of links.
     * @see Endpoint#GET_RISING
     */
    @Override
    @Nonnull
    public QueryMany<Link> getRisingLinks() {
        return new QueryMany<>(
                source -> Thing.from(source).toLink(),
                client,
                Endpoint.GET_RISING
        );
    }

    /**
     * Links sorted by {code top} have received a high amount of upvotes over an unspecified period of time.
     * @return A list of links.
     * @see Endpoint#GET_TOP
     */
    @Override
    @Nonnull
    public QueryMany<Link> getTopLinks() {
        return new QueryMany<>(
                source -> Thing.from(source).toLink(),
                client,
                Endpoint.GET_TOP
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Search                                                                                                      //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Provides access to the search function for links.
     * @return A list of things.
     * @see Endpoint#GET_SEARCH
     */
    @Override
    @Nonnull
    public QueryMany<Thing> getSearch() {
        return new QueryMany<>(
                Thing::from,
                client,
                Endpoint.GET_SEARCH
        );
    }
}
