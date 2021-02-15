package vartas.jra.models._json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractJSONTest {
    static Path resources = Paths.get("src","test","resources","json");

    public static String getContent(String filename) throws IOException {
        return Files.readString(resources.resolve(filename));
    }
}
