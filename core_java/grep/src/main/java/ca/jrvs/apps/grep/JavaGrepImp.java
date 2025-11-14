package ca.jrvs.apps.grep;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaGrepImp implements JavaGrep{

    private String regex;
    private String rootPath;
    private String outFile;

    final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("USAGE: JavaGrepImp regex rootPath outFile");
            System.exit(1);
        }

        JavaGrepImp javaGrep = new JavaGrepImp();
        javaGrep.setRegex(args[0]);
        javaGrep.setRootPath(args[1]);
        javaGrep.setOutFile(args[2]);

        try{
            javaGrep.process();
        }
        catch (Exception e){
            javaGrep.logger.error("Error executing JavaGrep: ", e);
        }
    }
    /**
     * Top level search workflow
     *
     * @throws IOException
     */
    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();

        for(File file : listFiles(rootPath)) {
            for(String line : readLines(file)) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    /**
     * Traverse a given directory and return all files
     *
     * @param rootDir input directory
     * @return files under the rootDir
     */
    @Override
    public List<File> listFiles(String rootDir) {
        List<File> fileList = new ArrayList<>();
        File root = new File(rootDir);

        File[] files = root.listFiles();
        if (files == null) {
            return fileList;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                fileList.addAll(listFiles(file.getAbsolutePath()));
            }
            else {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * Read a file and return all the lines
     * <p>
     * Explain FileReader, BufferedReader, and character encoding
     *
     * @param inputFile file to be read
     * @return lines
     * @throws IllegalArgumentException if a given inputFile is not a file
     */
    @Override
    public List<String> readLines(File inputFile) {
        if (!inputFile.isFile()) {
            throw new IllegalArgumentException(inputFile + " is not a file");
        }
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(inputFile.toPath(), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        catch (IOException e) {
            logger.error("Error reading file: " + inputFile.getAbsolutePath(), e);
        }
        return lines;

    }

    /**
     * check if a line contains the regex pattern (passed by the user)
     *
     * @param line input string
     * @return true if there is a match
     */
    @Override
    public boolean containsPattern(String line) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    /**
     * Write lines to a file
     * <p>
     * Explore: FileOutputStream, OutputStreamWriter, and BufferedWriter
     *
     * @param lines matched line
     * @throws IOException if write failed
     */
    @Override
    public void writeToFile(List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(outFile)), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
        catch (IOException e) {
            logger.error("Error writing to file: " + outFile, e);
            throw e;
        }
    }

    /**
     * @return
     */
    @Override
    public String getRootPath() {
        return rootPath;
    }

    /**
     * @param rootPath
     */
    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * @return
     */
    @Override
    public String getRegex() {
        return regex;
    }

    /**
     * @param regex
     */
    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * @return
     */
    @Override
    public String getOutFile() {
        return outFile;
    }

    /**
     * @param outFile
     */
    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }
}
