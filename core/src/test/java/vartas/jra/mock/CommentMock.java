package vartas.jra.mock;

import vartas.jra.models.AbstractComment;
import vartas.jra.models.Kind;
import vartas.jra.models.Thing;
import vartas.jra.models._json.JSONAbstractComment;

public class CommentMock extends AbstractComment {
    public static CommentMock from(Thing thing){
        assert thing.getKind() == Kind.Comment;

        CommentMock target = new CommentMock();
        JSONAbstractComment.fromJson(target, thing.getData().toString());
        return target;
    }
}
