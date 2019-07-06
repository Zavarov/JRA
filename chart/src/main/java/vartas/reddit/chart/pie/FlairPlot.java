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

import org.apache.commons.lang3.tuple.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import vartas.reddit.SubmissionInterface;

import java.awt.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class creates a plot over all flairs in the specified submissions. If
 * the submission is unflaired, it is described as such and if the amount of
 * flairs is less than 1% of all flairs, then they are added to an "other"
 * flair.
 */
public class FlairPlot implements Function<Collection<? extends SubmissionInterface>, JFreeChart>{
    /**
     * The alpha value of all the colors in the vartas.reddit.chart.
     */
    protected final double alpha;
    /**
     * @param alpha the alpha value of the colors.
     */
    public FlairPlot(double alpha){
        this.alpha = alpha;
    }
    /**
     * Creates a vartas.reddit.chart.pie vartas.reddit.chart showing the tags of all submissions.
     * @param submissions a collection over all submissions.
     * @return an image containing the vartas.reddit.chart.pie vartas.reddit.chart.
     */
    @Override
    public JFreeChart apply(Collection<? extends SubmissionInterface> submissions){
        return createFlairChart(countFlairs(submissions));
    }
    /**
     * Counts the frequency of all flairs and merges those that appeared less than 1% of the time
     * @param flairs all retrieved flairs.
     * @return a map over all flairs and their frequency.
     */
    private Map<String,Long> countFlairs(Collection<? extends SubmissionInterface> flairs){
        return flairs
                .stream()
                .map(SubmissionInterface::getLinkFlairText)
                .map(s -> s.orElse("unflaired"))
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()))
                .entrySet()
                .stream()
                .map(e -> (((double)e.getValue())/flairs.size()) >= 0.01 ? e : Pair.of("other",e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }
    /**
     * @param data the data extracted from the submission.
     * @return a vartas.reddit.chart.pie vartas.reddit.chart over all flairs of the submissions.
     */
    private JFreeChart createFlairChart(Map<String,Long> data){
        DefaultPieDataset dataset = new DefaultPieDataset();
        data.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .forEach(e -> dataset.setValue(e.getKey(), e.getValue()));
        JFreeChart chart = ChartFactory.createPieChart3D("Flairs", dataset, false, false, Locale.ENGLISH);
        //Add the amount of submissions to the label
        PieSectionLabelGenerator label = new StandardPieSectionLabelGenerator("{0} = {1} ({2})");
        
        PiePlot plot = (PiePlot)chart.getPlot();
        plot.setLabelGenerator(label);
        data.keySet().forEach(key -> {
            int hash = key.hashCode();
            int r = hash & 0xFF;
            int g = (hash>>8) & 0xFF;
            int b = (hash>>16) & 0xFF;
            plot.setSectionPaint(key, new Color(r,g,b,(int)(alpha*255)));
        });
        return chart;
    }
}