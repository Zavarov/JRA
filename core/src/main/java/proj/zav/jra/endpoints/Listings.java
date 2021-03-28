package proj.zav.jra.endpoints;

import com.google.common.base.Joiner;
import proj.zav.jra.AbstractClient;
import proj.zav.jra.http.APIRequest;
import proj.zav.jra.models.*;
import proj.zav.jra.models._json.JSONDuplicate;
import proj.zav.jra.models._json.JSONListing;
import proj.zav.jra.models._json.JSONSubmission;
import proj.zav.jra.models._json.JSONTrendingSubreddits;
import proj.zav.jra.query.QueryGet;

import javax.annotation.Nonnull;
import java.util.function.Function;

public abstract class Listings {
    /**
     * Return a list of trending subreddits, link to the {@link AbstractComment Comment} in
     * {@code r/trendingsubreddits}, and the {@link AbstractComment Comment} count of that {@link AbstractLink Link}.
     * @param client The client performing the request.
     * @return An instance of the trending subreddits.
     * @see Endpoint#GET_API_TRENDING_SUBREDDITS
     */
    @Nonnull
    public static QueryGet<TrendingSubreddits> getTrendingSubreddits(AbstractClient client){
        QueryGet<TrendingSubreddits> query = new QueryGet<>(
                source -> JSONTrendingSubreddits.fromJson(new TrendingSubreddits(), source),
                client,
                Endpoint.GET_API_TRENDING_SUBREDDITS
        );

        //For some weird reason, api/trending_subreddits is the only endpoint not supporting OAuth2...
        query.setHost(APIRequest.WWW);

        return query;
    }

    /**
     * Links sorted by {code best} have the highest ration between upvotes and downvotes.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @return A list of links.
     * @see Endpoint#GET_BEST
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getBestLinks(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_BEST
        );
    }

    /**
     * Get a listing of links by fullname.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param names A sequence of {@link AbstractLink Link} fullnames.
     * @return A list of links with the specified fullnames.
     * @see Endpoint#GET_BY_ID
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getLinksById(AbstractClient client, Function<Thing, T> mapper, @Nonnull String... names) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_BY_ID,
                Joiner.on(',').join(names)
        );
    }

    /**
     * Get the comment tree for a given {@link AbstractLink Link} article.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the {@link Submission} link.
     * @param subreddit The subreddit associated with the {@link Submission}.
     * @param article The base 36 id of a {@link AbstractLink Link}.
     * @param <T> The target type.
     * @return A {@link Submission} instance corresponding to the {@link AbstractLink Link}.
     * @see Endpoint#GET_SUBREDDIT_COMMENTS
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Submission> getComments(AbstractClient client, Function<Thing, T> mapper, String subreddit, String article) {
        return new QueryGet<>(
                source -> JSONSubmission.from(source, mapper),
                client,
                Endpoint.GET_SUBREDDIT_COMMENTS,
                subreddit,
                article
        );
    }

    /**
     * Get the comment tree for a given {@link AbstractLink Link} article.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the {@link Submission} link.
     * @param article The base 36 id of a {@link AbstractLink Link}.
     * @param <T> The target type.
     * @return A {@link Submission} instance corresponding to the {@link AbstractLink Link}.
     * @see Endpoint#GET_COMMENTS
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Submission> getComments(AbstractClient client, Function<Thing, T> mapper, String article) {
        return new QueryGet<>(
                source -> JSONSubmission.from(source, mapper),
                client,
                Endpoint.GET_COMMENTS,
                article
        );
    }

    /**
     * Return a list of other links of the same URL. This happens, for example, if a link is cross-posted to another
     * {@link AbstractSubreddit Subreddit}.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Duplicate}.
     * @param article The base 36 id of a {@link AbstractLink Link}.
     * @param <T> The target type.
     * @return An instance containing all duplicate links.
     * @see Endpoint#GET_DUPLICATES
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Duplicate> getDuplicates(AbstractClient client, Function<Thing, T> mapper, String article) {
        return new QueryGet<>(
                source -> JSONDuplicate.fromJson(source, mapper),
                client,
                Endpoint.GET_DUPLICATES,
                article
        );
    }

    /**
     * Links sorted by {code hot} have recently received a high amount of upvotes and/or comments.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param subreddit The subreddit associated with the links.
     * @param <T> The target type.
     * @return A list of links.
     * @see Endpoint#GET_SUBREDDIT_HOT
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getHotLinks(AbstractClient client, Function<Thing, T> mapper, String subreddit){
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDIT_HOT,
                subreddit
        );
    }

    /**
     * Links sorted by {code hot} have recently received a high amount of upvotes and/or comments.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param <T> The target type.
     * @return A list of links.
     * @see Endpoint#GET_HOT
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getHotLinks(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_HOT
        );
    }

    /**
     * Links sorted by {code new} are ordered according to their submission date.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param subreddit The subreddit associated with the links.
     * @param <T> The target type.
     * @return A list of links.
     * @see Endpoint#GET_SUBREDDIT_NEW
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getNewLinks(AbstractClient client, Function<Thing, T> mapper, String subreddit){
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDIT_NEW,
                subreddit
        );
    }

    /**
     * Links sorted by {code new} are ordered according to their submission date.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param <T> The target type.
     * @return A list of links.
     * @see Endpoint#GET_NEW
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getNewLinks(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_NEW
        );
    }

    /**
     * <p>
     * Fetches a random {@link Submission} from the {@link AbstractSubreddit Subreddit}.
     * </p>
     * <p>
     * The {@link Submission} corresponds to one of the current top links.
     * </p>
     * @param client The client performing the request.
     * @param mapper A mapper function processing the {@link Submission} link.
     * @param subreddit The subreddit associated with the {@link Submission}.
     * @param <T> The target type.
     * @return A random {@link Submission}.
     * @see #getRandomSubmission(AbstractClient, Function, String)
     * @see Endpoint#GET_SUBREDDIT_RANDOM
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Submission> getRandomSubmission(AbstractClient client, Function<Thing, T> mapper, String subreddit) {
        return new QueryGet<>(
                source -> JSONSubmission.from(source, mapper),
                client,
                Endpoint.GET_SUBREDDIT_RANDOM,
                subreddit
        );
    }

    /**
     * The {@link AbstractLink Link} is chosen randomly from the {@code top} links.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the {@link Submission} link.
     * @param <T> The target type.
     * @return A randomly fetched link.
     * @see #getRandomSubmission(AbstractClient, Function)
     * @see Endpoint#GET_RANDOM
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Submission> getRandomSubmission(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONSubmission.from(source, mapper),
                client,
                Endpoint.GET_RANDOM
        );
    }

    /**
     * Links sorted by {code rising} have received a high amount of upvotes and/or comments right now.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param subreddit The subreddit associated with the links.
     * @param <T> The target type.
     * @return A list of links.
     * @see Endpoint#GET_SUBREDDIT_RISING
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getRisingLinks(AbstractClient client, Function<Thing, T> mapper, String subreddit){
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDIT_RISING,
                subreddit
        );
    }

    /**
     * Links sorted by {code rising} have received a high amount of upvotes and/or comments right now.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param <T> The target type.
     * @return A list of links.
     * @see Endpoint#GET_RISING
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getRisingLinks(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_RISING
        );
    }

    /**
     * Links sorted by {code top} have received a high amount of upvotes over an unspecified period of time.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param subreddit The subreddit associated with the links.
     * @param <T> The target type.
     * @return A list of links.
     * @see Endpoint#GET_SUBREDDIT_TOP
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getTopLinks(AbstractClient client, Function<Thing, T> mapper, String subreddit){
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDIT_TOP,
                subreddit
        );
    }

    /**
     * Links sorted by {code top} have received a high amount of upvotes over an unspecified period of time.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param <T> The target type.
     * @return A list of links.
     * @see Endpoint#GET_TOP
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getTopLinks(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_TOP
        );
    }

    /**
     * Links sorted by {code controversial} have recently received a high amount of upvotes <b>and</b> downvotes.
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param subreddit The subreddit associated with the links.
     * @param <T> The target type.
     * @return A list of links.
     * @see Endpoint#GET_SUBREDDIT_CONTROVERSIAL
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getControversialLinks(AbstractClient client, Function<Thing, T> mapper, String subreddit) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDIT_CONTROVERSIAL,
                subreddit
        );
    }

    /**
     * Links sorted by {code controversial} have recently received a high amount of upvotes <b>and</b> downvotes.
     * @see Endpoint#GET_CONTROVERSIAL
     * @param client The client performing the request.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param <T> The target type.
     * @return A list of links.
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getControversialLinks(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_CONTROVERSIAL
        );
    }
}
