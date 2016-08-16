package controleur;

import vueV2.Gui;

/**
 * Created by blou on 04/08/16.
 */
public class Main {
    public static void main(String args[])
    {
        AcquisitionControleur acquisitionControleur = new AcquisitionControleur();
        HistoriqueControleur historiqueControleur = new HistoriqueControleur();
        Gui mainFrame = new Gui(acquisitionControleur,historiqueControleur);
        mainFrame.setVisible( true );
    }
}
