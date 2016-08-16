package vueV2;

import controleur.HistoriqueControleur;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by blou on 08/08/16.
 */
public class HistoriqueTab extends JPanel{

    BufferedImage actif_graphe;
    BufferedImage amplitude_graphe;
    HistoriqueControleur historiqueControleur;
    public HistoriqueTab(HistoriqueControleur historiqueControleur) {
        super();
        this.historiqueControleur = historiqueControleur;

        this.setLayout(new BorderLayout());

        //param panel
        JPanel param_panels = new JPanel();
        param_panels.setLayout(new BoxLayout(param_panels,BoxLayout.PAGE_AXIS));

        //sous-menu recherche par dates

        param_panels.add(Box.createRigidArea(new Dimension(0,20)));

        JLabel label_choix = new JLabel("Choisissez le type de recherche");
        JRadioButton rech_dates = new JRadioButton("Recherche par dates");

        param_panels.add(label_choix);
        param_panels.add(Box.createRigidArea(new Dimension(0,10)));
        param_panels.add(rech_dates);
        param_panels.add(Box.createRigidArea(new Dimension(0,10)));

        JLabel dates_label = new JLabel("Dates recherchées");//TODO
        JTextField dates_area = new JTextField();//TODO
        dates_area.setMinimumSize(new Dimension(300,30));
        dates_area.setMaximumSize(new Dimension(300,30));
        dates_area.setPreferredSize(new Dimension(300,30));
        dates_area.setEditable(true);
        dates_area.setAlignmentX(Component.LEFT_ALIGNMENT);

        param_panels.add(dates_label);
        param_panels.add(Box.createRigidArea(new Dimension(0,10)));
        param_panels.add(dates_area);

        param_panels.add(Box.createRigidArea(new Dimension(0,10)));

        // sous menu recherche par requete sql
        JRadioButton rech_sql = new JRadioButton("Recherche par requête SQL");
        param_panels.add(rech_sql);

        JTextField sql_area = new JTextField();//TODO
        sql_area.setMinimumSize(new Dimension(300,30));
        sql_area.setMaximumSize(new Dimension(300,30));
        sql_area.setPreferredSize(new Dimension(300,30));
        sql_area.setEditable(true);
        sql_area.setAlignmentX(Component.LEFT_ALIGNMENT);

        param_panels.add(sql_area);

        JLabel mesures_a_afficher_label = new JLabel("Données à afficher : ");//TODO
        JCheckBox actif_graph_checkbox = new JCheckBox("Graphe du courant actif",true);//TODO
        JCheckBox amplitude_graph_checkbox = new JCheckBox("Graphe de l'amplitude ",true);//TODO
        JCheckBox log_checkbox = new JCheckBox("Fichier de log",true);//TODO

        param_panels.add(mesures_a_afficher_label);

        param_panels.add(Box.createRigidArea(new Dimension(0,10)));

        param_panels.add(actif_graph_checkbox);
        param_panels.add(amplitude_graph_checkbox);
        param_panels.add(log_checkbox);

        JButton button_launch_research = new JButton("Lancer la recherche");
        button_launch_research.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(rech_dates.isSelected()) {
                    String d = dates_area.getText();
                    if(historiqueControleur.parseDateString(d)) {
                        historiqueControleur.rechercheDates(d);
                    }
                    else {
                        //po up windows ?
                    }
                }
                else {
                    String sql = sql_area.getText();
                    historiqueControleur.rechercheSQL(sql);
                }
            }
        });
        param_panels.add(button_launch_research);//TODO

        String	listData[] =
                {
                        "Item 1",
                        "Item 2",
                        "Item 3",
                        "Item 4"
                };

        JList list_results = new JList(listData);
        list_results.setMaximumSize(new Dimension(300,300));
        list_results.setMinimumSize(new Dimension(300,300));
        list_results.setPreferredSize(new Dimension(300,300));


        JScrollPane scroll_list = new JScrollPane();//TODO
        scroll_list.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_list.setSize(300,300);
        scroll_list.setViewportView(list_results);
        param_panels.add(scroll_list,BorderLayout.SOUTH);

        //init
        rech_dates.setSelected(true);
        //deselecting the other choice
        rech_sql.setSelected(false);
        sql_area.setVisible(false);
        //selecting the actual choice
        dates_label.setVisible(true);
        dates_area.setVisible(true);
        mesures_a_afficher_label.setVisible(true);
        actif_graph_checkbox.setVisible(true);
        amplitude_graph_checkbox.setVisible(true);
        log_checkbox.setVisible(true);

        //listeners on radiobuttons
        rech_dates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    rech_dates.setSelected(true);
                    //deselecting the other choice
                    rech_sql.setSelected(false);
                    sql_area.setVisible(false);
                    //selecting the actual choice
                    dates_label.setVisible(true);
                    dates_area.setVisible(true);
            }
        });

        rech_sql.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                rech_sql.setSelected(true);
                //deselecting the other choice
                rech_dates.setSelected(false);
                sql_area.setVisible(true);
                //selecting the actual choice
                dates_label.setVisible(false);
                dates_area.setVisible(false);
            }
        });
        //graphs panel
        JPanel graphs_panels = new JPanel();
        graphs_panels.setLayout(new BorderLayout());

        //init imgs
        try {
            this.actif_graphe = ImageIO.read(new File("blank.png"));//TODO
            this.amplitude_graphe = ImageIO.read(new File("blank2.png"));//TODO
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel actif_label = new JLabel(new ImageIcon(actif_graphe));
        JLabel amplitude_label = new JLabel(new ImageIcon(amplitude_graphe));

        graphs_panels.add(actif_label,BorderLayout.WEST);
        graphs_panels.add(amplitude_label,BorderLayout.EAST);

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

        actif_graph_checkbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if(actif_graph_checkbox.isSelected()) {
                    actif_label.setVisible(true);
                }
                else {
                    actif_label.setVisible(false);
                }
            }
        });

        amplitude_graph_checkbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if(amplitude_graph_checkbox.isSelected()) {
                    amplitude_label.setVisible(true);
                }
                else {
                    amplitude_label.setVisible(false);
                }
            }
        });

        log_checkbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if(log_checkbox.isSelected()) {
                    scroll.setVisible(true);
                }
                else {
                    scroll.setVisible(false);
                }
            }
        });
    }
}
