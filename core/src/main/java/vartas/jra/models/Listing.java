package vartas.jra.models;

import org.json.JSONObject;
import vartas.jra.models._factory.ThingFactory;
import vartas.jra.models._json.JSONListing;

import javax.annotation.Nonnull;

public class Listing extends ListingTOP{

    @Nonnull
    public Thing toThing(){
        return ThingFactory.create(Thing.Kind.Listing.toString(), JSONListing.toJson(this, new JSONObject()));
    }

    @Override
    public Listing getRealThis() {
        return this;
    }
}