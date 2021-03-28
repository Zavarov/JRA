package zav.jra;

import zav.jra._json.JSONComment;
import zav.jra.models.Kind;
import zav.jra.models.Thing;

public class Comment extends CommentTOP{
    public static Comment from(Thing thing){
        assert thing.getKind() == Kind.Comment;

        Comment target = new Comment();
        JSONComment.fromJson(target, thing.getData().toString());
        return target;
    }

    @Override
    public Comment getRealThis() {
        return this;
    }
}
