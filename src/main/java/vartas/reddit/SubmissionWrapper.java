/*
 * Copyright (C) 2017 u/Zavarov
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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.Paginator;

/**
 * An interface to make the interaction between the Reddit API and the Discord API much easier.
 * @author u/Zavarov
 */
public class SubmissionWrapper extends Wrapper<List<Submission>>{
    /**
     * The subreddit we're acquiring the submissions from.
     */
    protected String subreddit;
    /**
     * The date after which we don't want any more submissions.
     */
    protected Date end;
    /**
     * The date after which we want submissions.
     */
    protected Date start;
    /**
     * @param bot the bot that is connected to Reddit.
     */
    public SubmissionWrapper(RedditBot bot){
        super(bot);
    }
    /**
     * Sets the subreddit and the timestamp.
     * @param subreddit the subreddit.
     * @param start the timestamp at which we start.
     * @param end the timestamp at which we end.
     */
    public void parameter(String subreddit, Date start, Date end){
        this.subreddit = subreddit;
        this.start = start;
        this.end = end;
    }
    /**
     * Reads the latest submissions from the specified updated, that are newer than the specified threshold.
     * @return a list of the most recent submissions in ascending order.
     */
    @Override
    public List<Submission> request(){
        
        DefaultPaginator<Submission> paginator = bot.getClient().subreddit(subreddit)
                .posts()
                .sorting(SubredditSort.NEW)
                .limit(Paginator.RECOMMENDED_MAX_LIMIT)
                .timePeriod(TimePeriod.ALL)
                .build();
        List<Submission> submissions = new LinkedList<>();
        Listing<Submission> listing;
        do{
            listing = paginator.next();
            listing.stream()
                    .filter(s -> s.getCreated().before(end))
                    .filter(s -> s.getCreated().after(start))
                    .forEach(submissions::add);
        //Repeat when we haven't found the last submission
        }while(!listing.isEmpty() && listing.get(listing.size()-1).getCreated().before(end));
        return submissions;
    }
}