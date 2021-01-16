package vartas.reddit;

public class Comment extends CommentTOP{
    @Override
    public Comment getRealThis() {
        return this;
    }
}
