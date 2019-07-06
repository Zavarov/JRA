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
import net.dean.jraw.references.SubmissionReference;
import org.json.JSONArray;
import org.json.JSONObject;
import vartas.reddit.SubmissionInterface;
import vartas.reddit.UnresolvableRequestException;
import vartas.reddit.jraw.JrawClient;
import vartas.reddit.jraw.JrawSubmission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class implements the Reddit client using both the JRAW library and the Pushshift API to circumvent the 1000
 * submission limit imposed by Reddit.
 */
public class PushshiftClient extends JrawClient {
    /**
     * We only allow a single request per second for pushshift.
     */
    protected final RateLimiter rateLimiter = RateLimiter.create(1);
    /**
     * A scaling factor to convert the time in milliseconds to the time in seconds.
     */
    protected final static int MILLISECONDS_TO_SECONDS = 1000;

    /**
     * Initializes the client but doesn't establish a connection to Reddit.
     * @param redditName the account that created the bot.
     * @param version this version of the bot.
     * @param clientId the id of the client.
     * @param secret the secret of the client.
     */
    public PushshiftClient(String redditName, String version, String clientId, String secret) {
        super(redditName, version, clientId, secret);
    }

    /**
     * Request submissions within a given interval.
     * In order to circumvent the 1000 submissions restriction, they are first requested via the Pushshift API in order
     * to get their ids, then those ids are used for the communication with the Reddit API.
     * @param subreddit the name of the subreddit.
     * @param after the (exclusive) minimum age of the submissions.
     * @param before the (exclusive) maximum age of the submissions.
     * @return all submissions within the given interval.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    @Override
    public Optional<List<SubmissionInterface>> requestSubmission(String subreddit, Date after, Date before) throws UnresolvableRequestException{
        return request(() -> {
            String jsonContent;
            //Break if the JSON request failed
            try {
                jsonContent = requestJsonContent(subreddit, after, before);
            }catch(IOException e) {
                return Optional.empty();
            }
            List<SubmissionInterface> submissions = extractSubmissions(jsonContent)
                    .stream()
                    .map(client::submission)
                    .map(SubmissionReference::inspect)
                    .map(JrawSubmission::new)
                    .collect(Collectors.toList());

            return Optional.of(submissions);
        });
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
