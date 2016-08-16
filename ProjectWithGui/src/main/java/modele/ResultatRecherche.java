package modele;

import java.awt.image.BufferedImage;

/**
 * Created by blou on 10/08/16.
 */
public class ResultatRecherche {
    private String date;//TODO Date ou String ?
    private String log;
    private BufferedImage actif_graphe;
    private BufferedImage amplitude_graphe;

    public ResultatRecherche(String date,String log,BufferedImage actif_graphe,BufferedImage amplitude_graphe) {
        this.date=date;
        this.log=log;
        this.actif_graphe=actif_graphe;
        this.amplitude_graphe=amplitude_graphe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public BufferedImage getActif_graphe() {
        return actif_graphe;
    }

    public void setActif_graphe(BufferedImage actif_graphe) {
        this.actif_graphe = actif_graphe;
    }

    public BufferedImage getAmplitude_graphe() {
        return amplitude_graphe;
    }

    public void setAmplitude_graphe(BufferedImage amplitude_graphe) {
        this.amplitude_graphe = amplitude_graphe;
    }
}
