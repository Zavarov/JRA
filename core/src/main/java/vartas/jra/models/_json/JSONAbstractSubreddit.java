package vartas.jra.models._json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.jra.models.AbstractSubreddit;
import vartas.jra.models.Kind;
import vartas.jra.models.Thing;

import java.util.List;
import java.util.function.Supplier;

public class JSONAbstractSubreddit extends JSONAbstractSubredditTOP{
    public static <T extends AbstractSubreddit> T fromThing(String source, Supplier<T> supplier){
        return fromThing(JSONThing.fromJson(source), supplier);
    }

    public static <T extends AbstractSubreddit> T fromThing(Thing source, Supplier<T> supplier){
        assert source.getKind() == Kind.Subreddit;

        T target = supplier.get();
        fromJson(target, source.getData().toString());
        return target;
    }

    @Override
    protected void $fromHeaderSize(JSONObject source, AbstractSubreddit target){
        JSONArray bounds = source.optJSONArray(HEADERSIZE);
        if(bounds != null) {
            for(int i = 0 ; i < bounds.length() ; ++i)
                target.addHeaderSize(bounds.getInt(i));
        }
    }

    @Override
    protected void $toHeaderSize(AbstractSubreddit source, JSONObject target){
        List<Integer> bounds = source.getHeaderSize();
        if(!bounds.isEmpty())
            target.put(HEADERSIZE, new JSONArray(bounds));
    }
}
