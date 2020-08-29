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

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JSONSubreddit extends Subreddit {
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
    @Nonnull
    private final Path path;
    @Nonnull
    private final Subreddit subreddit;

    private JSONSubreddit(@Nonnull Subreddit subreddit, @Nonnull Path path){
        this.subreddit = subreddit;
        this.path = path;
    }

    public static JSONSubreddit of(@Nonnull Subreddit subreddit, @Nonnull Path path){
        return new JSONSubreddit(subreddit, path);
    }

    @Override
    public List<Submission> getSubmissions(Instant inclusiveFrom, Instant exclusiveTo) throws UnsuccessfulRequestException, TimeoutException, HttpResponseException {
        log.debug("Request submissions for [{}, {})", inclusiveFrom, exclusiveTo);
        List<Submission> submissions = new ArrayList<>();
        Range<Instant> range = Range.closedOpen(inclusiveFrom, exclusiveTo);

        //The bounds of createContiguousSet are inclusive
        for(LocalDate date : createContiguousSet(inclusiveFrom, exclusiveTo.minus(1, ChronoUnit.DAYS)))
            for(Submission submission : requestSubmission(date))
                if(range.contains(submission.getCreated()))
                    submissions.add(submission);

        return submissions;
    }

    private List<Submission> requestSubmission(LocalDate date) throws UnsuccessfulRequestException, TimeoutException, HttpResponseException{
        List<Submission> submissions;

        if(hasJsonSubmissions(date)) {
            log.debug("JSON files found for {}", date);
            submissions = loadJsonSubmissions(date);
        }else {
            log.debug("Request submissions for {}", date);
            submissions = requestRedditSubmissions(date);
            storeSubmissions(submissions);
        }

        return submissions;
    }

    private void storeSubmissions(List<Submission> submissions) throws UnsuccessfulRequestException{
        try {
            for(Submission submission : submissions){
                Path filePath = getSubmissionsPath(submission);

                if(Files.notExists(filePath.getParent()))
                    Files.createDirectories(filePath.getParent());

                if(Files.notExists(filePath))
                    Files.createFile(filePath);

                FileWriter writer = new FileWriter(filePath.toFile());
                JSONSubmission.of(submission).write(writer, 4, 0);
                writer.close();
            }
        }catch(IOException e){
            throw new UnsuccessfulRequestException(e);
        }
    }

    private List<Submission> requestRedditSubmissions(LocalDate date) throws UnsuccessfulRequestException, TimeoutException, HttpResponseException{
        return subreddit.getSubmissions(
                date.atStartOfDay(ZoneOffset.UTC).toInstant(),
                date.atStartOfDay(ZoneOffset.UTC).plusDays(1).toInstant()
        );
    }

    private List<Submission> loadJsonSubmissions(LocalDate date) throws UnsuccessfulRequestException{
        try {
            List<Submission> submissions = new ArrayList<>();

            for(Path submission : Files.list(getSubmissionsPath(date)).collect(Collectors.toList()))
                submissions.add(JSONSubmission.of(submission));

            return submissions;
        }catch(IOException e){
            throw new UnsuccessfulRequestException(e);
        }
    }

    private boolean hasJsonSubmissions(@Nonnull LocalDate date){
        return Files.exists(getSubmissionsPath(date));
    }

    @Nonnull
    private LocalDate getLocalDate(@Nonnull Instant date){
        return LocalDate.ofInstant(date, ZoneOffset.UTC);
    }

    @Nonnull
    private Path getSubmissionsPath(LocalDate parentDirectory){
        return path.resolve(parentDirectory.toString());
    }

    @Nonnull
    private Path getSubmissionsPath(@Nonnull Submission submission){
        Path parentPath = getSubmissionsPath(getLocalDate(submission.getCreated()));
        String fileName = submission.getId() + ".json";

        return parentPath.resolve(fileName);
    }

    @Nonnull
    private Set<LocalDate> createContiguousSet(@Nonnull Instant lowerBound, @Nonnull Instant upperBound){
        Range<LocalDate> range = Range.closed(getLocalDate(lowerBound), getLocalDate(upperBound));
        DiscreteDomain<LocalDate> domain = new DiscreteLocalDateDomain();

        return ContiguousSet.create(range, domain);
    }

    public static class DiscreteLocalDateDomain extends DiscreteDomain<LocalDate>{
        @Override
        public LocalDate minValue(){
            return LocalDate.MIN;
        }

        @Override
        public LocalDate maxValue(){
            return LocalDate.MAX;
        }

        @Override
        public LocalDate next(@Nonnull LocalDate localDate) {
            return localDate.plusDays(1);
        }

        @Override
        public LocalDate previous(@Nonnull LocalDate localDate) {
            return localDate.minusDays(1);
        }

        @Override
        public long distance(@Nonnull LocalDate dateBefore, @Nonnull LocalDate dateAfter) {
            return ChronoUnit.DAYS.between(dateBefore, dateAfter);
        }
    }
}
