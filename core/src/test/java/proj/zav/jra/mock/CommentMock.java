package proj.zav.jra.mock;

import proj.zav.jra.models.AbstractComment;
import proj.zav.jra.models.Kind;
import proj.zav.jra.models.Thing;
import proj.zav.jra.models._json.JSONAbstractComment;

public class CommentMock extends AbstractComment {
    public static CommentMock from(Thing thing){
        assert thing.getKind() == Kind.Comment;

        CommentMock target = new CommentMock();
        JSONAbstractComment.fromJson(target, thing.getData().toString());
        return target;
    }
}
