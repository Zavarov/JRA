package vartas.jra;

import org.json.JSONObject;
import vartas.jra._json.JSONComment;
import vartas.jra.types.Thing;
import vartas.jra.types._factory.ThingFactory;

public class Comment extends CommentTOP{

    public Thing toThing(){
        return ThingFactory.create(Thing.Kind.Comment.toString(), JSONComment.toJson(this, new JSONObject()));
    }

    @Override
    public Comment getRealThis() {
        return this;
    }
}
