package vartas.reddit.query;

import org.json.JSONObject;
import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.types.$factory.ListingFactory;
import vartas.reddit.types.$factory.ThingFactory;
import vartas.reddit.types.Listing;
import vartas.reddit.types.Thing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class QueryMany<T, Q extends QueryBase<List<T>,Q>> extends QueryBase<List<T>, Q> {
    protected final Function<Thing, T> mapper;
    public QueryMany(Function<Thing, T> mapper, Client client, Endpoint endpoint, Object... substitute) {
        super(client, endpoint, substitute);
        this.mapper = mapper;
    }

    @Override
    public List<T> query() throws IOException, HttpException, InterruptedException{
        JSONObject response = new JSONObject(client.get(params, endpoint, args));

        Thing thing = ThingFactory.create(Thing::new, response);

        //We should've received a collection of things
        assert Thing.Kind.Listing.matches(thing);

        Listing listing = ListingFactory.create(Listing::new, thing.getData());
        List<T> result = new ArrayList<>(listing.getChildren().size());

        for(Thing child : listing.getChildren()) {
            result.add(mapper.apply(child));
        }

        return Collections.unmodifiableList(result);
    }
}
