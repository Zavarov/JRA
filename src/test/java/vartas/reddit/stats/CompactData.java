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
package vartas.reddit.stats;

import java.util.ArrayList;
import java.util.List;
import vartas.reddit.PushshiftWrapper.CompactComment;
import vartas.reddit.PushshiftWrapper.CompactSubmission;

/**
 *
 * @author u/Zavarov
 */
public class CompactData {
    public List<CompactComment> comments;
    public List<CompactSubmission> submissions;
    
    public CompactData(){
        comments = new ArrayList<>();
        submissions = new ArrayList<>();
        
        CompactComment comment;
        CompactSubmission submission;
        
        //~~~~~~~~~~~~~~~~~~~~
        //Comments
        //~~~~~~~~~~~~~~~~~~~~
        comment = new CompactComment();
        comment.put("author","author1");
        comment.put("id","id1");
        comment.put("score","1");
        comment.put("submission","submission1");
        comment.put("subreddit","subreddit1");
        comment.put("title","title1");
        comments.add(comment);
        
        comment = new CompactComment();
        comment.put("author","author2");
        comment.put("id","id2");
        comment.put("score","2");
        comment.put("submission","submission2");
        comment.put("subreddit","subreddit2");
        comment.put("title","title2");
        comments.add(comment);
        
        //~~~~~~~~~~~~~~~~~~~~
        //Submissions        
        //~~~~~~~~~~~~~~~~~~~~
        submission = new CompactSubmission();
        submission.put("author","author1");
        submission.put("id","id1");
        submission.put("score","1");
        submission.put("title","title1");
        submission.put("link_flair_text","");
        submission.put("subreddit","subreddit");
        submission.put("over18", "true");
        submission.put("spoiler","true");
        submissions.add(submission);
        
        submission = new CompactSubmission();
        submission.put("author","author2");
        submission.put("id","id2");
        submission.put("score","2");
        submission.put("title","title2");
        submission.put("link_flair_text","flair");
        submission.put("subreddit","subreddit");
        submission.put("over18", "false");
        submission.put("spoiler","false");
        submissions.add(submission);
        
        submission = new CompactSubmission();
        submission.put("author","author3");
        submission.put("id","id3");
        submission.put("score","3");
        submission.put("title","title3");
        submission.put("link_flair_text","flair");
        submission.put("subreddit","subreddit");
        submission.put("over18", "true");
        submission.put("spoiler","true");
        submissions.add(submission);
        
        submission = new CompactSubmission();
        submission.put("author","author4");
        submission.put("id","id4");
        submission.put("score","4");
        submission.put("title","title4");
        submission.put("link_flair_text","flair");
        submission.put("subreddit","subreddit");
        submission.put("over18", "true");
        submission.put("spoiler","false");
        submissions.add(submission);
        
        
        submission = new CompactSubmission();
        submission.put("author","author5");
        submission.put("id","id5");
        submission.put("score","5");
        submission.put("title","title5");
        submission.put("link_flair_text","flair");
        submission.put("subreddit","subreddit");
        submission.put("over18", "false");
        submission.put("spoiler","true");
        submissions.add(submission);
    }
}
