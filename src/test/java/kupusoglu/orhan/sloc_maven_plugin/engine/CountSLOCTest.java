package kupusoglu.orhan.sloc_maven_plugin.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

class CountSLOCTest {

    @TempDir
    Path baseDir;

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "// comment            | 0 | 0 | 1 | 2",
        "/* comment */         | 0 | 0 | 1 | 2",
        "/** javadoc */        | 0 | 1 | 0 | 2",
        "/* comment */ int i;  | 0 | 0 | 0 | 3",
        "/** javadoc */ int i; | 0 | 0 | 0 | 3"
    })
    void processSource_singleLineComment_countedByType(String line, String blank, String javaDoc, String comment, String code) throws IOException {
        writeSource("src/p/A.java", "package p;", line, "class A {}");

        String[] row = countAndFindRow("A.java");

        assertEquals(Arrays.asList(blank, javaDoc, comment, code), Arrays.asList(row).subList(3, 7));
    }

    // the base directory itself lies below a directory named "test"
    @ParameterizedTest
    @CsvSource({
        "src/main/java/A.java,             src",
        "src/test/java/A.java,             test",
        "src/integration-test/java/A.java, int-test"
    })
    void processSource_baseDirBelowTestDir_fileTypeFromSourcePath(String relativePath, String expected) throws IOException {
        baseDir = Files.createDirectories(baseDir.resolve("test/project"));
        writeSource(relativePath, "class A {}");

        assertEquals(expected, countAndFindRow("A.java")[2]);
    }

    @Test
    void processSource_defaultPackageAfterNamedPackage_noPackageName() throws IOException {
        writeSource("src/p/A.java", "package p;", "class A {}");
        writeSource("src/Z.java", "class Z {}");

        // the order of a file tree walk is not defined, so visit the files explicitly
        CountSLOC countSLOC = new CountSLOC(new SystemStreamLog(), baseDir.toString(), "src", "java", StandardCharsets.UTF_8, false, false, true);
        countSLOC.processSource(baseDir.resolve("src/p/A.java"));
        countSLOC.processSource(baseDir.resolve("src/Z.java"));
        countSLOC.done();

        assertEquals("", findRow(readSavedTable(), "Z.java")[0]);
    }

    // the saved table is UTF-8 whatever the encoding of the sources
    @ParameterizedTest
    @ValueSource(strings = {"UTF-8", "ISO-8859-1"})
    void processSource_nonAsciiPackage_readInSourceEncoding(String encoding) throws IOException {
        Charset charset = Charset.forName(encoding);
        Path file = Files.createDirectories(baseDir.resolve("src")).resolve("A.java");
        Files.write(file, Arrays.asList("package de.b\u00fccher;", "class A {}"), charset);

        CountSLOC countSLOC = new CountSLOC(new SystemStreamLog(), baseDir.toString(), "src", "java", charset, false, false, true);
        countSLOC.processSource(file);
        countSLOC.done();

        assertEquals("de.b\u00fccher", findRow(readSavedTable(), "A.java")[0]);
    }

    /**
     * Writes a source file below the base directory.
     *
     * @param relativePath  path of the file relative to the base directory
     * @param lines         lines of the file
     */
    private void writeSource(String relativePath, String... lines) throws IOException {
        Path file = baseDir.resolve(relativePath);
        Files.createDirectories(file.getParent());
        Files.write(file, Arrays.asList(lines), StandardCharsets.UTF_8);
    }

    /**
     * Counts all sources below the base directory and returns the table row of the given file.
     *
     * @param fileName  name of the file to look up
     */
    private String[] countAndFindRow(String fileName) throws IOException {
        CountSLOC countSLOC = new CountSLOC(new SystemStreamLog(), baseDir.toString(), "src", "java", StandardCharsets.UTF_8, false, false, true);
        Files.walkFileTree(baseDir.resolve("src"), countSLOC);
        countSLOC.done();
        return findRow(readSavedTable(), fileName);
    }

    /**
     * Reads the SLOC table saved to the base directory.
     */
    private String[] readSavedTable() {
        return Common.readTextFile(baseDir.resolve(Common.OUTPUT_SLOC_FILE), StandardCharsets.UTF_8).toArray(new String[0]);
    }

    /**
     * Finds the table row of the given file and returns its trimmed cells.
     *
     * @param table     lines of the SLOC table
     * @param fileName  name of the file to look up
     */
    static String[] findRow(String[] table, String fileName) {
        return Arrays.stream(table)
            .map(line -> Arrays.stream(line.split("\\|")).skip(1).map(String::trim).toArray(String[]::new))
            .filter(cells -> cells.length > 1 && fileName.equals(cells[1]))
            .findFirst()
            .orElseThrow(() -> new AssertionError(String.format("No row for %s in:%n%s", fileName, String.join("\n", table))));
    }

}
