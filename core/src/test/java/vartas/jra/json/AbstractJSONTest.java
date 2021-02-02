package vartas.jra.json;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AbstractJSONTest {
    protected static Path jsonDirectory = Paths.get("src", "test", "resources", "json");

    public static JSONObject from(String filename) throws IOException {
        Path filePath = jsonDirectory.resolve(filename);
        String content = Files.readString(filePath);
        return new JSONObject(content);
    }
}
