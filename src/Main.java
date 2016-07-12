import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
      /*  GetDataFromUnidrive getter = new GetDataFromUnidrive("http://129.111.0.136/US/1.21/dynamic/readparval.xml");
        String response = getter.getResponse();

        ParserXML parser = new ParserXML(response);
        parser.processLineByLine();
        String nom = parser.getNom_param();
        String val = parser.getVal_param();
        System.out.println(nom + " "+ val);
*/
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(-3);
        arr.add(10);
        DrawGraph graph = new DrawGraph(arr);
        graph.createAndShowGui();
    }
}
