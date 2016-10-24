package io.mindmaps;

import com.google.common.collect.ImmutableSet;
import io.mindmaps.graql.Graql;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.fail;

public class GraqlDocsTest {

    // TODO: Make these tests work with the templating language
    private static final ImmutableSet<String> IGNORE_FILES = ImmutableSet.of("graql-templating.md");

    private static final Pattern TAG_GRAQL =
            Pattern.compile(
                    "(id=\"shell[0-9]+\">\\s*<pre>|```graql)" +
                    "\\s*(.*?)\\s*" +
                    "(</pre>|```)", Pattern.DOTALL);

    private static final Pattern SHELL_GRAQL = Pattern.compile("^*>>>(.*?)$", Pattern.MULTILINE);

    private int numFound = 0;

    @Test
    public void testExamplesValidSyntax() throws IOException {
        File dir = new File("..");

        Collection<File> files =
                FileUtils.listFiles(dir, new RegexFileFilter(".*\\.md"), DirectoryFileFilter.DIRECTORY);

        files.stream()
                .filter(file -> !IGNORE_FILES.contains(file.getName()))
                .forEach(this::assertFileValidSyntax);

        if (numFound < 10) {
            fail("Only found " + numFound + " Graql examples. Perhaps the regex is wrong?");
        }
    }

    private void assertFileValidSyntax(File file) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            fail();
        }

        String contents = new String(encoded, StandardCharsets.UTF_8);

        Matcher matcher = TAG_GRAQL.matcher(contents);

        while (matcher.find()) {
            numFound += 1;

            String graqlString = matcher.group(2);

            assertCodeblockValidSyntax(file.toString(), graqlString);
        }
    }

    private void assertCodeblockValidSyntax(String fileName, String block) {
        Matcher shellMatcher = SHELL_GRAQL.matcher(block);

        if (shellMatcher.find()) {
            while (shellMatcher.find()) {
                String graqlString = shellMatcher.group(1);
                assertGraqlStringValidSyntax(fileName, graqlString);
            }
        } else {
            assertGraqlStringValidSyntax(fileName, block);
        }
    }

    private void assertGraqlStringValidSyntax(String fileName, String graqlString) {
        try {
            Graql.parse(graqlString);
        } catch (IllegalArgumentException e1) {
            // Try and parse line-by-line instead
            String[] lines = graqlString.split("\n");

            try {
                if (lines.length > 1) {
                    for (String line : lines) {
                        if (!line.isEmpty()) Graql.parse(line);
                    }
                } else {
                    syntaxFail(fileName, graqlString, e1.getMessage());
                }
            } catch (IllegalArgumentException e2) {
                syntaxFail(fileName, graqlString, e1.getMessage() + "\nOR\n" + e2.getMessage());
            }
        }
    }

    private void syntaxFail(String fileName, String graqlString, String error) {
        fail("Invalid syntax in " + fileName + ":\n" + graqlString + "\nERROR:\n" + error);
    }
}
