/*
 * Copyright (c) 2019 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package vartas.reddit.api.comment.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import vartas.reddit.CommentInterface;

public class CommentPrettyPrinter {
    IndentPrinter printer;

    public CommentPrettyPrinter(IndentPrinter printer){
        this.printer = printer;
    }

    public String prettyprint(CommentInterface comment){
        printer.clearBuffer();

        printer.addLine("comment {");

        addAuthor(comment);
        addId(comment);
        addScore(comment);
        addSubreddit(comment);
        addSubmission(comment);
        addSubmissionTitle(comment);

        printer.addLine("}");

        return printer.getContent();
    }

    private void addAuthor(CommentInterface comment){
        printer.addLine(String.format("author = \"%s\"", comment.getAuthor()));
    }

    private void addId(CommentInterface comment){
        printer.addLine(String.format("id = \"%s\"", comment.getId()));
    }

    private void addSubreddit(CommentInterface comment){
        printer.addLine(String.format("subreddit = \"%s\"", comment.getSubreddit()));
    }

    private void addScore(CommentInterface comment){
        printer.addLine(String.format("score = %d", comment.getScore()));
    }

    private void addSubmissionTitle(CommentInterface comment){
        printer.addLine(String.format("submissionTitle = \"%s\"", comment.getSubmissionTitle()));
    }

    private void addSubmission(CommentInterface comment){
        printer.addLine(String.format("submission = \"%s\"", comment.getSubmission()));
    }
}
