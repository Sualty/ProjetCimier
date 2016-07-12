import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/** Assumes UTF-8 encoding. JDK 7+. */
public class parserXML {

    public static void main(String... aArgs) throws IOException {
        parserXML parser = new parserXML("C:\\Users\\Gunsillie\\Documents\\Random Algo\\ProjetCimier\\test.txt");
        parser.processLineByLine();
        log("Done.");
    }

    /**
     Constructor.
     @param aFileName full name of an existing, readable file.
     */
    public parserXML(String aFileName){
        fFilePath = Paths.get(aFileName);
    }


    /** Template method that calls {@link #processLine(String)}.  */
    public final void processLineByLine() throws IOException {
        try (Scanner scanner =  new Scanner(fFilePath, ENCODING.name())){
            while (scanner.hasNextLine()){
                processLine(scanner.nextLine());
            }
        }
    }


    protected void processLine(String aLine){
        //use a second Scanner to parse the content of each line
        if (aLine.equals("</parameters>")== false && aLine.equals("<parameters>")==false) {
            Scanner scanner = new Scanner(aLine);
            scanner.useDelimiter("\"");
            if (scanner.hasNext()) {
                scanner.next();
                String nom_param = scanner.next();
                scanner.next();
                String val_param = scanner.next();
            } else {
                log("Empty or invalid line. Unable to process.");
            }
        }
    }

    // PRIVATE
    private final Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    private static void log(Object aObject){
        System.out.println(String.valueOf(aObject));
    }

    private String quote(String aText){
        String QUOTE = "'";
        return QUOTE + aText + QUOTE;
    }
}
