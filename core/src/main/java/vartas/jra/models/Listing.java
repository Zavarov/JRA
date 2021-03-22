package vartas.jra.models;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Streams;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.jra.models._factory.ThingFactory;
import vartas.jra.models._json.JSONListing;
import vartas.jra.query.QueryGet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Listing<V> extends ListingTOP implements Iterable<V>{
    private final Function<String, V> mapper;

    public Listing(Function<String,V> mapper){
        this.mapper = mapper;
    }

    @Override
    public Listing<V> getRealThis() {
        return this;
    }

    public Thing toThing(){
        return ThingFactory.create(Kind.Listing, JSONListing.toJson(this, new JSONObject()));
    }

    @Nonnull
    @Override
    public java.util.Iterator<V> iterator() {
        return getChildren().stream().map(Object::toString).map(mapper).iterator();
    }

    public static class Iterator<V extends Snowflake> extends AbstractIterator<Listing<V>>{
        @Nonnull
        private static final Logger LOGGER = LoggerFactory.getLogger(Iterator.class);
        @Nonnull
        private final QueryGet<Listing<V>> query;
        @Nullable
        private Listing<V> current;

        public Iterator(@Nonnull QueryGet<Listing<V>> query){
            this.query = query;
        }

        public static <V extends Snowflake> Iterator<V> from(QueryGet<Listing<V>> query){
            return new Iterator<>(query);
        }

        @Override
        protected Listing<V> computeNext() {
            //No more pages remain
            if(current != null && current.isEmptyAfter() && current.isEmptyBefore()) {
                LOGGER.info("No more pages remain.");
                return endOfData();
            }

            //Request next page
            try {
                current = query.query();
                LOGGER.info("Received page with {} element(s).", current.sizeChildren());

                current.ifPresentAfter(after -> {
                    LOGGER.debug("Update 'after' to {} for the next page.", after);
                    query.setParameter("after", after);
                });

                current.ifPresentBefore(before -> {
                    LOGGER.debug("Update 'before' to {} for the next page.", before);
                    query.setParameter("before", before);
                });

                return current;
            }catch(IOException | InterruptedException e){
                LOGGER.warn(e.getMessage(), e);
                return endOfData();
            }
        }

        public Stream<V> toStream(){
            int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SORTED | Spliterator.DISTINCT;
            Spliterator<Listing<V>> spliterator = Spliterators.spliteratorUnknownSize(this, characteristics);

            return StreamSupport.stream(spliterator, false).flatMap(Streams::stream);
        }
    }
}
