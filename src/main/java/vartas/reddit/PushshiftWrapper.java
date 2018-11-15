package vartas.reddit;

/*
 * Copyright (C) 2018 u/Zavarov
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

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.RateLimiter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Submission;
import net.dean.jraw.tree.CommentNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;
import static vartas.xml.XMLCollection.NODE_TO_STRING;
import static vartas.xml.XMLCollection.STRING_TO_NODE;
import vartas.xml.XMLDocument;
import vartas.xml.collection.XMLList;
import vartas.xml.collection.XMLMap;
import vartas.xml.strings.XMLStringMap;

/**
 * This class retrieves the data from Reddit and stores them in an internal
 * data structure.
 * @author u/Zavarov
 */
public class PushshiftWrapper extends Wrapper<Void>{
    /**
     * The path to the comments file.
     */
    protected static String COMMENTS_FILE = "comments";
    /**
     * The path to the submission file.
     */
    protected static String SUBMISSIONS_FILE = "submissions";
    /**
     * We only allow a single request per second for pushshift.
     */
    protected final RateLimiter rate_limiter = RateLimiter.create(1);
    /**
     * The subreddit we're acquiring the submissions from.
     */
    protected String subreddit;
    /**
     * The date after which we don't want any more submissions.
     */
    protected Instant after;
    /**
     * The date after which we want submissions.
     */
    protected Instant before;
    /**
     * This format is used to generate the files for each entry.
     */
    public static final SimpleDateFormat DATE = new SimpleDateFormat("dd-MM-yyyy");
    /**
     * Set the correct timezone.
     */
    {
        DATE.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    /**
     * Sets up all the credentials for the Reddit client
     * @param bot the bot that has access to the Reddit API.
     */
    public PushshiftWrapper(RedditBot bot){
        super(bot);
    }
    /**
     * Sets the subreddit and the timestamp.
     * @param subreddit the subreddit.
     * @param before the timestamp at which we start.
     * @param after the timestamp at which we end.
     */
    public void parameter(String subreddit, Instant before, Instant after){
        this.subreddit = subreddit;
        this.before = before;
        this.after = after;
    }
    /**
     * @param file the XML file that is going to be loaded.
     * @return the XML instance containing the content of the file.
     */
    private XMLList<XMLMap<String,String>> load(File file){
        //The list will be transformed by the other load functions
        //So it'll never be written
        return XMLList.create(file, 
                n -> XMLMap.create(
                        (Element)XMLDocument.getNode("document", (Element)n),
                        NODE_TO_STRING, 
                        STRING_TO_NODE,
                        Function.identity()),
                null);
    }
    /**
     * @param subreddit the subreddit the submissions are in.
     * @param date the date when the submissions happened.
     * @return a list of all submissions in the subreddit on that date. 
     */
    private List<CompactSubmission> loadSubmission(String subreddit, Instant date){
        File file = submissionFile(subreddit,date);
        if(!file.exists())
            return ImmutableList.of();
        
        return load(file).stream()
                .map(e -> {
                    CompactSubmission s = new CompactSubmission();
                    s.putAll(e);
                    return s;
                }).collect(Collectors.toList());
    }
    /**
     * @param subreddit the subreddit the comments are in.
     * @param date the date when the comments happened.
     * @return a list of all comments in the subreddit on that date. 
     */
    private List<CompactComment> loadComment(String subreddit, Instant date){
        File file = commentFile(subreddit, date);
        
        if(!file.exists())
            return ImmutableList.of();
        
        
        return load(file).stream()
                .map(e -> {
                    CompactComment s = new CompactComment();
                    s.putAll(e);
                    return s;
                }).collect(Collectors.toList());
    }
    /**
     * @param subreddit the subreddit the comments are in.
     * @param date the date when the comments happened.
     * @return the file containing all comments from that subreddit on that date 
     */
    private File commentFile(String subreddit, Instant date){
        return new File(String.format("%s/%s/%s.com",
                COMMENTS_FILE,
                subreddit,
                DATE.format(Date.from(date))));
    }
    /**
     * 
     * @param subreddit the subreddit the submissions are in.
     * @param date the date when the submissions happened.
     * @return the file containing all submissions from that subreddit on that date 
     */
    private File submissionFile(String subreddit, Instant date){
        return new File(String.format("%s/%s/%s.sub",
                SUBMISSIONS_FILE,
                subreddit,
                DATE.format(Date.from(date))));
    }
    /**
     * @param date the time.
     * @param subreddit the subreddit.
     * @return all submissions at the specified time in the specified subreddit.
     */
    public List<CompactSubmission> getSubmissions(Instant date, String subreddit){
        return loadSubmission(subreddit, date);
    }
    /**
     * @param date the time.
     * @param subreddit the subreddit.
     * @return all comments in the subreddit at the specified time. 
     */
    public List<CompactComment> getComments(Instant date, String subreddit){
        return loadComment(subreddit, date);
    }
    /**
     * Request all submissions in the previously specified interval and
     * stores them in the internal maps.
     * @throws java.io.IOException when the HTTP request failed
     * @throws java.lang.InterruptedException when the process of writing to the files was interrupted.
     * @return null
     */
    @Override
    public Void request() throws IOException, InterruptedException{
        String listing = requestJsonContent();
        List<String> ids = extractIds(listing);
        
        //Store temporarily in case of an unexpected error
        XMLList<XMLStringMap<String>> submissions = XMLList.create(e -> e.update().getHead());
        XMLList<XMLStringMap<String>> comments = XMLList.create(e -> e.update().getHead());
        
        //Go through each submission
        ids.forEach( id -> {
            Submission submission = bot.getClient().submission(id).inspect();
            //Simplify the submission time to the current day
            submissions.add(extractData(submission));
            
            //Visit all comments iteratively
            Deque<CommentNode<Comment>> deque = new ArrayDeque<>();
            deque.addAll(bot.getClient().submission(id).comments().getReplies());
            
            while(!deque.isEmpty()){
                CommentNode<Comment> node = deque.pop();
                node.loadFully(bot.getClient());
                    
                Comment comment = node.getSubject();
                comments.add(extractData(comment,submission));
                deque.addAll(node.getReplies());
            }
        });
        //Store all files.
        File file = submissionFile(subreddit,after);
        file.getParentFile().mkdirs();
        file.createNewFile();
        submissions.update().write(new FileOutputStream(file), null);
        file = commentFile(subreddit,after);
        file.getParentFile().mkdirs();
        file.createNewFile();
        comments.update().write(new FileOutputStream(file), null);
        return null;
    }
    /**
     * Extracts the relevant parts of the comment.
     * @param comment the source.
     * @param submission the submission this comment is in
     * @return a map containing the most relevant information for us.
     */
    protected XMLStringMap<String> extractData(Comment comment, Submission submission){
        XMLStringMap<String> data = new XMLStringMap<>();
        
        data.put("author",comment.getAuthor());
        data.put("score",Integer.toString(comment.getScore()));
        data.put("id",comment.getId());
        data.put("submission",submission.getId());
        data.put("subreddit",comment.getSubreddit());
        data.put("title",submission.getTitle());
        
        return data;
    }
    /**
     * Extracts the relevant parts of the submission.
     * @param submission the source.
     * @return a map containing the most relevant information for us.
     */
    protected XMLStringMap<String> extractData(Submission submission){
        XMLStringMap<String> data = new XMLStringMap<>();
        
        data.put("author",submission.getAuthor());
        data.put("id",submission.getId());
        data.put("link_flair_text",submission.getLinkFlairText());
        data.put("over18",Boolean.toString(submission.isNsfw()));
        data.put("spoiler",Boolean.toString(submission.isSpoiler()));
        data.put("score",Integer.toString(submission.getScore()));
        data.put("subreddit",submission.getSubreddit());
        data.put("title",submission.getTitle());
        
        return data;
    }
    /**
     * Extracts the submissions from the JSON string.
     * @param content the JSON string.
     * @return a list containing all submissions.
     */
    protected List<String> extractIds(String content){
        List<String> ids = new LinkedList<>();
        JSONArray entries = new JSONObject(content).getJSONArray("data");
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
    protected String requestJsonContent() throws IOException{
        rate_limiter.acquire();
        StringBuilder builder = new StringBuilder();
        URL url = new URL(createUrl());
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
    protected String createUrl(){
        StringBuilder builder = new StringBuilder();
        builder.append("https://api.pushshift.io/reddit/search/submission/?subreddit=").append(subreddit);
        builder.append("&after=").append(Long.toString(after.getEpochSecond()));
        builder.append("&before=").append(Long.toString(before.getEpochSecond()));
        builder.append("&sort=desc&size=500");
        return builder.toString();
    }
    /**
     * This class acts as a wrapper for a compact representation for the comments.
     */
    public static class CompactComment extends XMLStringMap<String>{
        private static final long serialVersionUID = 0L;
        /**
         * The URL frame that will link to the comment.
         */
        private static final String URL = "https://www.reddit.com/r/%s/comments/%s/-/%s";
        /**
         * Guarantee that we won't have null as a value.
         * @param key the key of the entry.
         * @param value the value of the entry.
         * @return the possible value that has been removed or null, otherwise.
         */
        @Override
        public String put(String key, String value){
            return super.put(key, value == null ? "" : value);
        }
        /**
         * @return the author of the comment. 
         */
        public String getAuthor(){
            return super.get("author");
        }
        /**
         * @return the id of the comment. 
         */
        public String getId(){
            return super.get("id");
        }
        /**
         * @return the upvotes minus the downvotes. 
         */
        public long getScore(){
            return Long.parseLong(super.get("score"));
        }
        /**
         * @return the  name of the submission, the comment is in.
         */
        public String getSubmission(){
            return super.get("submission");
        }
        /**
         * @return the name of the subreddit, the comment is in.
         */
        public String getSubreddit(){
            return super.get("subreddit");
        }
        /**
         * @return the title of the submission, the comment is in. 
         */
        public String getTitle(){
            return super.get("title");
        }
        /**
         * @return a permalink to the comment. 
         */
        public String getPermalink(){
            return String.format(URL,getSubreddit(),getSubmission(),getId());
        }
    }
    /**
     * This class acts as a wrapper for a compact representation for the submissions.
     */
    public static class CompactSubmission extends XMLStringMap<String>{
        private static final long serialVersionUID = 0L;
        /**
         * The URL frame that will link to the submission.
         */
        private static final String URL = "https://redd.it/%s";
        /**
         * Guarantee that we won't have null as a value.
         * @param key the key of the entry.
         * @param value the value of the entry.
         * @return the possible value that has been removed or null, otherwise.
         */
        @Override
        public String put(String key, String value){
            return super.put(key, value == null ? "" : value);
        }
        /**
         * @return the author of the submission. 
         */
        public String getAuthor(){
            return super.get("author");
        }
        /**
         * @return the id of the submission. 
         */
        public String getId(){
            return super.get("id");
        }
        /**
         * @return the flair text, if one exists, or null otherwise. 
         */
        public String getLinkFlairText(){
            return super.get("link_flair_text");
        }
        /**
         * @return the subreddit the submission was posted in.
         */
        public String getSubreddit(){
            return super.get("subreddit");
        }
        /**
         * @return true, if the submission is marked as NSFW. 
         */
        public boolean isNsfw(){
            return Boolean.parseBoolean(super.get("over18"));
        }
        /**
         * @return true, if the submission is marked as spoiler.
         */
        public boolean isSpoiler(){
            return Boolean.parseBoolean(super.get("spoiler"));
        }
        /**
         * @return the upvotes minus the downvotes. 
         */
        public long getScore(){
            return Long.parseLong(super.get("score"));
        }
        /**
         * @return the title of the submission. 
         */
        public String getTitle(){
            return super.get("title");
        }
        /**
         * @return the permalink to the submission. 
         */
        public String getPermalink(){
            return String.format(URL,getId());
        }
    }
}