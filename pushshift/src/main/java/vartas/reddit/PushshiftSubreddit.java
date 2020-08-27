/*
 * Copyright (c) 2020 Zavarov
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

package vartas.reddit;

import net.dean.jraw.RedditClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PushshiftSubreddit extends JrawSubreddit{
    private static final String URL = "https://api.pushshift.io";
    private static final String SUBMISSION_ENDPOINT = "/reddit/submission/search";

    public PushshiftSubreddit(RedditClient jrawClient){
        super(jrawClient);
    }

    @Override
    protected void requestSubmissions(Instant inclusiveFrom, Instant exclusiveTo) throws TimeoutException, UnsuccessfulRequestException, HttpResponseException {
        for(Submission submission : JrawClient.request(() -> requestPushshiftSubmissions(inclusiveFrom, exclusiveTo),0))
            putSubmissions(submission.getId(), submission);
    }

    private Optional<List<Submission>> requestPushshiftSubmissions(Instant inclusiveFrom, Instant exclusiveTo){
        try{
            List<Submission> submissions = new ArrayList<>();

            for(JSONObject submission : requestAllPushshiftSubmissions(inclusiveFrom, exclusiveTo))
                submissions.add(getSubmissions(submission.getString("id")));

            return Optional.of(submissions);
        }catch(Exception e){
            return Optional.empty();
        }
    }

    private List<JSONObject> requestAllPushshiftSubmissions(Instant inclusiveFrom, Instant exclusiveTo) throws HttpResponseException, TimeoutException, UnsuccessfulRequestException{
        List<JSONObject> allSubmissions = new ArrayList<>();

        JSONArray submissions;
        do{
            submissions = executePushshiftRequest(inclusiveFrom, exclusiveTo).getJSONArray("data");

            for(int i = 0 ; i < submissions.length() ; ++i)
                allSubmissions.add(submissions.getJSONObject(i));

            //Shift the interval to get the remaining submissions in the next requests
            if(hasNext(submissions, exclusiveTo))
                exclusiveTo = getCreated(submissions.getJSONObject(submissions.length() - 1));
        }while(!submissions.isEmpty());

        return allSubmissions;
    }

    private JSONObject executePushshiftRequest(Instant inclusiveFrom, Instant exclusiveTo) throws HttpResponseException, TimeoutException, UnsuccessfulRequestException{
        try {
            HttpClient httpClient = HttpClientBuilder.create().setUserAgent(jrawClient.getHttp().getUserAgent().getValue()).build();
            HttpGet httpGet = new HttpGet(buildSubmissionRequest(inclusiveFrom, exclusiveTo));
            HttpResponse httpResponse = httpClient.execute(httpGet);

            handle(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase());

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            StringBuilder buffer = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                line = reader.readLine();
            }

            return new JSONObject(buffer.toString());
        //HttpResponseException is a subset of IOException
        }catch(HttpResponseException e){
            throw e;
        }catch(IOException e){
            throw new UnsuccessfulRequestException();
        }
    }

    private Instant getCreated(JSONObject submission){
        return Instant.ofEpochSecond(submission.getLong("created_utc"));
    }

    private boolean hasNext(JSONArray submissions, Instant inclusiveFrom){
        if(submissions.isEmpty())
            return false;
        else
            //Submissions are sorted by new -> Oldest submission at the end
            return getCreated(submissions.getJSONObject(submissions.length() - 1)).isBefore(inclusiveFrom);
    }

    private void handle(int errorCode, String explanation) throws HttpResponseException, TimeoutException {
        switch(errorCode){
            case HttpStatus.SC_OK:
                break;
            case HttpStatus.SC_GATEWAY_TIMEOUT:
            case HttpStatus.SC_SERVICE_UNAVAILABLE:
                LoggerFactory.getLogger(RedditClient.class.getSimpleName()).warn(explanation);
                throw new TimeoutException();
            default:
                LoggerFactory.getLogger(RedditClient.class.getSimpleName()).error(explanation);
                throw new HttpResponseException(errorCode, explanation);
        }
    }

    private String buildSubmissionRequest(Instant inclusiveFrom, Instant exclusiveTo){
        StringBuilder builder = new StringBuilder();

        builder.append(URL);
        builder.append(SUBMISSION_ENDPOINT);
        builder.append("/?subreddit=").append(getName());
        builder.append("&after=").append(inclusiveFrom.getEpochSecond() - 1);
        builder.append("&before=").append(exclusiveTo.getEpochSecond());
        builder.append("&sort=desc");
        builder.append("&size=500");

        return builder.toString();
    }
}
