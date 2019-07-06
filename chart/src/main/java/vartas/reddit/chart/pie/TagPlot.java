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
package vartas.reddit.chart.pie;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import vartas.reddit.SubmissionInterface;

import java.awt.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class creates a plot over the specified the submissions, that shows
 * the tags for each posts.
 * A submission can either be tagged as NSFW, as a spoiler, both or neither of
 * them.
 */
public class TagPlot implements Function<Collection<? extends SubmissionInterface>, JFreeChart>{
    /**
     * The alpha value of all the colors in the vartas.reddit.chart.
     */
    protected final double alpha;
    /**
     * @param alpha the alpha value of the colors.
     */
    public TagPlot(double alpha){
        this.alpha = alpha;
    }
    /**
     * Creates a vartas.reddit.chart.pie vartas.reddit.chart showing the tags of all submissions.
     * @param submissions a collection over all submissions.
     * @return an image containing the vartas.reddit.chart.pie vartas.reddit.chart.
     */
    @Override
    public JFreeChart apply(Collection<? extends SubmissionInterface> submissions){
        return createTagChart(
                countTags(submissions
                    .stream()
                    .map(this::getTags)),
                    submissions.size());
    }
    
    /**
     * Returns a representation of all the tags of the submission.<br>
     * In case the submission is tagged as NSFW, the String "nsfw" will be
     * included in the set. In case it is tagged as a spoiler, the String
     * "spoiler" is included in the set.
     * It is possible for a set to contain both of those Strings. In case the
     * submission is neither of those, the set will only contain the String
     * "untagged".
     * @param submission the submission we want to extract the tags from.
     * @return the names describing the tags of the submission.
     */
    private Set<String> getTags(SubmissionInterface submission){
        boolean is_nsfw = submission.isNsfw();
        boolean is_spoiler = submission.isSpoiler();
        if(!is_nsfw && !is_spoiler){
            return Collections.singleton("untagged");
        }else{
            Set<String> tags = new HashSet<>();
            if(is_nsfw){
                tags.add("nsfw");
            }
            if(is_spoiler){
                tags.add("spoiler");
            }
            return tags;
        }
    }
    /**
     * Counts the amount of NSFW and spoiler submissions. 
     * @param tags all retrieved tags.
     * @return a map over the NSFW and spoiler posts and their frequency.
     */
    private Map<String,Long> countTags(Stream<Set<String>> tags){
        return tags
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
    }
    /**
     * @param data the data extracted from the submission.
     * @param size the amount of submissions.
     * @return a vartas.reddit.chart.pie vartas.reddit.chart over all tags of the submissions.
     */
    private JFreeChart createTagChart(Map<String,Long> data, int size){
        long rest = data.computeIfAbsent("untagged", (k) -> 0L);
        long both = data.computeIfAbsent("nsfw",(k) -> 0L) + data.computeIfAbsent("spoiler",(k) -> 0L) + rest - size;
        //nsfw does exist after the previous step
        long nsfw = data.get("nsfw") - both;
        long spoiler = data.get("spoiler") - both;
        
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.insertValue(0, "untagged", rest);
        dataset.insertValue(1, "nsfw", nsfw);
        dataset.insertValue(2, "both", both);
        dataset.insertValue(3, "spoiler", spoiler);
        
        JFreeChart chart = ChartFactory.createPieChart3D("Tags", dataset, false, false, Locale.ENGLISH);
        //Add the amount of submissions to the label
        PieSectionLabelGenerator label = new StandardPieSectionLabelGenerator("{0} = {1} ({2})");

        PiePlot plot = (PiePlot)chart.getPlot();
        plot.setLabelGenerator(label);
        plot.setSectionPaint("untagged",new Color(119,119,119,(int)(225*alpha)));
        plot.setSectionPaint("nsfw",new Color(255,0,0,(int)(225*alpha)));
        plot.setSectionPaint("spoiler",new Color(0,0,0,(int)(225*alpha)));
        plot.setSectionPaint("both",blend(Color.RED,Color.BLACK,alpha));
        return chart;
    }
    /**
     * Blends the first color over the second color using the formula:
     * C = A*alpha - (1-alpha)*B
     * @param A the first color.
     * @param B the second color.
     * @param alpha the alpha value.
     * @return the new color that is a result from blending the two colors together.
     */
    private Color blend(Color A, Color B, double alpha){
        int r = (int)(A.getRed() * alpha + (1-alpha) * B.getRed());
        int b = (int)(A.getBlue() * alpha + (1-alpha) * B.getBlue());
        int g = (int)(A.getGreen()* alpha + (1-alpha) * B.getGreen());
        return new Color(r,g,b,(int)(alpha*255));
    }
}