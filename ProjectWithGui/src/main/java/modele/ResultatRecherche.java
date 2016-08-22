package modele;

import java.util.HashMap;

/**
 * Created by blou on 10/08/16.
 */
public class ResultatRecherche {
    private String date;//TODO Date ou String ?
    private HashMap<String, Double> actif_graphe;
    private HashMap<String, Double> amplitude_graphe;

    public ResultatRecherche(String date, HashMap<String, Double> actif_graphe, HashMap<String, Double> amplitude_graphe) {
        this.date = date;
        this.actif_graphe = actif_graphe;
        this.amplitude_graphe = amplitude_graphe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, Double> getActif_graphe() {
        return actif_graphe;
    }

    public void setActif_graphe(HashMap<String, Double> actif_graphe) {
        this.actif_graphe = actif_graphe;
    }

    public HashMap<String, Double> getAmplitude_graphe() {
        return amplitude_graphe;
    }

    public void setAmplitude_graphe(HashMap<String, Double> amplitude_graphe) {
        this.amplitude_graphe = amplitude_graphe;
    }
}
