package vartas.jra.models;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import vartas.jra.exceptions.HttpException;
import vartas.jra.models._factory.ThingFactory;
import vartas.jra.models._json.JSONListing;
import vartas.jra.query.QueryGet;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.function.Function;

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
        private final QueryGet<Listing<V>> query;

        public Iterator(QueryGet<Listing<V>> query){
            this.query = query;
        }

        public static <V extends Snowflake> Iterator<V> from(QueryGet<Listing<V>> query){
            return new Iterator<>(query);
        }

        @Override
        protected Listing<V> computeNext() {
            try {
                Listing<V> listing = query.query();

                if (listing.isEmptyChildren()) {
                    return endOfData();
                } else {
                    //The last element of the current query serves
                    //as an anchor point for the next query
                    String after = Iterables.getLast(listing).getName();
                    query.setParameter("after", after);
                    return listing;
                }
            }catch(IOException | HttpException | InterruptedException e){
                LoggerFactory.getLogger(getClass()).error(e.getMessage(), e);
                return endOfData();
            }
        }
    }
}
