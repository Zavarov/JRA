package vartas.reddit.$visitor;

import vartas.reddit.Comment;
import vartas.reddit.Submission;

public interface RedditVisitor extends RedditVisitorTOP{
    @Override
    default RedditVisitor getRealThis(){
        return this;
    }

    @Override
    default void traverse(Submission node){
        node.getRootComments().forEach(comment -> comment.accept(getRealThis()));
    }

    @Override
    default void traverse(Comment node){
        node.getReplies().forEach(reply -> reply.accept(getRealThis()));
    }
}
