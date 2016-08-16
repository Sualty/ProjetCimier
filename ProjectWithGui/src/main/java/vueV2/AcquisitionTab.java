package vueV2;

import controleur.AcquisitionControleur;
import modele.DynamicDataDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by blou on 08/08/16.
 */
public class AcquisitionTab extends JPanel{

    private DynamicDataDemo actif_graphe;
    private DynamicDataDemo amplitude_graphe;
    private AcquisitionControleur acquisitionControleur;
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public AcquisitionTab(AcquisitionControleur acquisitionControleur) {
        super();
        this.acquisitionControleur = acquisitionControleur;
        this.setLayout(new BorderLayout());

        //init graphes
        this.actif_graphe = new DynamicDataDemo("Courant actif","Courant actif (mA)","Temps (s)");
        this.amplitude_graphe = new DynamicDataDemo("Amplitude du courant","Amplitude (mA)","Temps (s)");

        //param panel
        JPanel param_panels = new JPanel();
        param_panels.setLayout(new BoxLayout(param_panels,BoxLayout.PAGE_AXIS));

        param_panels.add(Box.createRigidArea(new Dimension(0,20)));

        JLabel nb_mesures_label = new JLabel("Nombre de mesures par seconde : ");
        param_panels.add(nb_mesures_label);

        param_panels.add(Box.createRigidArea(new Dimension(0,10)));

        JSlider slider = new JSlider(1,10,1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);

        param_panels.add(slider);

        param_panels.add(Box.createRigidArea(new Dimension(0,40)));

        JLabel mesures_a_afficher_label = new JLabel("Données à afficher : ");
        JCheckBox actif_graph_checkbox = new JCheckBox("Graphe du courant actif",true);
        JCheckBox amplitude_graph_checkbox = new JCheckBox("Graphe de l'amplitude ",true);
        JCheckBox log_checkbox = new JCheckBox("Fichier de log",true);

        param_panels.add(mesures_a_afficher_label);

        param_panels.add(Box.createRigidArea(new Dimension(0,10)));

        param_panels.add(actif_graph_checkbox);
        param_panels.add(amplitude_graph_checkbox);
        param_panels.add(log_checkbox);

        param_panels.add(Box.createRigidArea(new Dimension(0,40)));

        JButton forcage_acq = new JButton("Lancer manuellement l'acquisition");

        param_panels.add(forcage_acq);

        //graphs panel
        JPanel graphs_panels = new JPanel();
        graphs_panels.setLayout(new BorderLayout());

        graphs_panels.add(this.actif_graphe,BorderLayout.WEST);
        graphs_panels.add(this.amplitude_graphe,BorderLayout.EAST);

        JTextArea log_file = new JTextArea(20,70);
        log_file.setEditable(false);
        log_file.setText(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi quam orci, bibendum in lobortis quis, \n" +
                        "maximus a justo. Sed lacus dui, finibus quis erat vitae, malesuada dictum mi. Curabitur sed urna \n" +
                        "non est sodales pretium. Nulla facilisi. Nunc vitae orci et lorem vulputate aliquet. Vestibulum \n" +
                        "efficitur, urna imperdiet lacinia vestibulum, lectus tellus porta eros, sit amet venenatis dolor \n" +
                        "odio nec purus. Phasellus at leo leo. Integer ultrices viverra ante, nec accumsan enim ultricies non. \n" +
                        "In posuere ligula quis urna semper, pretium mollis est consectetur. "
                        +
                        "non est sodales pretium. Nulla facilisi. Nunc vitae orci et lorem vulputate aliquet. Vestibulum \n" +
                        "efficitur, urna imperdiet lacinia vestibulum, lectus tellus porta eros, sit amet venenatis dolor \n" +
                        "odio nec purus. Phasellus at leo leo. Integer ultrices viverra ante, nec accumsan enim ultricies non. \n" +
                        "In posuere ligula quis urna semper, pretium mollis est consectetur. "
                        +
                        "non est sodales pretium. Nulla facilisi. Nunc vitae orci et lorem vulputate aliquet. Vestibulum \n" +
                        "efficitur, urna imperdiet lacinia vestibulum, lectus tellus porta eros, sit amet venenatis dolor \n" +
                        "odio nec purus. Phasellus at leo leo. Integer ultrices viverra ante, nec accumsan enim ultricies non. \n" +
                        "In posuere ligula quis urna semper, pretium mollis est consectetur. "
                        +
                        "non est sodales pretium. Nulla facilisi. Nunc vitae orci et lorem vulputate aliquet. Vestibulum \n" +
                        "efficitur, urna imperdiet lacinia vestibulum, lectus tellus porta eros, sit amet venenatis dolor \n" +
                        "odio nec purus. Phasellus at leo leo. Integer ultrices viverra ante, nec accumsan enim ultricies non. \n" +
                        "In posuere ligula quis urna semper, pretium mollis est consectetur. "
                        +
                        "non est sodales pretium. Nulla facilisi. Nunc vitae orci et lorem vulputate aliquet. Vestibulum \n" +
                        "efficitur, urna imperdiet lacinia vestibulum, lectus tellus porta eros, sit amet venenatis dolor \n" +
                        "odio nec purus. Phasellus at leo leo. Integer ultrices viverra ante, nec accumsan enim ultricies non. \n" +
                        "In posuere ligula quis urna semper, pretium mollis est consectetur. "
                        +
                        "non est sodales pretium. Nulla facilisi. Nunc vitae orci et lorem vulputate aliquet. Vestibulum \n" +
                        "efficitur, urna imperdiet lacinia vestibulum, lectus tellus porta eros, sit amet venenatis dolor \n" +
                        "odio nec purus. Phasellus at leo leo. Integer ultrices viverra ante, nec accumsan enim ultricies non. \n" +
                        "In posuere ligula quis urna semper, pretium mollis est consectetur. "
                        +
                        "non est sodales pretium. Nulla facilisi. Nunc vitae orci et lorem vulputate aliquet. Vestibulum \n" +
                        "efficitur, urna imperdiet lacinia vestibulum, lectus tellus porta eros, sit amet venenatis dolor \n" +
                        "odio nec purus. Phasellus at leo leo. Integer ultrices viverra ante, nec accumsan enim ultricies non. \n" +
                        "In posuere ligula quis urna semper, pretium mollis est consectetur. "
                        +
                        "non est sodales pretium. Nulla facilisi. Nunc vitae orci et lorem vulputate aliquet. Vestibulum \n" +
                        "efficitur, urna imperdiet lacinia vestibulum, lectus tellus porta eros, sit amet venenatis dolor \n" +
                        "odio nec purus. Phasellus at leo leo. Integer ultrices viverra ante, nec accumsan enim ultricies non. \n" +
                        "In posuere ligula quis urna semper, pretium mollis est consectetur. "
                        +
                        "non est sodales pretium. Nulla facilisi. Nunc vitae orci et lorem vulputate aliquet. Vestibulum \n" +
                        "efficitur, urna imperdiet lacinia vestibulum, lectus tellus porta eros, sit amet venenatis dolor \n" +
                        "odio nec purus. Phasellus at leo leo. Integer ultrices viverra ante, nec accumsan enim ultricies non. \n" +
                        "In posuere ligula quis urna semper, pretium mollis est consectetur. "
        );

        JScrollPane scroll = new JScrollPane();
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setSize(920,80);
        scroll.setViewportView(log_file);
        graphs_panels.add(scroll,BorderLayout.SOUTH);

        //adding panels to main panel
        this.add(param_panels,BorderLayout.WEST);
        this.add(Box.createRigidArea(new Dimension(40,0)),BorderLayout.CENTER);
        this.add(graphs_panels,BorderLayout.EAST);

        //setting the launch of acq.
        forcage_acq.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String etat_acq = forcage_acq.getText();
                if(etat_acq.contains("Lancer")) {
                    int nb_mesure = slider.getValue();

                    Thread launching= new Thread(new Runnable() {
                        @Override
                        public void run() {
                            forcage_acq.setText("Arrêter manuellement l'acquisition");
                            String s = null;
                            try {
                                s = acquisitionControleur.lancerAcquisition(nb_mesure,actif_graphe,amplitude_graphe);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            log_file.setText(s);
                        }
                    });
                    launching.start();
                }
                else {
                    acquisitionControleur.stopperAcquisition();
                    forcage_acq.setText("Lancer manuellement l'acquisition");
                }
            }
        });
    }
}
