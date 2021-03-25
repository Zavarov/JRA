package net.zav.jra.models._json;

import com.google.common.collect.Lists;
import net.zav.jra.models.AbstractLink;
import net.zav.jra.models.Submission;
import net.zav.jra.models.Thing;
import net.zav.jra.models._factory.SubmissionFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.function.Function;

public class JSONSubmission extends JSONSubmissionTOP{

    public static Submission from(String content, Function<Thing, ? extends AbstractLink> mapper){
        //For some godforsaken reason, /random either returns a random submission OR a list of links.
        try{
            return from(new JSONArray(content), mapper);
        }catch(JSONException ignored){
            return from(new JSONObject(content), mapper);
        }
    }

    private static Submission from(JSONObject source, Function<Thing, ? extends AbstractLink> mapper){
        List<? extends AbstractLink> links = Lists.newArrayList(JSONListing.fromThing(source, mapper));

        //Choose a random link
        int index = (int)(Math.random() * links.size());
        AbstractLink link = links.get(index);

        return SubmissionFactory.create(link);
    }

    private static Submission from(JSONArray source, Function<Thing, ? extends AbstractLink> mapper){
        //We receive an array consisting of two listings.
        //The first listing contains a randomly fetched submission
        //The second listing contains comments belonging to the fetched submission
        assert source.length() == 2;

        //Extract random link
        List<? extends AbstractLink> links = Lists.newArrayList(JSONListing.fromThing(source.getJSONObject(0), mapper));

        //Reddit should've returned only a single link
        assert links.size() == 1;
        AbstractLink link = links.get(0);

        //Extract comments, if present
        List<Thing> comments = Lists.newArrayList(JSONListing.fromThing(source.getJSONObject(0)));

        return SubmissionFactory.create(link, comments);
    }
}
