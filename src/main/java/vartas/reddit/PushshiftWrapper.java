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

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import vartas.xml.collection.XMLMultitable;
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
    protected static String COMMENTS_FILE = "comments.xml";
    /**
     * The path to the submission file.
     */
    protected static String SUBMISSIONS_FILE = "submissions.xml";
    /**
     * (unix timestamp, subreddit) -> {author,id,link_flair_text,over18,spoiler,score}
     */
    protected final XMLMultitable<Instant,String,CompactSubmission> submissions;
    /**
     * (submission id, subreddit) -> {author,score,id} 
     */
    protected final XMLMultitable<Instant,String,CompactComment> comments;
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
     * Sets up all the credentials for the Reddit client
     * @param bot the bot that has access to the Reddit API.
     */
    public PushshiftWrapper(RedditBot bot){
        super(bot);
        submissions = XMLMultitable.create(e -> e.update().getHead());
        comments = XMLMultitable.create(e -> e.update().getHead());
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
     * Removes all instances from the specified subreddit at the specified time.
     * @param date the time.
     * @param subreddit the subreddit.
     */
    public void remove(Instant date, String subreddit){
        submissions.remove(date, subreddit);
        comments.remove(date, subreddit);
    }
    /**
     * Removes all instances from the specified subreddit from the internal database.
     * @param subreddit the subreddit.
     */
    public void removeSubreddit(String subreddit){
        submissions.columnMap().remove(subreddit);
        comments.columnMap().remove(subreddit);
    }
    /**
     * Removes all instances at the specified time from the internal database.
     * @param date the time.
     */
    public void removeDate(Instant date){
        submissions.rowMap().remove(date);
        comments.rowMap().remove(date);
    }
    /**
     * @param subreddit the subreddit.
     * @return all (time,submissions) instances in the specified subreddit.
     */
    public Map<Instant,List<CompactSubmission>> getSubmissions(String subreddit){
        Map<Instant,XMLList<CompactSubmission>> map = submissions.column(subreddit);
        return Maps.asMap(map.keySet(), map::get);
    }
    /**
     * @param date the time.
     * @return all (subreddit,submissions) instances at the specified time.
     */
    public Map<String,List<CompactSubmission>> getSubmissions(Instant date){
        Map<String,XMLList<CompactSubmission>> map = submissions.row(date);
        return Maps.asMap(map.keySet(), map::get);
    }
    /**
     * @param date the time.
     * @param subreddit the subreddit.
     * @return all submissions at the specified time in the specified subreddit.
     */
    public List<CompactSubmission> getSubmissions(Instant date, String subreddit){
        return submissions.get(date, subreddit);
    }
    /**
     * @param date the time.
     * @param subreddit the subreddit.
     * @return all comments in the subreddit at the specified time. 
     */
    public List<CompactComment> getComments(Instant date, String subreddit){
        return comments.get(date, subreddit);
    }
    /**
     * @param subreddit a subreddit.
     * @return a map of all (time stamp,submissions) instances in the specified subreddit.
     */
    public Map<Instant,List<CompactComment>> getComments(String subreddit){
        Map<Instant,XMLList<CompactComment>> map = comments.column(subreddit);
        return Maps.asMap(map.keySet(), map::get);
    }
    /**
     * @param date a time stamp.
     * @return a map of all (subreddit,submissions) instances at the specified date.
     */
    public Map<String,List<CompactComment>> getComments(Instant date){
        Map<String,XMLList<CompactComment>> map = comments.row(date);
        return Maps.asMap(map.keySet(), map::get);
    }
    /**
     * @return a list of all known subreddits. 
     */
    public List<String> getSubreddits(){
        return comments.columnKeySet()
                .stream()
                .collect(Collectors.toList());
    }
    /**
     * @return a sorted list of all known time stamps.
     */
    public List<Instant> getDates(){
        return comments.rowKeySet()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }
    /**
     * Loads the submission and comment files and adds their content to the
     * current instance, if they exist.
     * @throws IOException if the serial file couldn't be accessed.
     * @throws ClassNotFoundException if the serial object belongs to an unknown class.
     */
    public void read() throws IOException, ClassNotFoundException{
        File file;
        
        file = new File(SUBMISSIONS_FILE);
        if(file.exists()){
            _read(SUBMISSIONS_FILE).cellSet().forEach(cell -> {
                cell.getValue().forEach(value -> {
                    CompactSubmission submission = new CompactSubmission();
                    submission.putAll(value);
                    submissions.putSingle(cell.getRowKey(), cell.getColumnKey(), submission);
                });
            });
        }
        file = new File(COMMENTS_FILE);
        if(file.exists()){
            _read(COMMENTS_FILE).cellSet().forEach(cell -> {
                cell.getValue().forEach(value -> {
                    CompactComment comment = new CompactComment();
                    comment.putAll(value);
                    comments.putSingle(cell.getRowKey(), cell.getColumnKey(), comment);
                });
            });
        }
    }
    /**
     * 
     * @param name the name of the file.
     * @return an instance of the serial data.
     * @throws IOException if the serial object couldn't be accessed.
     * @throws ClassNotFoundException if the serial object is of an unknown class.
     */
    protected XMLMultitable<Instant,String,XMLMap<String,String>> _read(String name) throws IOException, ClassNotFoundException{
        return XMLMultitable.create(new File(name), 
                e -> {
                    return XMLStringMap.create((Element)XMLDocument.getNode("document", (Element)e), NODE_TO_STRING, STRING_TO_NODE, Function.identity());
                }, 
                null, 
                r -> Instant.ofEpochMilli(Long.parseLong(r)), 
                Function.identity());
    }
    /**
     * Writes the submissions and comments to the hard disk.
     * @throws IOException if an error occured while writing the data.
     * @throws InterruptedException if the program was interrupted before the writing process was finished.
     */
    public void store() throws IOException, InterruptedException{
        File file;
        file = new File(SUBMISSIONS_FILE);
        if(file.exists()) file.delete();
        file = new File(COMMENTS_FILE);
        if(file.exists()) file.delete();        
        
        _store(SUBMISSIONS_FILE,submissions);
        _store(COMMENTS_FILE,comments);
        
    }
    /**
     * Writes a data structure to a file.
     * @param name the file name the data is stored in.
     * @param data the table that is written to the hard disk.
     * @throws IOException if an error occured while writing the data.
     * @throws InterruptedException if the program was interrupted before the writing process was finished.
     */
    private void _store(String name, XMLMultitable<Instant,String,?> data) throws IOException, InterruptedException{
        data.update(i -> Long.toString(i.toEpochMilli()),Function.identity())
            .write(new FileOutputStream(name), null);
    }
    /**
     * Request all submissions in the previously specified interval and
     * stores them in the internal maps.
     * @throws java.io.IOException when the HTTP request failed
     * @return null
     */
    @Override
    public Void request() throws IOException{
        String listing = requestJsonContent();
        List<String> ids = extractIds(listing);
        
        //Store temporarily in case of an unexpected error
        XMLMultitable<Instant,String,CompactSubmission> local_submissions;
        XMLMultitable<Instant,String,CompactComment> local_comments;
        local_submissions = XMLMultitable.create(null);
        local_comments = XMLMultitable.create(null);
        
        local_submissions.put(after, subreddit, XMLList.create(null));
        local_comments.put(after, subreddit, XMLList.create(null));
        
        //Go through each submission
        ids.forEach( id -> {
            Submission submission = bot.getClient().submission(id).inspect();
            //Simplify the submission time to the current day
            local_submissions
                    .get(after, subreddit)
                    .add(extractData(submission));
            
            //Visit all comments iteratively
            Deque<CommentNode<Comment>> deque = new ArrayDeque<>();
            deque.addAll(bot.getClient().submission(id).comments().getReplies());
            
            while(!deque.isEmpty()){
                CommentNode<Comment> node = deque.pop();
                node.loadFully(bot.getClient());
                    
                Comment comment = node.getSubject();
                local_comments
                        .get(after,subreddit)
                        .add(extractData(comment,submission));
                deque.addAll(node.getReplies());
            }
        });
        submissions.putAll(local_submissions);
        comments.putAll(local_comments);
        return null;
    }
    /**
     * Extracts the relevant parts of the comment.
     * @param comment the source.
     * @param submission the submission this comment is in
     * @return a map containing the most relevant information for us.
     */
    protected CompactComment extractData(Comment comment, Submission submission){
        CompactComment data = new CompactComment();
        
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
    protected CompactSubmission extractData(Submission submission){
        CompactSubmission data = new CompactSubmission();
        
        data.put("author",submission.getAuthor());
        data.put("id",submission.getId());
        data.put("link_flair_text",submission.getLinkFlairText() == null ? "" : submission.getLinkFlairText());
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