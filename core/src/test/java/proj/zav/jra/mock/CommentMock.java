package zav.jra.mock;

import zav.jra.models.AbstractComment;
import zav.jra.models.Kind;
import zav.jra.models.Thing;
import zav.jra.models._json.JSONAbstractComment;

public class CommentMock extends AbstractComment {
    public static CommentMock from(Thing thing){
        assert thing.getKind() == Kind.Comment;

        CommentMock target = new CommentMock();
        JSONAbstractComment.fromJson(target, thing.getData().toString());
        return target;
    }
}
