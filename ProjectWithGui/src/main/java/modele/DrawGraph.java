package modele;

import modele.configuration.Configuration;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Class creating a graph and saving it into a JPEG picture .
 */
public class DrawGraph extends ApplicationFrame
{

    private JFreeChart lineChart;
    private File file;

    /**
     * Contructor ; initialize the graph and the file .
     * @param applicationTitle
     * @param chartTitle
     * @param y_axis
     * @param valeurs
     */
    public DrawGraph(String applicationTitle , String chartTitle, String y_axis, List<Double> valeurs)
    {
        super(applicationTitle);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Temps (s)",y_axis,
                createDataset(valeurs),
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );

        String title = this.lineChart.getTitle().getText();
        DateFormat dateFormat = new SimpleDateFormat(Configuration.dateFormatString);

        if(title.equals("Courant actif dans le moteur en fonction du temps"))
            title = "courant_actif";
        else if (title.equals("Amplitude du courant dans le moteur en fonction du temps"))
            title = "magnitude_courant";

        Date date = new Date();

        file = new File(dateFormat.format(date) +title+ ".jpeg");
        file.getParentFile().mkdirs();
    }

    /**
     * Saving the graph into the file
     * @throws IOException
     */
    public void saveGraph() throws IOException {

        int width = 640; /* Width of the image */
        int height = 480; /* Height of the image */

        String p = file.getAbsolutePath();
        System.out.println(p);
        ChartUtilities.saveChartAsJPEG(file ,lineChart, width ,height);
    }

    /**
     * Transforming a list of double values into a dataset (which can be used by the graph)
     * @param valeurs
     * @return
     */
    private DefaultCategoryDataset createDataset( List<Double> valeurs )
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        for(int i=0; i<valeurs.size();i++){
            dataset.addValue(new Double(valeurs.get(i)), "intensité", new Integer(i));
        }
        return dataset;
    }
}