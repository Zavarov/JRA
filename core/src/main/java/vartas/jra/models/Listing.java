package vartas.jra.models;

import com.google.common.collect.Lists;
import org.json.JSONObject;
import vartas.jra.models._factory.ThingFactory;
import vartas.jra.models._json.JSONListing;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
    public Iterator<V> iterator() {
        return getChildren().stream().map(Object::toString).map(mapper).iterator();
    }

    public List<V> asList(){
        return Collections.unmodifiableList(Lists.newArrayList(this));
    }
}
