import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import java.util.List;

public class DrawGraph extends ApplicationFrame
{
    public DrawGraph( String applicationTitle , String chartTitle, List<Double> valeurs)
    {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Temps (s)","Courant actif dans le moteur (A)",
                createDataset(valeurs),
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
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