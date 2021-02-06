package vartas.jra;

import org.json.JSONObject;
import vartas.jra._json.JSONComment;
import vartas.jra.models.Thing;
import vartas.jra.models._factory.ThingFactory;

public class Comment extends CommentTOP{

    public Thing toThing(){
        return ThingFactory.create(Thing.Kind.Comment.toString(), JSONComment.toJson(this, new JSONObject()));
    }

    @Override
    public Comment getRealThis() {
        return this;
    }
}
