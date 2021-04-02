package zav.mc.gradle

import zav.mc.cd4code.Main
import java.nio.file.Path
import java.nio.file.Paths

generate("zav.jra.Account")
generate("zav.jra.Client")
generate("zav.jra.Comment")
generate("zav.jra.FrontPage")
generate("zav.jra.Link")
generate("zav.jra.Parameter")
generate("zav.jra.Preferences")
generate("zav.jra.Subreddit")

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
