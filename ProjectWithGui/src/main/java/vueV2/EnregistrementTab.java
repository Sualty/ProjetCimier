package vueV2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by blou on 22/08/16.
 */
public class EnregistrementTab extends JPanel{

    private JCheckBox checkbox_ac;
    private JCheckBox checkbox_amp;
    private JCheckBox checkbox_log;
    private JRadioButton png_radio;
    private JRadioButton jpeg_radio;
    private JRadioButton txt_radio;
    private JRadioButton csv_radio;
    public EnregistrementTab() {
        super();
        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        JLabel enregistrement_choix = new JLabel("Que voulez-vous enregistrer ?");
        checkbox_ac = new JCheckBox("Graphe du courant actif");
        checkbox_amp = new JCheckBox("Graphe de l'amplitude du courant");
        checkbox_log = new JCheckBox("Fichier de log");

        JLabel img_choix_enr = new JLabel("Images au format...");
        png_radio = new JRadioButton(".png");
        jpeg_radio = new JRadioButton(".jpeg");

        JLabel log_choix_enr = new JLabel("Fichier de log au format...");
        txt_radio = new JRadioButton(".txt");
        csv_radio = new JRadioButton(".csv");

        this.add(enregistrement_choix, BorderLayout.WEST);
        this.add(checkbox_ac, BorderLayout.WEST);
        this.add(checkbox_amp,BorderLayout.WEST);
        this.add(checkbox_log,BorderLayout.WEST);
        this.add(img_choix_enr,BorderLayout.WEST);
        this.add(png_radio,BorderLayout.WEST);
        this.add(jpeg_radio,BorderLayout.WEST);
        this.add(log_choix_enr,BorderLayout.WEST);
        this.add(txt_radio,BorderLayout.WEST);
        this.add(csv_radio,BorderLayout.WEST);

        png_radio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                png_radio.setSelected(true);
                jpeg_radio.setSelected(false);
            }
        });

        jpeg_radio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jpeg_radio.setSelected(true);
                png_radio.setSelected(false);
            }
        });

        txt_radio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                txt_radio.setSelected(true);
                csv_radio.setSelected(false);
            }
        });

        csv_radio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                csv_radio.setSelected(true);
                txt_radio.setSelected(false);
            }
        });

        checkbox_log.setSelected(true);
        checkbox_amp.setSelected(true);
        checkbox_ac.setSelected(true);

        png_radio.setSelected(true);
        txt_radio.setSelected(true);
    }

    public boolean[] recapitultifEnregistrement() {
        boolean[] result = new boolean[7];
        result[0] = this.checkbox_ac.isSelected();
        result[1] = this.checkbox_amp.isSelected();
        result[2] = this.checkbox_log.isSelected();
        result[3] = this.png_radio.isSelected();
        result[4] = this.jpeg_radio.isSelected();
        result[5] = this.txt_radio.isSelected();
        result[6]  =this.csv_radio.isSelected();
        return result;
    }
}