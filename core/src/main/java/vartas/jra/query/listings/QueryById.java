package vartas.jra.query.listings;

import com.google.common.base.Joiner;
import org.json.JSONObject;
import vartas.jra.Client;
import vartas.jra.Endpoint;
import vartas.jra.Link;
import vartas.jra.exceptions.HttpException;
import vartas.jra.http.APIRequest;
import vartas.jra.query.Query;
import vartas.jra.types.Listing;
import vartas.jra.types.Thing;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class QueryById extends Query<List<Link>, QueryById> {
    public QueryById(Client client, String... names){
        super(client, Endpoint.GET_BY_ID, Joiner.on(",").join(names));
    }

    @Override
    protected QueryById getRealThis() {
        return this;
    }

    @Override
    public List<Link> query() throws InterruptedException, IOException, HttpException {
        String source = new APIRequest.Builder(client)
                .setParams(params)
                .setEndpoint(endpoint)
                .setArgs(args)
                .build()
                .get();
        JSONObject response = new JSONObject(source);

        Listing listing = Thing.from(response).toListing();

        return listing.getChildren().stream().map(Thing::toLink).collect(Collectors.toUnmodifiableList());
    }
}
