package vartas.jra;

import org.json.JSONArray;
import vartas.jra.http.APIRequest;
import vartas.jra.models.*;
import vartas.jra.models._factory.MessagingFactory;
import vartas.jra.models._json.JSONPreferences;
import vartas.jra.query.QueryOne;
import vartas.jra.query.QueryPatch;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

public class SelfAccount extends SelfAccountTOP{
    private final Client client;

    public SelfAccount(Client client){
        this.client = client;
    }

    @Override
    public SelfAccount getRealThis() {
        return this;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Account                                                                                                     //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Retrieves all currently blocked users.
     * @return A list of users.
     * @deprecated Deprecated in favor of {@link #getPreferencesBlocked()}
     * @see #getPreferencesBlocked()
     * @see Endpoint#GET_ME_BLOCKED
     */
    @Override
    @Nonnull
    @Deprecated
    public QueryOne<UserList> getBlocked() {
        return new QueryOne<>(
                source -> Thing.from(source).toUserList(client),
                client,
                Endpoint.GET_ME_BLOCKED
        );
    }

    /**
     * Retrieves all friends.
     * @return A list of users.
     * @deprecated Deprecated in favor of {@link #getPreferencesFriends()}.
     * @see #getPreferencesFriends()
     * @see Endpoint#GET_ME_FRIENDS
     */
    @Override
    @Nonnull
    @Deprecated
    public QueryOne<UserList> getFriends() {
        return new QueryOne<>(
                source -> Thing.from(source).toUserList(client),
                client,
                Endpoint.GET_ME_FRIENDS
        );
    }


    /**
     * Returns a breakdown of the {@link Karma} received so far.<p>
     * Each entry contains the number of {@link Link} and {@link Comment} in one of the subreddits the user has been
     * active at some point.
     * @return A list of {@link Karma} instances.
     * @see Endpoint#GET_ME_KARMA
     */
    @Override
    @Nonnull
    public QueryOne<KarmaList> getKarma() {
        return new QueryOne<>(
                source -> Thing.from(source).toKarmaList(),
                client,
                Endpoint.GET_ME_KARMA
        );
    }

    /**
     * Returns the preference of this {@link Account}.<p>
     * Those settings contain information such as the default {@link Comment} sort, whether nightmode is enabled or
     * whether they should be notified via email upon mentions or responses.
     * @return An instance of the user preferences.
     * @see Endpoint#GET_ME_PREFS
     */
    @Override
    @Nonnull
    public QueryOne<Preferences> getPreferences() {
        return new QueryOne<>(
                source -> JSONPreferences.fromJson(new Preferences(), source),
                client,
                Endpoint.GET_ME_PREFS
        );
    }

    /**
     * Updates the preferences of this {@link Account}.
     * @return An instance of the updated user preferences.
     * @see Endpoint#GET_ME_PREFS
     */
    @Override
    @Nonnull
    public QueryPatch<Preferences> patchPreferences() {
        return new QueryPatch<>(
                source -> JSONPreferences.fromJson(new Preferences(), source),
                client,
                Endpoint.GET_ME_PREFS,
                APIRequest.BodyType.JSON
        );
    }

    /**
     * Returns a list of all trophies of this {@link Account}.
     * @return A list of trophies.
     * @see Endpoint#GET_ME_TROPHIES
     */
    @Override
    @Nonnull
    public QueryOne<TrophyList> getTrophies() {
        return new QueryOne<>(
                source -> Thing.from(source).toTrophyList(),
                client,
                Endpoint.GET_ME_TROPHIES
        );
    }

    /**
     * Retrieves all currently blocked users.
     * @return A list of users.
     * @see Endpoint#GET_PREFS_BLOCKED
     */
    @Override
    @Nonnull
    public QueryOne<UserList> getPreferencesBlocked() {
        return new QueryOne<>(
                source -> Thing.from(source).toUserList(client),
                client,
                Endpoint.GET_PREFS_BLOCKED
        );
    }

    /**
     * Retrieves all friends.
     * @return A list of users.
     * @see Endpoint#GET_PREFS_FRIENDS
     */
    @Override
    @Nonnull
    public QueryOne<UserList> getPreferencesFriends() {
        //TODO Find a better way to extract friends
        Function<String, UserList> mapper = source -> {
            JSONArray response = new JSONArray(source);

            //I think that's a relic from when /prefs/friends/ used to return both friends and blocked users
            //I.e. The first entry contains all friends
            //And the second entry should always be empty.
            assert response.length() == 2;

            UserList friends = Thing.from(response.getJSONObject(0)).toUserList(client);
            UserList blocked = Thing.from(response.getJSONObject(1)).toUserList(client);

            assert blocked.isEmptyData();

            return friends;
        };

        return new QueryOne<>(
                mapper,
                client,
                Endpoint.GET_PREFS_FRIENDS
        );
    }

    /**
     * Returns the message settings of this {@link Account}.<p>
     * The message settings consists of a list containing all currently blocked and trusted users.
     * @return An instance of the message settings.
     * @see Endpoint#GET_PREFS_MESSAGING
     */
    @Override
    @Nonnull
    public QueryOne<Messaging> getPreferencesMessaging() {
        //TODO Use JSONMessaging instead
        Function<String, Messaging> mapper = source -> {
            JSONArray response = new JSONArray(source);

            assert response.length() == 2;

            List<FakeAccount> blocked = Thing.from(response.getJSONObject(0)).toUserList(client).getData();
            List<FakeAccount> trusted = Thing.from(response.getJSONObject(1)).toUserList(client).getData();

            return MessagingFactory.create(blocked, trusted);
        };

        return new QueryOne<>(
                mapper,
                client,
                Endpoint.GET_PREFS_MESSAGING
        );
    }

    /**
     * Returns the whitelist of all users that are able to send messages to the currently logged-in user. Even if
     * private messages have been disabled.
     * @return A list of users.
     * @see Endpoint#GET_PREFS_TRUSTED
     */
    @Override
    @Nonnull
    public QueryOne<UserList> getPreferencesTrusted() {
        return new QueryOne<>(
                source -> Thing.from(source).toUserList(client),
                client,
                Endpoint.GET_PREFS_TRUSTED
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Subreddits                                                                                                  //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public QueryOne<String> getMineContributor() {
        return new QueryOne<>(
                Function.identity(),
                client,
                Endpoint.GET_SUBREDDITS_MINE_CONTRIBUTOR
        );
    }

    @Override
    public QueryOne<String> getMineModerator() {
        return new QueryOne<>(
                Function.identity(),
                client,
                Endpoint.GET_SUBREDDITS_MINE_MODERATOR
        );
    }

    @Override
    public QueryOne<String> getMineStreams() {
        return new QueryOne<>(
                Function.identity(),
                client,
                Endpoint.GET_SUBREDDITS_MINE_STREAMS
        );
    }

    @Override
    public QueryOne<String> getMineSubscriber() {
        return new QueryOne<>(
                Function.identity(),
                client,
                Endpoint.GET_SUBREDDITS_MINE_SUBSCRIBER
        );
    }
}
