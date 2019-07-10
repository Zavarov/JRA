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

package vartas.reddit.api.comment;

import de.monticore.ModelingLanguage;
import de.monticore.io.paths.ModelPath;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.se_rwth.commons.Files;
import vartas.reddit.CommentInterface;
import vartas.reddit.api.comment._ast.ASTCommentArtifact;
import vartas.reddit.api.comment._parser.CommentParser;
import vartas.reddit.api.comment._symboltable.CommentLanguage;
import vartas.reddit.api.comment._symboltable.CommentSymbolTableCreator;
import vartas.reddit.api.comment.prettyprint.CommentPrettyPrinter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class CommentHelper {

    public static void store(Collection<CommentInterface> comments, File target){
        try {
            CommentPrettyPrinter printer = new CommentPrettyPrinter(new IndentPrinter());

            StringBuilder fileContent = new StringBuilder();
            comments.stream().map(printer::prettyprint).forEach(fileContent::append);

            target.getParentFile().mkdirs();
            target.createNewFile();

            Files.writeToTextFile(new StringReader(fileContent.toString()), target);
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    public static List<? extends CommentInterface> parse(String filePath){
        try{
            CommentSymbolTableCreator symbolTableCreator = createSymbolTableCreator();

            CommentParser parser = new CommentParser();
            Optional<ASTCommentArtifact> comments = parser.parse(filePath);
            if(parser.hasErrors())
                throw new IllegalArgumentException("The parser encountered errors while parsing "+filePath);
            if(!comments.isPresent())
                throw new IllegalArgumentException("The comment file couldn't be parsed");

            ASTCommentArtifact ast = comments.get();
            symbolTableCreator.createFromAST(ast);

            return ast.getCommentList();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static GlobalScope createGlobalScope(){
        ModelPath path = new ModelPath(Paths.get(""));
        ModelingLanguage language = new CommentLanguage();
        return new GlobalScope(path, language);
    }

    private static CommentSymbolTableCreator createSymbolTableCreator(){
        GlobalScope globalScope = createGlobalScope();
        ResolvingConfiguration resolvingConfiguration = new ResolvingConfiguration();

        resolvingConfiguration.addDefaultFilters(globalScope.getResolvingFilters());

        return new CommentSymbolTableCreator(resolvingConfiguration, globalScope);
    }
}
