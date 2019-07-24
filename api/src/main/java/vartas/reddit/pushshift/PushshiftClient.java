/*
 * Copyright (c) 2019 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package vartas.reddit.pushshift;

import com.google.common.util.concurrent.RateLimiter;
import org.json.JSONArray;
import org.json.JSONObject;
import vartas.reddit.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class implements the Reddit client using both the JRAW library and the Pushshift API to circumvent the 1000
 * submission limit imposed by Reddit.
 */
public class PushshiftClient implements ClientInterface {
    /**
     * We only allow a single request per second for pushshift.
     */
    protected final RateLimiter rateLimiter = RateLimiter.create(1);
    /**
     * A scaling factor to convert the time in milliseconds to the time in seconds.
     */
    protected final static int MILLISECONDS_TO_SECONDS = 1000;
    /**
     * The underlying client.
     */
    protected ClientInterface client;

    /**
     * Initializes the client but doesn't establish a connection to Reddit.
     * @param client the underlying client responsible for the communication with the Reddit API.
     */
    public PushshiftClient(ClientInterface client) {
        this.client = client;
    }

    /**
     * @param name the name of the user.
     * @return the Reddit user instance with that name.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    @Override
    public Optional<UserInterface> requestUser(String name) throws UnresolvableRequestException {
        return client.requestUser(name);
    }

    /**
     * @param name the name of the subreddit.
     * @return the subreddit with that name.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    @Override
    public Optional<SubredditInterface> requestSubreddit(String name) throws UnresolvableRequestException {
        return client.requestSubreddit(name);
    }

    /**
     * @param submission the id of the submission.
     * @return all comments of the submission.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    @Override
    public Optional<List<CommentInterface>> requestComment(String submission) throws UnresolvableRequestException {
        return client.requestComment(submission);
    }

    /**
     * Requests a single submission.
     * @param submissionId the id of the submission.
     * @return the submission instance.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    @Override
    public Optional<SubmissionInterface> requestSubmission(String submissionId) throws UnresolvableRequestException{
        return client.requestSubmission(submissionId);
    }

    /**
     * Request submissions within a given interval sorted by their creation date.
     * In order to circumvent the 1000 submissions restriction, they are first requested via the Pushshift API in order
     * to get their ids, then those ids are used for the communication with the Reddit API.
     * @param subreddit the name of the subreddit.
     * @param after the (exclusive) minimum age of the submissions.
     * @param before the (exclusive) maximum age of the submissions.
     * @return all submissions within the given interval sorted by their creation time.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    @Override
    public Optional<TreeSet<SubmissionInterface>> requestSubmission(String subreddit, Date after, Date before) throws UnresolvableRequestException{
        String jsonContent;
        //Break if the JSON request failed
        try {
            jsonContent = requestJsonContent(subreddit, after, before);
        }catch(IOException e) {
            return Optional.empty();
        }
        TreeSet<SubmissionInterface> submissions = extractSubmissions(jsonContent)
                .stream()
                .map(client::requestSubmission)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(TreeSet::new));

        return Optional.of(submissions);
    }

    /**
     * Extracts the submissions from the JSON string.
     * @param jsonContent the JSON string.
     * @return a list containing all submissions.
     */
    protected List<String> extractSubmissions(String jsonContent){
        List<String> ids = new LinkedList<>();
        JSONArray entries = new JSONObject(jsonContent).getJSONArray("data");
        for(int i = 0 ; i < entries.length() ; ++i){
            ids.add(entries.getJSONObject(i).getString("id"));
        }
        return ids;
    }
    /**
     * Creates a request for the submissions between the given interval in
     * ascending order with an upper limit of at most 500 submissions.
     * @return A JSON string containing all submissions within the interval.
     * @throws IOException if a communication error occured.
     */
    protected String requestJsonContent(String subreddit, Date after, Date before) throws IOException{
        rateLimiter.acquire();
        StringBuilder builder = new StringBuilder();
        URL url = new URL(createUrl(subreddit, after, before));
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line = reader.readLine();
            while(line != null){
                builder.append(line);
                line = reader.readLine();
            }
        }
        return builder.toString();
    }
    /**
     * Generates the URL for the request.
     * @return the URL that represents this request.
     */
    protected String createUrl(String subreddit, Date after, Date before){
        StringBuilder builder = new StringBuilder();
        builder.append("https://api.pushshift.io/reddit/search/submission/?subreddit=").append(subreddit);
        builder.append("&after=").append(after.getTime()/MILLISECONDS_TO_SECONDS);
        builder.append("&before=").append(before.getTime()/MILLISECONDS_TO_SECONDS);
        builder.append("&sort=desc&size=500");
        return builder.toString();
    }
}
