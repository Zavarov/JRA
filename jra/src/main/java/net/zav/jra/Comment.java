package net.zav.jra;

import net.zav.jra._json.JSONComment;
import net.zav.jra.models.Kind;
import net.zav.jra.models.Thing;

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
