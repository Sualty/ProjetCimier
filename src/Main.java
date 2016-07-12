import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
        GetDataFromUnidrive getter = new GetDataFromUnidrive("http://129.111.0.136/US/1.21/dynamic/readparval.xml");
        String response = getter.getResponse();

        ParserXML parser = new ParserXML(response);
        parser.processLineByLine();
        String nom = parser.getNom_param();
        String val = parser.getVal_param();
        System.out.println(nom + " "+ val);
    }
}
