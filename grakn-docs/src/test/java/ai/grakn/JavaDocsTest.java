package ai.grakn;

import groovy.util.Eval;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.fail;

public class JavaDocsTest {

    private static final Pattern TAG_JAVA =
            Pattern.compile(
                    "(id=\"java[0-9]+\">\\s*<pre>|```java)" +
                    "\\s*(.*?)\\s*" +
                    "(</pre>|```)", Pattern.DOTALL);

    private static String groovyPrefix;
    private int numFound = 0;

    @BeforeClass
    public static void loadGroovyPrefix() throws IOException {
        groovyPrefix = new String(Files.readAllBytes(Paths.get("src/test/java/ai/grakn/prefix.groovy")));
    }

    @Test
    public void testExamplesValidGroovy() throws IOException {
        File dir = new File("..");

        Collection<File> files =
                FileUtils.listFiles(dir, new RegexFileFilter(".*\\.md"), DirectoryFileFilter.DIRECTORY);

        files.forEach(this::assertFileValidGroovy);

        if (numFound < 10) {
            fail("Only found " + numFound + " Java examples. Perhaps the regex is wrong?");
        }
    }

    private void assertFileValidGroovy(File file) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            fail();
        }

        String contents = new String(encoded, StandardCharsets.UTF_8);

        Matcher matcher = TAG_JAVA.matcher(contents);

        String groovyString = groovyPrefix;

        while (matcher.find()) {
            String match = matcher.group(2);
            if (!match.trim().startsWith("-test-ignore")) {
                numFound += 1;
                groovyString += matcher.group(2) + "\n";
            }
        }

        assertGroovyStringValid(file.toString(), groovyString);
    }

    private void assertGroovyStringValid(String fileName, String groovyString) {
        try {
            Eval.me(groovyString.replaceAll("\\$", "\\\\\\$"));
        } catch (Exception e) {
            e.printStackTrace();
            compilationFail(fileName, groovyString, e.getMessage());
        }
    }

    private void compilationFail(String fileName, String groovyString, String error) {
        fail("Invalid Groovy in " + fileName + ":\n" + groovyString + "\nERROR:\n" + error);
    }
}
