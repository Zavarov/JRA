package vartas.jra;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vartas.jra.$factory.SubmissionFactory;
import vartas.jra.types.Listing;
import vartas.jra.types.Thing;

import java.util.Collections;
import java.util.List;

public class Submission extends SubmissionTOP{

    public static Submission from(String content){
        //For some godforsaken reason, /random either returns a random submission OR a list of links.
        try{
            return from(new JSONArray(content));
        }catch(JSONException ignored){
            return from(new JSONObject(content));
        }
    }

    private static Submission from(JSONObject source){
        Listing links = Thing.from(source).toListing();

        //Choose a random link
        int index = (int)(Math.random() * links.getChildren().size());
        Link link = links.getChildren().get(index).toLink();

        return SubmissionFactory.create(link);
    }

    private static Submission from(JSONArray source){
        Link link;
        List<Thing> comments;

        //We receive an array consisting of two listings.
        //The first listing contains a randomly fetched submission
        //The second listing contains comments belonging to the fetched submission
        //Note that it might be the case that the comments are compressed
        assert source.length() == 2;

        //Extract random submissions
        Listing listing = Thing.from(source.getJSONObject(0)).toListing();
        List<Thing> children = listing.getChildren();

        //Reddit should've only returned a single submission
        assert children.size() == 1;

        link = children.get(0).toLink();

        //Extract comments, if present
        listing = Thing.from(source.getJSONObject(1)).toListing();
        comments = Collections.unmodifiableList(listing.getChildren());

        return SubmissionFactory.create(link, comments);
    }

    @Override
    public Submission getRealThis() {
        return this;
    }
}
