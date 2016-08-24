package vueV2;

import controleur.HistoriqueControleur;
import controleur.KindOfData;
import modele.DrawGraph;
import modele.ResultatRecherche;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by blou on 08/08/16.
 */
public class HistoriqueTab extends JPanel{

    DrawGraph actif_graphe;
    DrawGraph amplitude_graphe;
    HistoriqueControleur historiqueControleur;
    ArrayList<ResultatRecherche> liste_triplets;
    public HistoriqueTab(HistoriqueControleur historiqueControleur) {
        super();
        this.historiqueControleur = historiqueControleur;
        this.liste_triplets = new ArrayList<>();
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

        JLabel dates_label = new JLabel("Dates recherchées");
        JTextField dates_area = new JTextField();
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

        JTextField sql_area = new JTextField();
        sql_area.setMinimumSize(new Dimension(300,30));
        sql_area.setMaximumSize(new Dimension(300,30));
        sql_area.setPreferredSize(new Dimension(300,30));
        sql_area.setEditable(true);
        sql_area.setAlignmentX(Component.LEFT_ALIGNMENT);

        param_panels.add(sql_area);

        JLabel mesures_a_afficher_label = new JLabel("Données à afficher : ");
        JCheckBox actif_graph_checkbox = new JCheckBox("Graphe du courant actif",true);
        JCheckBox amplitude_graph_checkbox = new JCheckBox("Graphe de l'amplitude ",true);
        JCheckBox log_checkbox = new JCheckBox("Fichier de log",true);

        param_panels.add(mesures_a_afficher_label);

        param_panels.add(Box.createRigidArea(new Dimension(0,10)));

        param_panels.add(actif_graph_checkbox);
        param_panels.add(amplitude_graph_checkbox);
        param_panels.add(log_checkbox);

        JButton button_launch_research = new JButton("Lancer la recherche");

        param_panels.add(button_launch_research);

        JList list_results = new JList();
        list_results.setModel(new DefaultListModel());
        JScrollPane scroll_list = new JScrollPane();
        scroll_list.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_list.setSize(300,300);
        scroll_list.setViewportView(list_results);
        param_panels.add(scroll_list,BorderLayout.SOUTH);

        button_launch_research.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                liste_triplets = historiqueControleur.rechercheDates(dates_area.getText());
                ArrayList<String> list_results_string=new ArrayList<String>();
                for(int i=0;i<liste_triplets.size();i++) {
                    list_results_string.add(liste_triplets.get(i).getDate());
                }
                String[] dates_tab_list = new String[list_results_string.size()];
                dates_tab_list = list_results_string.toArray(dates_tab_list);
               for(int i=0;i<dates_tab_list.length;i++) {
                   ((DefaultListModel)list_results.getModel()).addElement(dates_tab_list[i]);
               }
            }
        });
        JButton export_button = new JButton("Exporter");
        param_panels.add(export_button);

        JButton supprimer_button = new JButton("Supprimer sélection");
        param_panels.add(supprimer_button);

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

        supprimer_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                ArrayList<String> list_selected = (ArrayList<String>)list_results.getSelectedValuesList();
                JPanel panel = new JPanel(new BorderLayout(5, 5));

                JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
                label.add(new JLabel("Mot de passe administrateur", SwingConstants.RIGHT));
                panel.add(label, BorderLayout.WEST);

                JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
                JPasswordField password = new JPasswordField();
                controls.add(password);
                panel.add(controls, BorderLayout.CENTER);

                JOptionPane.showConfirmDialog(param_panels, panel, "Demande de permission", JOptionPane.OK_CANCEL_OPTION);

                String s = new String(password.getPassword());

                if(s.equals(modele.configuration.Configuration.mdp_admin)) {
                    historiqueControleur.supprimer(list_selected);
                    int[] index_selected = list_results.getSelectedIndices();
                    int count=0;
                    for(int i : index_selected) {
                        DefaultListModel listModel = (DefaultListModel) list_results.getModel();
                        listModel.removeElementAt(i-count);
                        count++;
                    }
                }

            }
        });


        //graphs panel
        JPanel graphs_panels = new JPanel();
        graphs_panels.setLayout(new BorderLayout());

       this.actif_graphe = new DrawGraph("dtdt","ddfdf");
       this.amplitude_graphe = new DrawGraph("fdfsf","fqfq");


        graphs_panels.add(actif_graphe,BorderLayout.WEST);
        graphs_panels.add(amplitude_graphe,BorderLayout.EAST);

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
                    actif_graphe.setVisible(true);
                }
                else {
                    actif_graphe.setVisible(false);
                }
            }
        });

        amplitude_graph_checkbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if(amplitude_graph_checkbox.isSelected()) {
                    amplitude_graphe.setVisible(true);
                }
                else {
                    amplitude_graphe.setVisible(false);
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

        list_results.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                int[] indices = list_results.getSelectedIndices();
                if(!(indices.length==1 && indices[0]==-1)) {
                    ArrayList<ResultatRecherche> resultats_liste = new ArrayList<ResultatRecherche>();
                    for(int index:indices) {
                        resultats_liste.add(liste_triplets.get(index));
                    }

                    actif_graphe = new DrawGraph("Courant actif en fonction du temps","courant actif (mA)",resultats_liste, KindOfData.ACTIVECURRENT);
                    amplitude_graphe = new DrawGraph("Amplitude du courant en fonction du temps","Amplitude (mA)",resultats_liste,KindOfData.CURRENTMAGNITUDE);

                    graphs_panels.remove(actif_graphe);
                    graphs_panels.remove(amplitude_graphe);
                    graphs_panels.add(actif_graphe,BorderLayout.WEST);
                    graphs_panels.add(amplitude_graphe,BorderLayout.EAST);

                    String txt = historiqueControleur.createLog(resultats_liste);
                    log_file.setText(txt);
                    graphs_panels.revalidate();
                }
            }
        });

        button_launch_research.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(rech_dates.isSelected()) {
                    String d = dates_area.getText();
                    if(historiqueControleur.parseDateString(d)==null) {
                        historiqueControleur.rechercheDates(d);
                    }
                    else {
                        //po up windows ?
                    }
                }
                else {
                    String sql = sql_area.getText();
                    String result = historiqueControleur.rechercheSQL(sql);
                    log_file.setText(result);
                }
            }
        });

        export_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EnregistrementTab enregistrementTab = new EnregistrementTab();
                int input = JOptionPane.showConfirmDialog(param_panels, enregistrementTab, "Enregistrement", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                if(input == JOptionPane.OK_OPTION)
                {
                    boolean[] result = enregistrementTab.recapitultifEnregistrement();
                    if(result[0]==true) {
                        if(result[3]==true) {
                            actif_graphe.enregistrerGraphe("png");
                        }
                        else if(result[4]==true) {
                            actif_graphe.enregistrerGraphe("jpeg");
                        }
                    }
                    if(result[1]==true) {
                        if(result[3]==true) {
                            amplitude_graphe.enregistrerGraphe("png");
                        }
                        else if(result[4]==true) {
                            amplitude_graphe.enregistrerGraphe("jpeg");
                        }
                    }
                    if(result[2]==true) {
                        if(result[5]==true) {
                            historiqueControleur.enregistrerLog(log_file.getText(),"txt");
                        }
                        else if(result[6]==true) {
                            historiqueControleur.enregistrerLog(log_file.getText(),"csv");
                        }
                    }
                }
            }
        });
    }
}
