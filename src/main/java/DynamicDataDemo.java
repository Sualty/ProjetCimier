import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An example to show how we can create a dynamic chart.
 */
public class DynamicDataDemo extends ApplicationFrame implements ActionListener {

    /** The time series data. */
    private TimeSeries series;

    /** The most recent value added. */
    private double lastValue = 0.0;

    /** Timer to refresh graph after every 1/4th of a second */
    private Timer timer = new Timer(250, this);

        /**
     * Constructs a new dynamic chart application.
     *
     * @param title  the frame title.
     */
    public DynamicDataDemo(final String title, String titleX, String titleY) {

        super(title);
        this.series = new TimeSeries("Intensité", Millisecond.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
        final JFreeChart chart = createChart(dataset, title, titleX, titleY);

        timer.setInitialDelay(1000);

        //Sets background color of chart
        chart.setBackgroundPaint(Color.LIGHT_GRAY);

        //Created JPanel to show graph on screen
        final JPanel content = new JPanel(new BorderLayout());

        //Created Chartpanel for chart area
        final ChartPanel chartPanel = new ChartPanel(chart);

        //Added chartpanel to main panel
        content.add(chartPanel);

        //Sets the size of whole window (JPanel)
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 500));

        //Puts the whole content on a Frame
        setContentPane(content);

        timer.start();

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return A sample chart.
     */
    private JFreeChart createChart(final XYDataset dataset, String title, String titleX, String titleY) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                title,
                titleX,
                titleY,
                dataset,
                true,
                true,
                false
        );

        final XYPlot plot = result.getXYPlot();

        plot.setBackgroundPaint(new Color(0xffffe0));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.lightGray);

        ValueAxis xaxis = plot.getDomainAxis();
        xaxis.setAutoRange(true);

        //Domain axis would show data of 60 seconds for a time
        xaxis.setFixedAutoRange(60000.0);  // 60 seconds
        xaxis.setVerticalTickLabels(true);

        ValueAxis yaxis = plot.getRangeAxis();
        yaxis.setAutoRange(true);

        return result;
    }

    public void actionPerformed(final ActionEvent e) {

        final Millisecond now = new Millisecond();
        this.series.add(new Millisecond(), this.lastValue);
    }

    public void setLastValue(double v) {
        if(v == 0 && this.lastValue != 0){
            timer.stop();
        }
        this.lastValue = v;
    }
}  