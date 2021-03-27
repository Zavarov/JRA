package proj.zav.jra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractTest {
    protected static final Path JSON_PATH = Paths.get("src", "test", "resources", "json");
    protected Path TARGET_PATH;

    public String readJson(String fileName) throws IOException {
        assert TARGET_PATH != null;
        return Files.readString(TARGET_PATH.resolve(fileName));
    }
}
