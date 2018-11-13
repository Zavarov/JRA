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
package vartas.reddit.stats.chart.line;

import com.google.common.collect.AbstractIterator;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;

/**
 * This class is a frame for all graphs, that show a change over time. <br>
 * This change can be a change over days, months and even years.
 * @author u/Zavarov
 * @param <T> the datatype of all distinct elements
 */
public abstract class DevelopmentChart<T>{
    /**
     * The default timezone that is used in the program.
     */
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    /**
     * The dataset that contains the data for all years, months and days.
     */
    protected TimeSeriesCollection dataset;
    /**
     * Creates a line chart over the given data.<br>
     * The chart will contain a line containing the change in entire years,
     * months and days.
     * @param data the data that is used to fill the dataset.
     * @param interval the interval between each step.
     * @return a line chart representing the data.
     */
    public JFreeChart create(Map<Instant, List<T>> data, Interval interval){
        dataset = new TimeSeriesCollection();
        
        TimeSeries series = new TimeSeries(interval.toString());
        OffsetDateTime before = Instant.ofEpochMilli(
                data
                    .keySet()
                    .stream()
                    .mapToLong(Instant::toEpochMilli)
                    .max()
                    .orElse(0))
                .atOffset(ZoneOffset.UTC).plusDays(1);
        OffsetDateTime after = Instant.ofEpochMilli(
                data
                    .keySet()
                    .stream()
                    .mapToLong(Instant::toEpochMilli)
                    .min()
                    .orElse(0))
                .atOffset(ZoneOffset.UTC);
        
        Iterator<OffsetDateTime> iterator = interval.getEntries().apply(before, after);
        OffsetDateTime current = after, next;
        while(iterator.hasNext()){
            next = iterator.next();
            series.add(interval.getTimePeriod(current.toInstant()), collect(data,next,current));
            current = next;
        }
        dataset.addSeries(series);
        JFreeChart chart =  ChartFactory.createTimeSeriesChart(getTitle(), getXLabel(), getYLabel(), dataset,false,false,false);
        //Set the default language of the months to English.
        ((DateAxis)chart.getXYPlot().getDomainAxis()).setLocale(Locale.ENGLISH);
        //Normalize the values
        chart.getXYPlot().getRangeAxis().setLowerBound(0);
        return chart;
    }
    /**
     * @param data the map containing all values for the time frame.
     * @param end the time stamp of the newest submissions.
     * @param start the time stamp of the oldest submission.
     * @return the y value for all days in the interval.
     */
    protected long collect(Map<Instant,List<T>> data, OffsetDateTime end, OffsetDateTime start){
        Set<T> accumulated = new HashSet<>();
        while(start.isBefore(end)){
            if(data.containsKey(start.toInstant()))
                accumulated.addAll(data.get(start.toInstant()));
            start = start.plusDays(1);
        }
        return count(accumulated);
    }
    /**
     * @return the title of the chart. 
     */
    protected abstract String getTitle();
    /**
     * @return the type of values on the y axis. 
     */
    protected abstract String getYLabel();
    /**
     * @return the type of values on the x axis. 
     */
    protected String getXLabel(){
        return "Time";
    }
    /**
     * @param data all elements in the given interval.
     * @return the number representing those elements.
     */
    protected abstract long count(Collection<T> data);
    /**
     * The supported intervals.
     */
    public static enum Interval{
        /**
         * Step size of a day.
         */
        DAY(d -> d.plusDays(1), t -> new Day(t, UTC, Locale.ENGLISH)),
        /**
         * Step size of a year.
         */
        WEEK(d -> d.plusWeeks(1), t -> new Week(t, UTC, Locale.ENGLISH)),
        /**
         * Step size of a month.
         */
        MONTH(d -> d.plusMonths(1).withDayOfMonth(1), t -> new Month(t, UTC, Locale.ENGLISH)),
        /**
         * Step size of a year.
         */
        YEAR(d -> d.plusYears(1).withDayOfYear(1), t -> new Year(t, UTC, Locale.ENGLISH));
        /**
         * The iterator over all elements in the given interval with respect to
         * the step size.
         */
        private final EntryFunction entries;
        /**
         * Maps the timestamp to the respective day, week, month or year.
         */
        private final Function<Date,RegularTimePeriod> period;
        /**
         * @return the function that creates the iterator.
         */
        public EntryFunction getEntries(){
            return entries;
        }
        /**
         * @param time the current time stamp.
         * @return the time period the current instance is in. 
         */
        public RegularTimePeriod getTimePeriod(Instant time){
            return period.apply(Date.from(time));
        }
        /**
         * @return the name of this interval where only the first letter is capitalized. 
         */
        @Override
        public String toString(){
            String source = name();
            return source.substring(0,1).toUpperCase() + source.substring(1).toLowerCase();
        }
        /**
         * @param next the function that returns the next timestamp that follows from the current one.
         * @param period the function that creates the time periods from each date.
         */
        private Interval(Function<OffsetDateTime,OffsetDateTime> next, Function<Date,RegularTimePeriod> period){
            this.entries = new EntryFunction(next);
            this.period = period;
        }
    }
    /**
     * This function creates an iterator that visits all time steps in the given interval.
     */
    public static class EntryFunction implements BiFunction<OffsetDateTime,OffsetDateTime,Iterator<OffsetDateTime>>{
        /**
         * The function that returns the next timestamp that follows from the current one.
         */
        protected final Function<OffsetDateTime,OffsetDateTime> next;
        /**
         * @param next The function that returns the next timestamp that follows from the current one. 
         */
        private EntryFunction(Function<OffsetDateTime,OffsetDateTime> next){
            this.next = next;
        }
        /**
         * @param before the time stamp of the newest submission.
         * @param after the time stamp of the oldest submission.
         * @return an iterator that visits all time stamps in between.
         */
        @Override
        public Iterator<OffsetDateTime> apply(OffsetDateTime before, OffsetDateTime after) {
            return new DateIterator(
                    before,
                    after,
                    next
            );
        }
    }
    /**
     * This class implements the iterator that will visit all timestamps in a given interval.
     */
    private static class DateIterator extends AbstractIterator<OffsetDateTime>{
        /**
         * The function that returns the next timestamp that follows from the current one.
         */
        protected final Function<OffsetDateTime,OffsetDateTime> next;
        /**
         * The time stamp of the newest submission.
         */
        protected final OffsetDateTime before;
        /**
         * The time stamp of the oldest submission.
         */
        protected final OffsetDateTime after;
        /**
         * The current time stamp.
         */
        protected OffsetDateTime current;
        /**
         * @param before the time stamp of the newest submission.
         * @param after the time stamp of the oldest submission.
         * @param next the function that returns the next timestamp that follows from the current one.
         */
        public DateIterator(OffsetDateTime before, OffsetDateTime after, Function<OffsetDateTime,OffsetDateTime> next){
            this.next = next;
            this.before = before;
            this.after = after;
            this.current = after;
        }
        /**
         * @return the next timestep or null, if we are already at the end.
         */
        @Override
        protected OffsetDateTime computeNext() {
            if(current.isEqual(before)){
                return endOfData();
            }
            current = next.apply(current).isAfter(before) ? before : next.apply(current);
            return current;
        }
    }
}