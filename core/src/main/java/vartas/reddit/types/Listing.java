package vartas.reddit.types;

import org.json.JSONArray;
import vartas.reddit.types.$factory.ThingFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Listing extends ListingTOP{
    protected static final String BEFORE = "before";
    protected static final String AFTER = "after";
    protected static final String MOD_HASH = "modhash";
    protected static final String CHILDREN = "children";
    @Override
    public Optional<String> getBefore() {
        return Optional.ofNullable(getSource().optString(BEFORE));
    }

    @Override
    public Optional<String> getAfter() {
        return Optional.ofNullable(getSource().optString(AFTER));
    }

    @Override
    public String getModhash() {
        return getSource().getString(MOD_HASH);
    }

    @Override
    public List<Thing> getChildren() {
        JSONArray data = getSource().getJSONArray(CHILDREN);
        List<Thing> things = new ArrayList<>();

        for(int i = 0 ; i < data.length() ; ++i)
            things.add(ThingFactory.create(Thing::new, data.getJSONObject(i)));

        return Collections.unmodifiableList(things);
    }

    @Override
    public Listing getRealThis() {
        return this;
    }
}
