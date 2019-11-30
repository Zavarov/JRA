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
import vartas.reddit.Client;
import vartas.reddit.Submission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * This class implements the Reddit client using both the JRAW library and the Pushshift API to circumvent the 1000
 * submission limit imposed by Reddit.
 */
public class PushshiftClient implements Client {
    /**
     * We only allow a single request per second for pushshift.
     */
    protected final RateLimiter rateLimiter = RateLimiter.create(1);
    /**
     * The underlying client.
     */
    protected Client client;

    /**
     * Initializes the client but doesn't establish a connection to Reddit.
     * @param client the underlying client responsible for the communication with the Reddit API.
     */
    public PushshiftClient(Client client) {
        this.client = client;
    }

    /**
     * Request submissions within a given interval sorted by their creation date.<br>
     * In order to circumvent the 1000 submissions restriction, they are first requested via the Pushshift API in order
     * to get their ids, then those ids are used for explicitly request them using the Reddit API.
     * @param subreddit The subreddit name.
     * @param start The (inclusive) age of the oldest submissions.
     * @param end The (exclusive) age of the newest submissions.
     * @return All submissions within the given interval.
     */
    @Override
    public Optional<Collection<Submission>> requestSubmission(String subreddit, LocalDateTime start, LocalDateTime end) {
        String jsonContent;
        //Break if the JSON request failed
        try {
            jsonContent = requestJsonContent(subreddit, start, end);

            TreeSet<Submission> submissions = new TreeSet<>();

            for(String id : extractSubmissions(jsonContent))
                client.requestSubmission(id).ifPresent(submissions::add);

            return Optional.of(submissions);
        }catch(IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Extracts the submissions from the JSON string.
     * @param jsonContent The received JSON string.
     * @return A list containing all submissions.
     */
    private List<String> extractSubmissions(String jsonContent){
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
     * @param subreddit The subreddit name.
     * @param start The (inclusive) age of the oldest submissions.
     * @param end The (exclusive) age of the newest submissions.
     * @return A JSON string containing all submissions within the interval.
     * @throws IOException If a communication error occurred.
     */
    private String requestJsonContent(String subreddit, LocalDateTime start, LocalDateTime end) throws IOException{
        rateLimiter.acquire();
        StringBuilder builder = new StringBuilder();
        URL url = new URL(createUrl(subreddit, start, end));
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
     * @param subreddit The name of the subreddit.
     * @param start The (inclusive) minimum age of the submissions.
     * @param end The (exclusive) maximum age of the submissions.
     * @return The URL that represents this request.
     */
    private String createUrl(String subreddit, LocalDateTime start, LocalDateTime end){
        StringBuilder builder = new StringBuilder();
        builder.append("https://api.pushshift.io/reddit/search/submission/?subreddit=").append(subreddit);
        //Offset by 1 second to make 'after' and 'before' inclusive.
        builder.append("&after=").append(start.toEpochSecond(ZoneOffset.UTC)-1);    //after - 1 -> Inclusive after
        builder.append("&before=").append(end.toEpochSecond(ZoneOffset.UTC));       //before    -> Exclusive before
        builder.append("&sort=desc&size=500");
        return builder.toString();
    }
    /**
     * Logs the underlying client in.
     */
    @Override
    public void login(){
        client.login();
    }

    /**
     * Logs the underlying client out.
     */
    @Override
    public void logout(){
        client.logout();
    }
}
