import org.jfree.ui.RefineryUtilities;

import java.util.List;
public class Main {

    public static void main(String[] args) throws Exception {
        GetDataFromUnidrive getter = new GetDataFromUnidrive("http://192.168.130.182/US/4.02/dynamic/readparval.xml");
        getter.displayCurrent();
        List<Double> valeurs = getter.getListCurrent();
        List<Double> valeurs_magnitude = getter.getListCurrentMagnitude();
        DrawGraph graphe = new DrawGraph("Active Current", "Courant dans le moteur en fonction du temps", valeurs);
        graphe.pack( );
        RefineryUtilities.centerFrameOnScreen( graphe );
        graphe.setVisible( true );

        DrawGraph graphe2 = new DrawGraph("Magnitude Current", "Courant dans le moteur en fonction du temps", valeurs_magnitude);
        graphe2.pack( );
        RefineryUtilities.centerFrameOnScreen( graphe2 );
        graphe2.setVisible( true );

    /*    ParserXML parser = new ParserXML(response);
        parser.processLineByLine();
        String nom = parser.getNom_param();
        String val = parser.getVal_param();
        System.out.println(nom + " "+ val);

        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(-3);
        arr.add(10);
        DrawGraph graph = new DrawGraph(arr);
        graph.createAndShowGui();*/
    }
}
