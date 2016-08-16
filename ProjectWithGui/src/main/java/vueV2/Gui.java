package vueV2;

import controleur.AcquisitionControleur;
import controleur.HistoriqueControleur;

import javax.swing.*;
import java.awt.*;

/**
 * Created by blou on 08/08/16.
 */
public class Gui extends JFrame{


    private JTabbedPane tabbedPane;
    private		AcquisitionTab		panel1;
    private		HistoriqueTab		panel2;

    public Gui(AcquisitionControleur acquisitionControleur, HistoriqueControleur historiqueControleur) {
        setTitle( "Lecture Cimier" );
        setSize( 1000, 1000 );
        setBackground( Color.gray );

        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );
        getContentPane().add( topPanel );

        // Create the tab pages
        this.panel1 = new AcquisitionTab(acquisitionControleur);
        this.panel2 = new HistoriqueTab(historiqueControleur);

        // Create a tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab( "Acquisition", panel1 );
        tabbedPane.addTab( "Historique", panel2 );

        topPanel.add( tabbedPane, BorderLayout.CENTER );

        this.pack();//adapte l'interface aux composants internes
    }
}
