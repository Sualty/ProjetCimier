package modele;

import controleur.KindOfData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * Class creating a graph and saving it into a JPEG picture .
 */
public class DrawGraph extends JPanel {

    private JFreeChart lineChart;

    public DrawGraph(String chartTitle,String y_axis) {
        super();
        final XYDataset dataset = new XYSeriesCollection();
        this.lineChart = ChartFactory.createXYLineChart(
                chartTitle,
                "Temps (s)",y_axis,
                dataset,
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 460 , 340 ) );

        this.add(chartPanel);
    }

    /**
     * Contructor ; initialize the graph and the file .
     * @param chartTitle
     * @param y_axis
     * @param valeurs
     */
    public DrawGraph(String chartTitle, String y_axis, ArrayList<ResultatRecherche> valeurs,KindOfData kindOfData)
    {
        super();
        final XYDataset dataset = createDataset(valeurs,kindOfData);
        this.lineChart = ChartFactory.createXYLineChart(
                chartTitle,
                "Temps (s)",y_axis,
                dataset,
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 460 , 340 ) );

        this.add(chartPanel);
    }

    /**
     * Transforming a list of double values into a dataset (which can be used by the graph)
     * @param valeurs
     * @return
     */
    private XYDataset createDataset(ArrayList<ResultatRecherche> valeurs, KindOfData kindOfData)
    {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        for(int k=0;k<valeurs.size();k++) {
            ResultatRecherche r = valeurs.get(k);
            final XYSeries sery = new XYSeries(r.getDate()+" - "+k);
            if(kindOfData==KindOfData.ACTIVECURRENT) {
                String[] tab_string = r.getActif_graphe().keySet().toArray(new String[r.getActif_graphe().size()]);
                for(int i=0;i<tab_string.length;i++) {
                    sery.add(i,r.getActif_graphe().get(tab_string[i]));
                }
            }
            else if(kindOfData==KindOfData.CURRENTMAGNITUDE){
                String[] tab_string = r.getAmplitude_graphe().keySet().toArray(new String[r.getAmplitude_graphe().size()]);
                for(int i=0;i<tab_string.length;i++) {
                    sery.add(i,r.getAmplitude_graphe().get(tab_string[i]));
                }
            }
            dataset.addSeries(sery);
        }

        return dataset;
    }

    public void enregistrerGraphe(String kind) {
        BufferedImage objBufferedImage=this.lineChart.createBufferedImage(600,800);
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        try {
            ImageIO.write(objBufferedImage, kind, bas);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] byteArray=bas.toByteArray();

        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage image = null;
        try {
            image = ImageIO.read(in);
            String title = this.lineChart.getTitle().getText();
            File outputfile = new File(title+"."+kind);
            ImageIO.write(image, kind, outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}