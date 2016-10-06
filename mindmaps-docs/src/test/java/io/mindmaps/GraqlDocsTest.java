package io.mindmaps;

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

    private static final Pattern HTML_GRAQL =
            Pattern.compile("id=\"shell[0-9]+\">\\s*<pre>\\s*(.*?)\\s*</pre>", Pattern.DOTALL);

    private static final Pattern SHELL_GRAQL = Pattern.compile("\\s*>>> (.*?)\r?\n.*", Pattern.DOTALL);

    private int numFound = 0;

    @Test
    public void testExamplesValidSyntax() throws IOException {
        File dir = new File("..");

        Collection<File> files =
                FileUtils.listFiles(dir, new RegexFileFilter(".*\\.md"), DirectoryFileFilter.DIRECTORY);

        files.forEach(this::assertFileValidSyntax);

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

        Matcher matcher = HTML_GRAQL.matcher(contents);

        while (matcher.find()) {
            numFound += 1;

            String graqlString = matcher.group(1);

            try {
                assertStringValidSyntax(file.toString(), graqlString);
            } catch (IllegalArgumentException e) {
            }
        }
    }

    private void assertStringValidSyntax(String fileName, String graqlString) {
        Matcher shellMatcher = SHELL_GRAQL.matcher(graqlString);

        if (shellMatcher.matches()) {
            graqlString = shellMatcher.group(1);
        }

        try {
            Graql.parse(graqlString);
        } catch (IllegalArgumentException e1) {
            // Try and parse line-by-line instead
            String[] lines = graqlString.split("\n");

            try {
                if (lines.length > 1) {
                    for (String line : lines) {
                        Graql.parse(line);
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
