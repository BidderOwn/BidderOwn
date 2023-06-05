package site.bidderown.server.base.resolver;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PathResolver {
    private final String FIRST = "src";
    private List<String> paths;

    public Path resolve(String... path) {
        paths = new ArrayList<>(Arrays.asList("main", "resources"));
        paths.addAll(Arrays.asList(path));
        return Paths.get(FIRST, paths.toArray(String[]::new));
    }

    public String getImagePathString(String kind, String fileName) {
        return resolve("images", kind, fileName).toUri().getPath();
    }

    public String getResourcePathString(String... path) {
        return resolve(path).toUri().getPath();
    }
}
