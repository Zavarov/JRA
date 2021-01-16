package vartas.reddit.query;

import de.se_rwth.commons.Joiners;
import org.json.JSONObject;
import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.Link;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.types.$factory.ListingFactory;
import vartas.reddit.types.$factory.ThingFactory;
import vartas.reddit.types.Listing;
import vartas.reddit.types.Thing;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class QueryById extends Query<List<Link>, QueryById> {
    public QueryById(Client client, String... names){
        super(client, Endpoint.GET_BY_ID, Joiners.COMMA.join(names));
    }

    @Override
    protected QueryById getRealThis() {
        return this;
    }

    @Override
    public List<Link> query() throws InterruptedException, IOException, HttpException {
        JSONObject response = new JSONObject(client.get(params, Endpoint.GET_BY_ID, args));

        Thing thing = ThingFactory.create(Thing::new, response);

        //We should've received a collection of things
        assert Thing.Kind.Listing.matches(thing);

        Listing listing = ListingFactory.create(Listing::new, thing.getData());
        return listing.getChildren().stream().map(Thing::toLink).collect(Collectors.toUnmodifiableList());
    }
}
