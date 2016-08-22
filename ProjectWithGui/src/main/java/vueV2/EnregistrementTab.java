package vueV2;

import javax.swing.*;
import java.awt.*;

/**
 * Created by blou on 22/08/16.
 */
public class EnregistrementTab extends JPanel{

    public EnregistrementTab() {
        super();
        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        JLabel enregistrement_choix = new JLabel("Que voulez-vous enregistrer ?");

        Checkbox checkbox_ac = new Checkbox("Graphe du courant actif");
        Checkbox checkbox_amp = new Checkbox("Graphe de l'amplitude du courant");
        Checkbox checkbox_log = new Checkbox("Fichier de log");

        JLabel img_choix_enr = new JLabel("Images au format...");
        JRadioButton png_radio = new JRadioButton(".png");
        JRadioButton jpeg_radio = new JRadioButton(".jpeg");

        JLabel log_choix_enr = new JLabel("Fichier de log au format...");
        JRadioButton txt_radio = new JRadioButton(".txt");
        JRadioButton csv_radio = new JRadioButton(".csv");

        this.add(enregistrement_choix);
        this.add(checkbox_ac);
        this.add(checkbox_amp);
        this.add(checkbox_log);
        this.add(img_choix_enr);
        this.add(png_radio);
        this.add(jpeg_radio);
        this.add(log_choix_enr);
        this.add(txt_radio);
        this.add(csv_radio);
    }
}