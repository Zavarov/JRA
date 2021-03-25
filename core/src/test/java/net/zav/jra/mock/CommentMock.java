package net.zav.jra.mock;

import net.zav.jra.models.AbstractComment;
import net.zav.jra.models.Kind;
import net.zav.jra.models.Thing;
import net.zav.jra.models._json.JSONAbstractComment;

public class CommentMock extends AbstractComment {
    public static CommentMock from(Thing thing){
        assert thing.getKind() == Kind.Comment;

        CommentMock target = new CommentMock();
        JSONAbstractComment.fromJson(target, thing.getData().toString());
        return target;
    }
}
