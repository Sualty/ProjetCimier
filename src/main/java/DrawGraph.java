import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DrawGraph extends ApplicationFrame
{

    private JFreeChart lineChart;
    public DrawGraph( String applicationTitle , String chartTitle, String y_axis,List<Double> valeurs)
    {
        super(applicationTitle);
        this.lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Temps (s)",y_axis,
                createDataset(valeurs),
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
    }

    public void saveGraph() throws IOException {
        String title = this.lineChart.getTitle().getText();
        DateFormat dateFormat = new SimpleDateFormat("yyyy\\MM\\dd\\HH_mm_ss");
        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");  //POUR LINUX

        int width = 640; /* Width of the image */
        int height = 480; /* Height of the image */

        Date date = new Date();

        if(title.equals("Courant actif dans le moteur en fonction du temps"))
            title = "courant_actif";
        else if (title.equals("Magnitude du courant dans le moteur en fonction du temps"))
            title = "magnitude_courant";

        File file = new File(dateFormat.format(date) +title+ ".jpeg");
        file.getParentFile().mkdirs();
        ChartUtilities.saveChartAsJPEG(file ,lineChart, width ,height);
    }

    private DefaultCategoryDataset createDataset( List<Double> valeurs )
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        for(int i=0; i<valeurs.size();i++){
            dataset.addValue(new Double(valeurs.get(i)), "intensité", new Integer(i));
        }
        return dataset;
    }
}