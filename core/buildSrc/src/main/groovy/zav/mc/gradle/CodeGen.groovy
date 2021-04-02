package zav.mc.gradle

import zav.mc.cd4code.Main
import java.nio.file.Path
import java.nio.file.Paths

generate("zav.jra.AbstractClient")
generate("zav.jra.exceptions.Exceptions")
generate("zav.jra.models.AbstractAccount")
generate("zav.jra.models.AbstractComment")
generate("zav.jra.models.AbstractLink")
generate("zav.jra.models.AbstractPreferences")
generate("zav.jra.models.AbstractSubreddit")
generate("zav.jra.models.Created")
generate("zav.jra.models.Duplicate")
generate("zav.jra.models.Karma")
generate("zav.jra.models.Listing")
generate("zav.jra.models.Messaging")
generate("zav.jra.models.Rule")
generate("zav.jra.models.Snowflake")
generate("zav.jra.models.Submission")
generate("zav.jra.models.SubredditSettings")
generate("zav.jra.models.Thing")
generate("zav.jra.models.TrendingSubreddits")
generate("zav.jra.models.Trophy")
generate("zav.jra.models.Types")
generate("zav.jra.models.Votable")
generate("zav.jra.models.VotableCreated")

def generate(String model){
    Path CURRENT = Paths.get("").toAbsolutePath();
    Path PARENT = CURRENT.getParent();

    Path MODELS_PATH = Paths.get("build", "codegen")
    Path LOCAL_MODELS_PATH = Paths.get("src", "main", "models")
    Path TEMPLATES_PATH = Paths.get("build", "codegen")
    Path LOCAL_TEMPLATES_PATH = Paths.get("src", "main", "templates")
    Path SOURCES_DIRECTORY = Paths.get("src", "main", "java")
    Path TARGET_DIRECTORY = Paths.get("build", "src", "main", "java")

    String[] args = new String[]{
            CURRENT.resolve(MODELS_PATH),
            CURRENT.resolve(LOCAL_MODELS_PATH),
            CURRENT.resolve(TEMPLATES_PATH),
            CURRENT.resolve(LOCAL_TEMPLATES_PATH),
            PARENT.resolve(SOURCES_DIRECTORY),
            CURRENT.resolve(TARGET_DIRECTORY),
            model
    }

    Main.main(args)
}
