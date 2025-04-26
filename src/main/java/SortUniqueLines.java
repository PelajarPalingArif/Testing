import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class SortUniqueLines {

    public static void main(String[] args) throws IOException {
        Path filePath = Paths.get("build.txt");

        // Read lines, deduplicate, sort
        List<String> sortedUniqueLines = Files.lines(filePath)
                .map(String::trim)
                .filter(line -> !line.isEmpty()) // optional: skip blank lines
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        // Overwrite the file with the cleaned list
        Files.write(filePath, sortedUniqueLines);

        System.out.println("Sorted, de-duped, and saved.");
    }
}
