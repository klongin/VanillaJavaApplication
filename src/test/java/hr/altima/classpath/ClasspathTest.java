package hr.altima.classpath;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import org.junit.jupiter.api.Test;

public class ClasspathTest {

    @Test
    public void utilSolution() {
        String path = "telekomdocuments\\in";

        File file = new File(path);
        String absolutePath = file.getAbsolutePath();

        System.out.println(absolutePath);

        assertTrue(absolutePath.endsWith("telekomdocuments\\in"));
    }
}
