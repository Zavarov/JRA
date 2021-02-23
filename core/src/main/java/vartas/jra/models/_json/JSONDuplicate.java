package vartas.jra.models._json;

import com.google.common.collect.Lists;
import org.json.JSONArray;
import vartas.jra.models.AbstractLink;
import vartas.jra.models.Duplicate;
import vartas.jra.models.Thing;
import vartas.jra.models._factory.DuplicateFactory;

import java.util.List;
import java.util.function.Function;

public class JSONDuplicate extends JSONDuplicateTOP {
    public static Duplicate fromJson(String source, Function<Thing, ? extends AbstractLink> mapper){
        //TODO Integrate into the deserialization
        JSONArray response = new JSONArray(source);

        AbstractLink reference;
        List<AbstractLink> duplicates;

        //We receive an array consisting of two listings.
        //The first listing contains the original submission
        //The second listing contains duplicates. Duplicates are
        //created e.g. by cross-posting the original submission.
        assert response.length() == 2;

        //Extract source
        List<? extends AbstractLink> references = Lists.newLinkedList(JSONListing.fromThing(response.getJSONObject(0), mapper));

        //Reddit should've only returned a single submission
        assert references.size() == 1;
        reference = references.get(0);

        //Duplicates, if present / Wrapping it in an ArrayList is necessary due to the generic type
        duplicates = Lists.newLinkedList(JSONListing.fromThing(response.getJSONObject(1), mapper));

        return DuplicateFactory.create(reference, duplicates);
    }
}
