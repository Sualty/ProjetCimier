import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/** Assumes UTF-8 encoding. JDK 7+. */
public class ParserXML {
    private final static Charset ENCODING = StandardCharsets.UTF_8;
    private String chaine;
    private String nom_param;
    private String val_param;
    public static void main(String... aArgs) throws IOException {
        ParserXML parser = new ParserXML("<parameters>\n" +
                "<parameter name=\"1.21\" value=\"55\" dp=\"1\" text=\"5.5Hz\" />\n" +
                "<parameter name=\"1.24\" value=\"0\" dp=\"1\" text=\"0.0Hz\" />\n" +
                "</parameters>");
        parser.processLineByLine();
        log("Done.");
    }

    /**
     Constructor.
     */
    public ParserXML(String chaineXML){chaine=chaineXML;}


    /** Template method that calls {@link #processLine(String)}.  */
    public final void processLineByLine() throws IOException {
        try (Scanner scanner =  new Scanner(chaine)){
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
                nom_param = scanner.next();
                scanner.next();
                val_param = scanner.next();
            } else {
                log("Empty or invalid line. Unable to process.");
            }
        }
    }

    // PRIVATE

    private static void log(Object aObject){
        System.out.println(String.valueOf(aObject));
    }
    private String quote(String aText){
        String QUOTE = "'";
        return QUOTE + aText + QUOTE;
    }

    public String getNom_param() {
        return nom_param;
    }

    public String getVal_param() {
        return val_param;
    }
}
