package proj.zav.jra.endpoints;

import org.json.JSONObject;
import proj.zav.jra.AbstractClient;
import proj.zav.jra.models.*;
import proj.zav.jra.models._json.JSONAbstractAccount;
import proj.zav.jra.models._json.JSONFakeAccount;
import proj.zav.jra.models._json.JSONListing;
import proj.zav.jra.models._json.JSONTrophyList;
import proj.zav.jra.query.QueryDelete;
import proj.zav.jra.query.QueryGet;
import proj.zav.jra.query.QueryPost;
import proj.zav.jra.query.QueryPut;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Users {
    /**
     * For blocking an user account.
     * @param client The client performing the request.
     * @return The jQuery response.
     * @see Endpoint#POST_BLOCK_USER;
     */
    public static QueryPost<String> postBlockUser(AbstractClient client) {
        return new QueryPost<>(Function.identity(), client, Endpoint.POST_BLOCK_USER);
    }
    /**
     * Create a relationship between a user and another user or subreddit.
     * @param client The client performing the request.
     * @return The jQuery response.
     * @see Endpoint#POST_FRIEND
     */
    public static QueryPost<String> postFriend(AbstractClient client) {
        return new QueryPost<>(Function.identity(), client, Endpoint.POST_FRIEND);
    }
    /**
     * Create a relationship between a user and another user or subreddit.
     * @param client The client performing the request.
     * @param subreddit The subreddit that is modified.
     * @return The jQuery response.
     * @see Endpoint#POST_SUBREDDIT_FRIEND
     */
    public static QueryPost<String> postFriend(AbstractClient client, String subreddit) {
        return new QueryPost<>(Function.identity(), client, Endpoint.POST_SUBREDDIT_FRIEND, subreddit);
    }

    /**
     * Report a user. Reporting a user brings it to the attention of a Reddit admin.
     * @param client The client performing the request.
     * @return The jQuery response.
     * @see Endpoint#POST_REPORT_USER
     */
    public static QueryPost<String> postReportUser(AbstractClient client) {
        return new QueryPost<>(Function.identity(), client, Endpoint.POST_REPORT_USER);
    }

    /**
     * TODO Doc
     * @param client The client performing the request.
     * @return The jQuery response.
     * @see Endpoint#POST_SETPERMISSION
     */
    public static QueryPost<String> postSetPermission(AbstractClient client) {
        return new QueryPost<>(Function.identity(), client, Endpoint.POST_SETPERMISSION);
    }

    /**
     * TODO Doc
     * @param client The client performing the request.
     * @param subreddit The subreddit that is modified.
     * @return The jQuery response.
     * @see Endpoint#POST_SUBREDDIT_SETPERMISSION
     */
    public static QueryPost<String> postSetPermission(AbstractClient client, String subreddit) {
        return new QueryPost<>(Function.identity(), client, Endpoint.POST_SUBREDDIT_SETPERMISSION, subreddit);
    }

    /**
     * Remove a relationship between a user and another user or subreddit.
     * @param client The client performing the request.
     * @return The jQuery response.
     * @see Endpoint#POST_UNFRIEND
     */
    public static QueryPost<String> postUnfriend(AbstractClient client) {
        return new QueryPost<>(Function.identity(), client, Endpoint.POST_UNFRIEND);
    }

    /**
     * Remove a relationship between a user and another user or subreddit.
     * @param client The client performing the request.
     * @param subreddit The subreddit whose relationship is modified.
     * @return The jQuery response.
     * @see Endpoint#POST_SUBREDDIT_UNFRIEND
     */
    public static QueryPost<String> postUnfriend(AbstractClient client, String subreddit) {
        return new QueryPost<>(Function.identity(), client, Endpoint.POST_SUBREDDIT_UNFRIEND, subreddit);
    }

    /**
     * TODO Doc
     * @param client The client performing the request.
     * @return A mapping associating each user with their user data.
     * @see Endpoint#GET_USER_DATA_BY_ACCOUNT_IDS
     */
    public static QueryGet<Map<String, FakeAccount>> getUserDataByAccountIds(AbstractClient client) {
        Function<String, Map<String, FakeAccount>> mapper = source -> {
            JSONObject root = new JSONObject(source);
            Map<String, FakeAccount> result = new HashMap<>();

            for(String key : root.keySet())
                result.put(key, JSONFakeAccount.fromJson(new FakeAccount(), root.getJSONObject(key)));

            return result;
        };

        return new QueryGet<>(
                mapper,
                client,
                Endpoint.GET_USER_DATA_BY_ACCOUNT_IDS
        );
    }

    /**
     * Check whether a username is available for registration. The name has to be specified as a parameter in the
     * {@link QueryGet Query}.
     * @param client The client performing the request.
     * @return {@code true}, if the username is available. Otherwise false.
     * @see Endpoint#GET_USERNAME_AVAILABLE
     */
    public static QueryGet<Boolean> getUsernameAvailable(AbstractClient client) {
        return new QueryGet<>(
                Boolean::parseBoolean,
                client,
                Endpoint.GET_USERNAME_AVAILABLE
        );
    }

    /**
     * Stop being friends with a user.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @return The jQuery response.
     * @see Endpoint#DELETE_ME_FRIENDS_USERNAME
     */
    public static QueryDelete<String> deleteMeFriends(AbstractClient client, String username) {
        return new QueryDelete<>(Function.identity(), client, Endpoint.DELETE_ME_FRIENDS_USERNAME, username);
    }

    /**
     * Get information about a specific 'friend', such as notes.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @return The jQuery response.
     * @see Endpoint#GET_ME_FRIENDS_USERNAME
     */
    public static QueryGet<String> getMeFriends(AbstractClient client, String username) {
        return new QueryGet<>(
                Function.identity(),
                client,
                Endpoint.GET_ME_FRIENDS_USERNAME,
                username
        );
    }

    /**
     * Create or update a "friend" relationship.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @return The jQuery response.
     * @see Endpoint#PUT_ME_FRIENDS_USERNAME
     */
    public static QueryPut<String> putMeFriends(AbstractClient client, String username) {
        return new QueryPut<>(
                Function.identity(),
                client,
                Endpoint.PUT_ME_FRIENDS_USERNAME,
                username
        );
    }

    /**
     * Return a list of trophies for the a given user.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @return A {@link List} of trophies.
     * @see Endpoint#GET_USER_USERNAME_TROHPIES
     */
    @Nonnull
    public static QueryGet<List<Trophy>> getTrophies(AbstractClient client, String username) {
        return new QueryGet<>(
                source -> JSONTrophyList.fromThing(source).getData(),
                client,
                Endpoint.GET_USER_USERNAME_TROHPIES,
                username
        );
    }

    /**
     * Return information about the user, including karma and gold status.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @param supplier A supplier for a fresh account instance.
     * @param <T> The target type.
     * @return An account instance for the specified user.
     * @see Endpoint#GET_USER_USERNAME_ABOUT
     */
    @Nonnull
    public static <T extends AbstractAccount> QueryGet<T> getAccount(AbstractClient client, Supplier<T> supplier, String username){
        return new QueryGet<>(
                source -> JSONAbstractAccount.fromThing(source, supplier),
                client,
                Endpoint.GET_USER_USERNAME_ABOUT,
                username
        );
    }

    /**
     * Retrieves all comments submitted by the specified user.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param <T> The target type.
     * @return A {@link Listing} of comments.
     * @see Endpoint#GET_USER_USERNAME_COMMENTS
     */
    @Nonnull
    public static <T extends AbstractComment> QueryGet<Listing<T>> getComments(AbstractClient client, Function<Thing, T> mapper, String username) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_USER_USERNAME_COMMENTS,
                username
        );
    }

    /**
     * Retrieves all links/comments downvoted by the specified user.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @return A {@link Listing} of Things.
     * @see Endpoint#GET_USER_USERNAME_DOWNVOTED
     */
    @Nonnull
    public static QueryGet<Listing<Thing>> getDownvoted(AbstractClient client, String username) {
        return new QueryGet<>(
                JSONListing::fromThing,
                client,
                Endpoint.GET_USER_USERNAME_DOWNVOTED,
                username
        );
    }

    /**
     * Retrieves all links/comments gilded by the specified user.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @return A {@link Listing} of Things.
     * @see Endpoint#GET_USER_USERNAME_GILDED
     */
    @Nonnull
    public static QueryGet<Listing<Thing>> getGilded(AbstractClient client, String username) {
        return new QueryGet<>(
                JSONListing::fromThing,
                client,
                Endpoint.GET_USER_USERNAME_GILDED,
                username
        );
    }

    /**
     * Retrieves all links/comments hidden by the specified user.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @return A {@link Listing} of Things.
     * @see Endpoint#GET_USER_USERNAME_HIDDEN
     */
    @Nonnull
    public static QueryGet<Listing<Thing>> getHidden(AbstractClient client, String username) {
        return new QueryGet<>(
                JSONListing::fromThing,
                client,
                Endpoint.GET_USER_USERNAME_HIDDEN,
                username
        );
    }


    /**
     * TODO Doc
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @return A {@link Listing} of Things.
     * @see Endpoint#GET_USER_USERNAME_OVERVIEW
     */
    @Nonnull
    public static QueryGet<Listing<Thing>> getOverview(AbstractClient client, String username) {
        return new QueryGet<>(
                JSONListing::fromThing,
                client,
                Endpoint.GET_USER_USERNAME_OVERVIEW,
                username
        );
    }

    /**
     * Retrieves all links/comments saved by the specified user.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @return A {@link Listing} of Things.
     * @see Endpoint#GET_USER_USERNAME_SAVED
     */
    @Nonnull
    public static QueryGet<Listing<Thing>> getSaved(AbstractClient client, String username) {
        return new QueryGet<>(
                JSONListing::fromThing,
                client,
                Endpoint.GET_USER_USERNAME_SAVED,
                username
        );
    }

    /**
     * Retrieves all links submitted by the specified user.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @param mapper A mapper function processing the Things contained by the {@link Listing}.
     * @param <T> The target type.
     * @return A {@link Listing} of links.
     * @see Endpoint#GET_USER_USERNAME_SUBMITTED
     */
    @Nonnull
    public static <T extends AbstractLink> QueryGet<Listing<T>> getSubmitted(AbstractClient client, Function<Thing, T> mapper, String username) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_USER_USERNAME_SUBMITTED,
                username
        );
    }
    /**
     * Retrieves all links/comments upvoted by the specified user.
     * @param client The client performing the request.
     * @param username The name of the specified user.
     * @return A {@link Listing} of Things.
     * @see Endpoint#GET_USER_USERNAME_UPVOTED
     */
    @Nonnull
    public static QueryGet<Listing<Thing>> getUpvoted(AbstractClient client, String username) {
        return new QueryGet<>(
                JSONListing::fromThing,
                client,
                Endpoint.GET_USER_USERNAME_UPVOTED,
                username
        );
    }
}
