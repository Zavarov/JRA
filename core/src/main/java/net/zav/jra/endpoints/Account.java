package net.zav.jra.endpoints;

import net.zav.jra.AbstractClient;
import net.zav.jra.http.APIRequest;
import net.zav.jra.models.*;
import net.zav.jra.models._json.*;
import net.zav.jra.query.QueryGet;
import net.zav.jra.query.QueryPatch;
import org.json.JSONArray;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Nonnull
public abstract class Account {
    /**
     * Returns the {@link AbstractSelfAccount SelfAccount} of the client.
     * @param supplier Provides the account instance.
     * @param client The client instance performing the API request.
     * @return An instance of the {@link AbstractSelfAccount SelfAccount} of the currently logged-in user.
     * @see Endpoint#GET_ME
     */
    @Nonnull
    public static  <T extends AbstractSelfAccount> QueryGet<T> getMe(@Nonnull AbstractClient client, @Nonnull Supplier<T> supplier) {
        return new QueryGet<>(
                //TODO Inline
                source -> {
                    T target = supplier.get();
                    JSONAbstractSelfAccount.fromJson(target, source);
                    return target;
                },
                client,
                Endpoint.GET_ME
        );
    }

    /**
     * Retrieves all currently blocked users.
     * @param client The client instance performing the API request.
     * @return A list of users.
     * @deprecated Deprecated in favor of {@link #getPreferencesBlocked(AbstractClient)}
     * @see #getPreferencesBlocked(AbstractClient)
     * @see Endpoint#GET_ME_BLOCKED
     */
    @Nonnull
    @Deprecated
    public static QueryGet<List<FakeAccount>> getBlocked(@Nonnull AbstractClient client) {
        return new QueryGet<>(
                source -> JSONUserList.fromThing(source).getData(),
                client,
                Endpoint.GET_ME_BLOCKED
        );
    }

    /**
     * Retrieves all friends.
     * @param client The client instance performing the API request.
     * @return A list of users.
     * @deprecated Deprecated in favor of {@link #getPreferencesFriends(AbstractClient)}.
     * @see #getPreferencesFriends(AbstractClient)
     * @see Endpoint#GET_ME_FRIENDS
     */
    @Nonnull
    @Deprecated
    public static QueryGet<List<FakeAccount>> getFriends(@Nonnull AbstractClient client) {
        return new QueryGet<>(
                source -> JSONUserList.fromThing(source).getData(),
                client,
                Endpoint.GET_ME_FRIENDS
        );
    }


    /**
     * Returns a breakdown of the {@link Karma} received so far.<p>
     * Each entry contains the number of {@link AbstractLink Link} and {@link AbstractComment Comment} in one of the
     * subreddits the user has been active at some point.
     * @param client The client instance performing the API request.
     * @return A list of {@link Karma} instances.
     * @see Endpoint#GET_ME_KARMA
     */
    @Nonnull
    public static QueryGet<List<Karma>> getKarma(@Nonnull AbstractClient client) {
        return new QueryGet<>(
                source -> JSONKarmaList.fromThing(source).getData(),
                client,
                Endpoint.GET_ME_KARMA
        );
    }

    /**
     * Returns the preference of this {@link Account}.<p>
     * Those settings contain information such as the default {@link AbstractComment Comment} sort, whether nightmode is
     * enabled or whether they should be notified via email upon mentions or responses.
     * @param client The client instance performing the API request.
     * @return An instance of the user preferences.
     * @see Endpoint#GET_ME_PREFS
     */
    @Nonnull
    public static <T extends AbstractPreferences> QueryGet<T> getPreferences(@Nonnull AbstractClient client, Supplier<T> supplier) {
        return new QueryGet<>(
                source -> JSONAbstractPreferences.fromJson(source, supplier),
                client,
                Endpoint.GET_ME_PREFS
        );
    }

    /**
     * Updates the preferences of this {@link Account}.
     * @param client The client instance performing the API request.
     * @return An instance of the updated user preferences.
     * @see Endpoint#GET_ME_PREFS
     */
    @Nonnull
    public static <T extends AbstractPreferences> QueryPatch<T> patchPreferences(@Nonnull AbstractClient client, Supplier<T> supplier) {
        return new QueryPatch<>(
                source -> JSONAbstractPreferences.fromJson(source, supplier),
                client,
                Endpoint.GET_ME_PREFS,
                APIRequest.BodyType.JSON
        );
    }

    /**
     * Returns a list of all trophies of this {@link Account}.
     * @param client The client instance performing the API request.
     * @return A list of trophies.
     * @see Endpoint#GET_ME_TROPHIES
     */
    @Nonnull
    public static QueryGet<List<Trophy>> getTrophies(@Nonnull AbstractClient client) {
        return new QueryGet<>(
                source -> JSONTrophyList.fromThing(source).getData(),
                client,
                Endpoint.GET_ME_TROPHIES
        );
    }

    /**
     * Retrieves all currently blocked users.
     * @param client The client instance performing the API request.
     * @return A list of users.
     * @see Endpoint#GET_PREFS_BLOCKED
     */
    @Nonnull
    public static QueryGet<List<FakeAccount>> getPreferencesBlocked(@Nonnull AbstractClient client) {
        return new QueryGet<>(
                source -> JSONUserList.fromThing(source).getData(),
                client,
                Endpoint.GET_PREFS_BLOCKED
        );
    }

    /**
     * Retrieves all friends.
     * @param client The client instance performing the API request.
     * @return A list of users.
     * @see Endpoint#GET_PREFS_FRIENDS
     */
    @Nonnull
    public static QueryGet<List<FakeAccount>> getPreferencesFriends(@Nonnull AbstractClient client) {
        //TODO Find a better way to extract friends
        Function<String, List<FakeAccount>> mapper = source -> {
            JSONArray response = new JSONArray(source);

            //I think that's a relic from when /prefs/friends/ used to return both friends and blocked users
            //I.e. The first entry contains all friends
            //And the second entry should always be empty.
            assert response.length() == 2;

            UserList friends = JSONUserList.fromThing(response.getJSONObject(0));
            UserList blocked = JSONUserList.fromThing(response.getJSONObject(1));

            assert blocked.isEmptyData();

            return friends.getData();
        };

        return new QueryGet<>(
                mapper,
                client,
                Endpoint.GET_PREFS_FRIENDS
        );
    }

    /**
     * Returns the message settings of this {@link Account}.<p>
     * The message settings consists of a list containing all currently blocked and trusted users.
     * @param client The client instance performing the API request.
     * @return An instance of the message settings.
     * @see Endpoint#GET_PREFS_MESSAGING
     */
    @Nonnull
    public static QueryGet<Messaging> getPreferencesMessaging(@Nonnull AbstractClient client) {
        return new QueryGet<>(
                JSONMessaging::fromJson,
                client,
                Endpoint.GET_PREFS_MESSAGING
        );
    }

    /**
     * Returns the whitelist of all users that are able to send messages to the currently logged-in user. Even if
     * private messages have been disabled.
     * @param client The client instance performing the API request.
     * @return A list of users.
     * @see Endpoint#GET_PREFS_TRUSTED
     */
    @Nonnull
    public static QueryGet<List<FakeAccount>> getPreferencesTrusted(@Nonnull AbstractClient client) {
        return new QueryGet<>(
                source -> JSONUserList.fromThing(source).getData(),
                client,
                Endpoint.GET_PREFS_TRUSTED
        );
    }
}
