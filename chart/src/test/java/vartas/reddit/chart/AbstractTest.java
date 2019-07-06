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

package vartas.reddit.chart;

import de.monticore.ModelingLanguageFamily;
import de.monticore.io.paths.ModelPath;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import vartas.reddit.api.comment._ast.ASTComment;
import vartas.reddit.api.comment._ast.ASTCommentArtifact;
import vartas.reddit.api.comment._parser.CommentParser;
import vartas.reddit.api.comment._symboltable.CommentLanguage;
import vartas.reddit.api.comment._symboltable.CommentSymbolTableCreator;
import vartas.reddit.api.submission._ast.ASTSubmission;
import vartas.reddit.api.submission._ast.ASTSubmissionArtifact;
import vartas.reddit.api.submission._parser.SubmissionParser;
import vartas.reddit.api.submission._symboltable.SubmissionLanguage;
import vartas.reddit.api.submission._symboltable.SubmissionSymbolTableCreator;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.fail;

public class AbstractTest {
    protected GlobalScope scope;
    protected SubmissionSymbolTableCreator submissionSymbolTableCreator;
    protected CommentSymbolTableCreator commentSymbolTableCreator;
    protected SubmissionParser submissionParser;
    protected CommentParser commentParser;
    protected List<ASTComment> comments;
    protected List<ASTSubmission> submissions;

    @Before
    public void setUp(){
        Log.initWARN();

        ModelPath modelPath = new ModelPath(Paths.get("src/test/resources"));

        SubmissionLanguage submissionLanguage = new SubmissionLanguage();
        CommentLanguage commentLanguage = new CommentLanguage();
        ModelingLanguageFamily language = new ModelingLanguageFamily();

        language.addModelingLanguage(submissionLanguage);
        language.addModelingLanguage(commentLanguage);


        scope = new GlobalScope(modelPath, language);

        ResolvingConfiguration resolving = new ResolvingConfiguration();
        resolving.addDefaultFilters(submissionLanguage.getResolvingFilters());
        resolving.addDefaultFilters(commentLanguage.getResolvingFilters());


        submissionSymbolTableCreator = new SubmissionSymbolTableCreator(resolving, scope);
        commentSymbolTableCreator = new CommentSymbolTableCreator(resolving, scope);

        submissionParser = submissionLanguage.getParser();
        commentParser = commentLanguage.getParser();

        comments = parseComments("src/test/resources/comments.com");
        submissions = parseSubmission("src/test/resources/submissions.sub");
    }

    protected List<ASTSubmission> parseSubmission(String fileName){
        try{
            Optional<ASTSubmissionArtifact> submission = submissionParser.parse(fileName);
            if(submissionParser.hasErrors())
                fail("The parser encountered errors while parsing "+fileName);
            if(!submission.isPresent())
                fail("The submission couldn't be parsed");

            ASTSubmissionArtifact astSubmission = submission.get();
            submissionSymbolTableCreator.createFromAST(astSubmission);

            return astSubmission.getSubmissionList();
        }catch(IOException e){
            fail(e.getMessage());
            return null;
        }
    }

    protected List<ASTComment> parseComments(String fileName){
        try{
            Optional<ASTCommentArtifact> comment = commentParser.parse(fileName);
            if(commentParser.hasErrors())
                fail("The parser encountered errors while parsing "+fileName);
            if(!comment.isPresent())
                fail("The submission couldn't be parsed");

            ASTCommentArtifact astComment = comment.get();
            commentSymbolTableCreator.createFromAST(astComment);

            return astComment.getCommentList();
        }catch(IOException e){
            fail(e.getMessage());
            return null;
        }
    }
}
