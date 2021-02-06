package vartas.jra.types;

import org.json.JSONObject;
import vartas.jra.types._factory.ThingFactory;
import vartas.jra.types._json.JSONListing;

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
