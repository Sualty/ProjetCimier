import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.opencsv.CSVWriter;
import org.jfree.ui.RefineryUtilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class reads information from the Unidrive and then writes into database, .csv files and graphs
 */
public class GetDataFromUnidrive extends Thread{

    private List<Double> currentValues;
    private List<Double> currentMagnitudeValues;

    /**
     * Constructor
     *
     * @throws URISyntaxException
     */
    public GetDataFromUnidrive() throws URISyntaxException {
        //initialize the lists ; adding 0.0 twice to each list in order to have something to start with in the displayCurrent() method .
        this.currentValues = new ArrayList<>();
        currentValues.add(0.0);currentValues.add(0.0);
        this.currentMagnitudeValues = new ArrayList<>();
        currentMagnitudeValues.add(0.0);currentMagnitudeValues.add(0.0);
    }

    /**
     * running the thread and calling displayCurrent method
     */
    public void run() {
        try {
            try {
                displayCurrent();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connecting using username and password ; then getting the active current and the current magnitude each second .
     *
     * @throws Exception
     */
    public void displayCurrent() throws IOException, InterruptedException {
        final WebClient webClient = new WebClient();
        try {

            // Get the first page
            final HtmlPage page1 = webClient.getPage("http://"+Configuration.ipUnidrive+"/main/login.htm");

            //Connecting using administrator account
            List<HtmlForm> arr = page1.getForms();
            for (HtmlForm form : arr) {
                final HtmlTextInput username = form.getInputByName("UserLogin_InputUsername");
                if (username == null) {
                    System.out.println("NULL USER");
                }
                final HtmlPasswordInput password = form.getInputByName("UserLogin_InputPassword");
                if (password == null) {
                    System.out.println("NULL PASSWORD");
                }
                final HtmlElement button = page1.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/form/table/tbody/tr/td/table/tbody/tr[5]/td[2]/table/tbody/tr[2]/td[2]/table/tbody/tr/td[2]/a");
                if (button != null) {
                    username.setValueAttribute(Configuration.login);
                    password.setValueAttribute(Configuration.password);
                    HtmlPage page2 = button.click();
                    if (page2.getTitleText().equals("Drive Description ("+Configuration.login+")")) {
                        System.out.println("PERMISSION GRANTED");
                    }
                }
                HtmlPage page_final = webClient.getPage("http://"+Configuration.ipUnidrive+"/US/4/parameters/menu.htm");

                try {

                    //the Shutdown Hook will disconnect the program from the Unidrive if something bad happens.
                    final HtmlPage p = page_final;
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        public void run() {

                            HtmlElement button_logout = (HtmlElement) p.getElementById("mainnav7");
                            HtmlPage page_out = null;
                            try {
                                page_out = button_logout.click();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println(page_out.getTitleText());
                        }
                    }, "Shutdown-thread"));

                    DynamicDataDemo active_current_graph = new DynamicDataDemo("Courant actif en fonction du temps", "Temps (s)", "Courant actif (A)");
                    DynamicDataDemo current_magnitude_graph = new DynamicDataDemo("Amplitude du courant en fonction du temps", "Temps (s)", "Amplitude du courant (A)");

                    while(true) {
                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat(Configuration.dateFormatString);

                        //creating dynamic graphs
                        active_current_graph = new DynamicDataDemo("Courant actif en fonction du temps", "Temps (s)", "Courant actif (A)");
                        active_current_graph.pack();
                        RefineryUtilities.centerFrameOnScreen(active_current_graph);

                        current_magnitude_graph = new DynamicDataDemo("Amplitude du courant en fonction du temps", "Temps (s)", "Amplitude du courant (A)");
                        current_magnitude_graph.pack();
                        RefineryUtilities.centerFrameOnScreen(current_magnitude_graph);

                        //waiting not zero current values
                        while (this.currentMagnitudeValues.get(currentMagnitudeValues.size() - 1) == 0
                                && this.currentValues.get(currentValues.size() - 1) == 0) {
                            page_final = webClient.getPage("http://" + Configuration.ipUnidrive + "/US/4/parameters/menu.htm");
                            HtmlElement active_current = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");
                            HtmlElement current_magnitude = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[5]/td[2]");

                            double ac = parseCurrent(active_current.getTextContent());
                            double cm = parseCurrent(current_magnitude.getTextContent());
                            //writing to list for static graphs
                            if (ac != 0.0 && cm != 0.0) {
                                this.currentValues.add(ac);
                                this.currentMagnitudeValues.add(cm);
                                active_current_graph.setLastValue(ac);
                                current_magnitude_graph.setLastValue(cm);
                                active_current_graph.setVisible(true);
                                current_magnitude_graph.setVisible(true);
                            }
                        }

                        //init csv writer
                        File file = new File(dateFormat.format(date) + ".csv");
                        System.out.println(file.getAbsolutePath());
                        file.getParentFile().mkdirs();
                        CSVWriter writer = new CSVWriter(new FileWriter(dateFormat.format(date) + ".csv"), '\t');

                        // feed in your array (or convert your data to an array)
                        String[] entries = "Date#Active current#Current magnitude".split("#");
                        writer.writeNext(entries);

                        //here comes the serious stuff
                        do {
                            page_final = webClient.getPage("http://" + Configuration.ipUnidrive + "/US/4/parameters/menu.htm");
                            HtmlElement active_current = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");
                            HtmlElement current_magnitude = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[5]/td[2]");

                            double ac = parseCurrent(active_current.getTextContent());
                            double cm = parseCurrent(current_magnitude.getTextContent());

                            //writing to list for static graphs
                            this.currentValues.add(ac);
                            this.currentMagnitudeValues.add(cm);

                            //writing in .csv log file
                            String s = date.toString() + "#" + ac + "#" + cm;
                            String[] data = s.split("#");
                            writer.writeNext(data);

                            //writing to rt graphs
                            current_magnitude_graph.setLastValue(cm);
                            active_current_graph.setLastValue(ac);

                            //sleeping 1 second
                            Thread.sleep(1000);
                        }

                        while (!(this.currentMagnitudeValues.get(currentMagnitudeValues.size() - 1) == 0
                                && this.currentMagnitudeValues.get(currentMagnitudeValues.size() - 2) == 0
                                && this.currentValues.get(currentValues.size() - 1) == 0
                                && this.currentValues.get(currentValues.size() - 2) == 0));

                        writer.close();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    final HtmlElement button_logout = (HtmlElement) page_final.getElementById("mainnav7");
                    HtmlPage page_out = button_logout.click();
                    System.out.println(page_out.getTitleText());
                }


                final HtmlElement button_logout = (HtmlElement) page_final.getElementById("mainnav7");
                HtmlPage page_out = button_logout.click();
                System.out.println(page_out.getTitleText());

            }

        } catch (Exception e) {
            e.printStackTrace();
            HtmlPage page_final = webClient.getPage("http://"+ Configuration.ipUnidrive+"/US/4/parameters/menu.htm");
            final HtmlElement button_logout = (HtmlElement) page_final.getElementById("mainnav7");
            HtmlPage page_out = button_logout.click();
            System.out.println(page_out.getTitleText());
        }

        Thread savegraphs = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("length current"+currentValues.size());
                DrawGraph graphe = new DrawGraph("Courant actif", "Courant actif dans le moteur en fonction du temps", "Courant actif (A)", currentValues);
                graphe.pack();
                RefineryUtilities.centerFrameOnScreen(graphe);
                graphe.setVisible(true);

                System.out.println("length magnitude current"+currentMagnitudeValues.size());
                DrawGraph graphe2 = new DrawGraph("Magnitude Current", "Magnitude du courant dans le moteur en fonction du temps", "Magnitude du courant (A)", currentMagnitudeValues);
                graphe2.pack();
                RefineryUtilities.centerFrameOnScreen(graphe2);
                graphe2.setVisible(true);

                try {
                    graphe.saveGraph();
                    graphe2.saveGraph();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        savegraphs.start();
        savegraphs.join();
    }

    /**
     * Parsing a text field which is got from the parameters
     * @param current
     * @return
     */
    public double parseCurrent(String current) {
        return Double.parseDouble(current.substring(0, current.length() - 1));
    }
}