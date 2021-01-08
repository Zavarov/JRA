package vartas.reddit.$json;

import vartas.reddit.Comment;
import vartas.reddit.Submission;

//#TODO Is there a way where we don't need this class?
public class $JSONComment extends Comment {
    private final Submission submission;

    public $JSONComment(Submission submission) {
        this.submission = submission;
    }

    @Override
    public Submission getSubmission() {
        return submission;
    }
}
