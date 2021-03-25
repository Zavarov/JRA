package net.zav.jra.endpoints;

import com.google.common.base.Joiner;
import net.zav.jra.AbstractClient;
import net.zav.jra.models.*;
import net.zav.jra.models._json.*;
import net.zav.jra.query.QueryGet;
import net.zav.jra.query.QueryPost;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Subreddits {
    public static QueryGet<Listing<FakeAccount>> getBanned(AbstractClient client) {
        return new QueryGet<>(
                source -> JSONListing.fromJson(source, JSONFakeAccount::fromJson),
                        client,
                        Endpoint.GET_ABOUT_BANNED
                );

    }

    public static QueryGet<Listing<FakeAccount>> getBanned(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                source -> JSONListing.fromJson(source, JSONFakeAccount::fromJson),
                        client,
                        Endpoint.GET_SUBREDDIT_ABOUT_BANNED,
                        subreddit
                );

    }

    public static QueryGet<Listing<FakeAccount>> getContributors(AbstractClient client) {
        return new QueryGet<>(
                source -> JSONListing.fromJson(source, JSONFakeAccount::fromJson),
                client,
                Endpoint.GET_ABOUT_CONTRIBUTORS
        );
    }

    public static QueryGet<Listing<FakeAccount>> getContributors(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                source -> JSONListing.fromJson(source, JSONFakeAccount::fromJson),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_CONTRIBUTORS,
                subreddit
        );
    }

    public static QueryGet<List<FakeAccount>> getModerators(AbstractClient client) {
        return new QueryGet<>(
                source -> JSONUserList.fromThing(source).getData(),
                client,
                Endpoint.GET_ABOUT_MODERATORS
        );
    }

    public static QueryGet<List<FakeAccount>> getModerators(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                source -> JSONUserList.fromThing(source).getData(),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_MODERATORS,
                subreddit
        );
    }

    public static QueryGet<Listing<FakeAccount>> getMuted(AbstractClient client) {
        return new QueryGet<>(
                source -> JSONListing.fromJson(source, JSONFakeAccount::fromJson),
                client,
                Endpoint.GET_ABOUT_MUTED
        );
    }

    public static QueryGet<Listing<FakeAccount>> getMuted(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                source -> JSONListing.fromJson(source, JSONFakeAccount::fromJson),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_MUTED,
                subreddit
        );
    }

    public static QueryGet<Listing<FakeAccount>> getWikibanned(AbstractClient client) {
        return new QueryGet<>(
                source -> JSONListing.fromJson(source, JSONFakeAccount::fromJson),
                client,
                Endpoint.GET_ABOUT_WIKIBANNED
        );
    }

    public static QueryGet<Listing<FakeAccount>> getWikibanned(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                source -> JSONListing.fromJson(source, JSONFakeAccount::fromJson),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_WIKIBANNED,
                subreddit
        );
    }

    public static QueryGet<Listing<FakeAccount>> getWikicontributors(AbstractClient client) {
        return new QueryGet<>(
                source -> JSONListing.fromJson(source, JSONFakeAccount::fromJson),
                client,
                Endpoint.GET_ABOUT_WIKICONTRIBUTORS
        );
    }

    public static QueryGet<Listing<FakeAccount>> getWikicontributors(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                source -> JSONListing.fromJson(source, JSONFakeAccount::fromJson),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_WIKICONTRIBUTORS,
                subreddit
        );
    }

    public static QueryPost<String> postDeleteSubredditBanner(AbstractClient client) {
        QueryPost<String> query = new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_DELETE_SUBREDDIT_BANNER
        );

        query.setParameter("api_type","json");

        return query;
    }

    public static QueryPost<String> postDeleteSubredditBanner(AbstractClient client, String subreddit) {
        QueryPost<String> query = new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_DELETE_SUBREDDIT_BANNER,
                subreddit
        );

        query.setParameter("api_type","json");

        return query;
    }

    public static QueryPost<String> postDeleteSubredditHeader(AbstractClient client) {
        QueryPost<String> query = new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_DELETE_SUBREDDIT_HEADER
        );

        query.setParameter("api_type","json");

        return query;
    }

    public static QueryPost<String> postDeleteSubredditHeader(AbstractClient client, String subreddit) {
        QueryPost<String> query = new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_DELETE_SUBREDDIT_HEADER,
                subreddit
        );

        query.setParameter("api_type","json");

        return query;
    }

    public static QueryPost<String> postDeleteSubredditIcon(AbstractClient client) {
        QueryPost<String> query = new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_DELETE_SUBREDDIT_ICON
        );

        query.setParameter("api_type","json");

        return query;
    }

    public static QueryPost<String> postDeleteSubredditIcon(AbstractClient client, String subreddit) {
        QueryPost<String> query = new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_DELETE_SUBREDDIT_ICON,
                subreddit
        );

        query.setParameter("api_type","json");

        return query;
    }

    public static QueryPost<String> postDeleteSubredditImage(AbstractClient client) {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_DELETE_SUBREDDIT_IMAGE
        );
    }

    public static QueryPost<String> postDeleteSubredditImage(AbstractClient client, String subreddit) {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_DELETE_SUBREDDIT_IMAGE,
                subreddit
        );
    }

    @Deprecated
    public static QueryGet<String> getRecommendSubreddits(AbstractClient client, String... subreddits) {
        return new QueryGet<>(
                Function.identity(),
                client,
                Endpoint.GET_RECOMMEND_SUBREDDITS,
                Joiner.on(',').join(subreddits)
        );
    }

    public static QueryGet<List<String>> getSearchRedditNames(AbstractClient client) {
        Function<String, List<String>> mapper = source -> {
            JSONObject node = new JSONObject(source);
            JSONArray data = node.getJSONArray("names");
            List<String> names = new ArrayList<>(data.length());

            for(int i = 0 ; i < data.length() ; ++i)
                names.add(data.getString(i));

            return names;
        };

        return new QueryGet<>(
                mapper,
                client,
                Endpoint.GET_SEARCH_REDDIT_NAMES
        );
    }

    public static QueryPost<List<String>> postSearchRedditNames(AbstractClient client) {
        Function<String, List<String>> mapper = source -> {
            JSONObject node = new JSONObject(source);
            JSONArray data = node.getJSONArray("names");
            List<String> names = new ArrayList<>(data.length());

            for(int i = 0 ; i < data.length() ; ++i)
                names.add(data.getString(i));

            return names;
        };

        return new QueryPost<>(
                mapper,
                client,
                Endpoint.POST_SEARCH_REDDIT_NAMES
        );
    }

    public static QueryPost<String> postSiteAdmin(AbstractClient client) {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SITE_ADMIN
        );
    }

    public static QueryPost<List<FakeSubreddit>> postSearchSubreddits(AbstractClient client) {
        Function<String, List<FakeSubreddit>> mapper = source -> {
            JSONObject node = new JSONObject(source);

            JSONArray data = node.getJSONArray("subreddits");
            List<FakeSubreddit> subreddits = new ArrayList<>(data.length());

            for(int i = 0 ; i < data.length() ; ++i)
                subreddits.add(JSONFakeSubreddit.fromJson(new FakeSubreddit(), data.getJSONObject(i)));

            return subreddits;
        };

        return new QueryPost<>(
                mapper,
                client,
                Endpoint.POST_SEARCH_SUBREDDITS
        );
    }

    public static QueryGet<Map<String, Object>> getSubmitText(AbstractClient client) {
        return new QueryGet<>(
                source -> new JSONObject(source).toMap(),
                client,
                Endpoint.GET_SUBMIT_TEXT
        );
    }

    public static QueryGet<Map<String, Object>> getSubmitText(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                source -> new JSONObject(source).toMap(),
                client,
                Endpoint.GET_SUBREDDIT_SUBMIT_TEXT,
                subreddit
        );
    }

    public static QueryGet<List<FakeSubreddit>> getSubredditAutocomplete(AbstractClient client) {
        Function<String, List<FakeSubreddit>> mapper = source -> {
            JSONObject node = new JSONObject(source);
            JSONArray data = node.getJSONArray("subreddits");
            List<FakeSubreddit> subreddits = new ArrayList<>(data.length());

            for(int i = 0 ; i < data.length() ; ++i) {
                subreddits.add(JSONFakeSubreddit.fromJson(new FakeSubreddit(), data.getJSONObject(i)));
            }

            return subreddits;
        };

        return new QueryGet<>(
                mapper,
                client,
                Endpoint.GET_SUBREDDIT_AUTOCOMPLETE
        );
    }

    public static QueryGet<Listing<Thing>> getSubredditAutocompleteV2(AbstractClient client) {
        return new QueryGet<>(
                JSONListing::fromThing,
                client,
                Endpoint.GET_SUBREDDIT_AUTOCOMPLETE_V2
        );
    }

    public static QueryPost<String> postStylesheet(AbstractClient client) {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_STYLESHEET
        );
    }

    public static QueryPost<String> postStylesheet(AbstractClient client, String subreddit) {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_SUBREDDIT_STYLESHEET,
                subreddit
        );
    }

    public static QueryPost<String> postSubscribe(AbstractClient client) {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBSCRIBE
        );
    }

    public static QueryPost<String> postUploadImage(AbstractClient client) {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_UPLOAD_SUBREDDIT_IMAGE
        );
    }

    public static QueryPost<String> postUploadImage(AbstractClient client, String subreddit) {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_UPLOAD_SUBREDDIT_IMAGE,
                subreddit
        );
    }

    public static QueryGet<Map<String, Object>> getPostRequirements(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                source -> new JSONObject(source).toMap(),
                client,
                Endpoint.GET_SUBREDDIT_POST_REQUIREMENTS,
                subreddit
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<T> getSubreddit(AbstractClient client, Supplier<T> supplier, String name){
        return new QueryGet<>(
                source -> JSONAbstractSubreddit.fromThing(source, supplier),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT,
                name
        );
    }

    public static QueryGet<SubredditSettings> getSubredditEdit(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                JSONSubredditSettings::fromThing,
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_EDIT,
                subreddit
        );
    }

    public static QueryGet<Rules> getSubredditRules(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                source -> JSONRules.fromJson(new Rules(), source),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_RULES,
                subreddit
        );
    }

    public static QueryGet<Map<String, Object>> getTraffic(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                source -> new JSONObject(source).toMap(),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_TRAFFIC,
                subreddit
        );
    }

    @Deprecated()
    public static QueryGet<String> getSidebar(AbstractClient client) {
        return new QueryGet<>(
                Function.identity(),
                client,
                Endpoint.GET_SIDEBAR
        );
    }

    @Deprecated()
    public static QueryGet<String> getSidebar(AbstractClient client, String subreddit) {
        return new QueryGet<>(
                Function.identity(),
                client,
                Endpoint.GET_SUBREDDIT_SIDEBAR,
                subreddit
        );
    }

    public static <T extends AbstractLink> QueryGet<Submission> getSticky(AbstractClient client, Function<Thing, T> mapper){
        return new QueryGet<>(
                source -> JSONSubmission.from(source, mapper),
                client,
                Endpoint.GET_ABOUT_STICKY
        );
    }

    public static <T extends AbstractLink> QueryGet<Submission> getSticky(AbstractClient client, Function<Thing, T> mapper, String subreddit){
        return new QueryGet<>(
                source -> JSONSubmission.from(source, mapper),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_STICKY,
                subreddit
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getSubredditsDefault(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDITS_DEFAULT
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getSubredditsGold(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDITS_GOLD
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getSubredditsNew(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDITS_NEW
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getSubredditsPopular(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDITS_POPULAR
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getMineContributor(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDITS_MINE_CONTRIBUTOR
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getMineModerator(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDITS_MINE_MODERATOR
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getMineStreams(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDITS_MINE_STREAMS
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getMineSubscriber(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDITS_MINE_SUBSCRIBER
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getSubredditsSearch(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_SUBREDDITS_SEARCH
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getUsersNew(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_USERS_NEW
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getUsersPopular(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_USERS_POPULAR
        );
    }

    public static <T extends AbstractSubreddit> QueryGet<Listing<T>> getUsersSearch(AbstractClient client, Function<Thing, T> mapper) {
        return new QueryGet<>(
                source -> JSONListing.fromThing(source, mapper),
                client,
                Endpoint.GET_USERS_SEARCH
        );
    }
}
