import org.jfree.ui.RefineryUtilities;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {


        Class.forName("com.mysql.jdbc.Driver");
        /*ConnectDatabase database = new ConnectDatabase("jdbc:mysql://localhost:3306/cimier?useSSL=false","root","ZUdug@H!");

        database.addDatas(34,"01-01-2016",3.0);
        database.addRecords("04-07-1994","16-33-00",KindOfData.ACTIVECURRENT);
        database.getIdOfRecord("04-07-1994","16-33-00",KindOfData.ACTIVECURRENT);
        database.accessDatas();
        database.accessRecords();
        database.emptyRecords();
        database.emptyDatas();*/

        GetDataFromUnidrive getter = new GetDataFromUnidrive("http://192.168.130.182/US/4.02/dynamic/readparval.xml");
        getter.displayCurrent();

        List<Double> valeurs = getter.getListCurrent();
        List<Double> valeurs_magnitude = getter.getListCurrentMagnitude();

        DrawGraph graphe = new DrawGraph("Courant actif", "Courant actif dans le moteur en fonction du temps","Courant actif (A)", valeurs);
        graphe.pack( );
        RefineryUtilities.centerFrameOnScreen( graphe );
        graphe.setVisible( true );

        DrawGraph graphe2 = new DrawGraph("Magnitude Current", "Magnitude du courant dans le moteur en fonction du temps","Magnitude du courant (A)", valeurs_magnitude);
        graphe2.pack( );
        RefineryUtilities.centerFrameOnScreen( graphe2 );
        graphe2.setVisible( true );




        graphe.saveGraph();
        graphe2.saveGraph();


    }
}
