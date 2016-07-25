import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jfree.ui.RefineryUtilities;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class opens a socket for sending a request to the Unidrive SP .
 */
public class GetDataFromUnidrive {

    private List<Double> currentValues;
    private List<Double> currentMagnitudeValues;

    /**
     * Constructor
     *
     * @param addr the URI request
     * @throws URISyntaxException
     */
    public GetDataFromUnidrive(String addr) throws URISyntaxException {
        this.currentValues = new ArrayList<>();
        this.currentMagnitudeValues = new ArrayList<>();
    }

    /**
     * Connecting using username and password ; then getting the active current each second .
     *
     * @throws Exception
     */
    public void displayCurrent() throws IOException {
        final WebClient webClient = new WebClient();
        try {


            // Get the first page
            final HtmlPage page1 = webClient.getPage("http://192.168.130.182/main/login.htm");

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
                    username.setValueAttribute("root");
                    password.setValueAttribute("ut72");
                    HtmlPage page2 = button.click();
                    if (page2.getTitleText().equals("Drive Description (root)")) {
                        System.out.println("PERMISSION GRANTED");
                    }
                }

                //accessing to the data
                HtmlPage page_final = webClient.getPage("http://192.168.130.182/US/4/parameters/menu.htm");

                try {
                    Date date = new Date();

                    //setting graphs
                    SimpleDateFormat format = new SimpleDateFormat("yyyy\\MM\\dd\\HH_mm_ss");
                    //SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");

                    DynamicDataDemo active_current_graph = new DynamicDataDemo("Courant actif en fonction du temps", "Temps (s)", "Courant actif (A)");
                    active_current_graph.pack();
                    RefineryUtilities.centerFrameOnScreen(active_current_graph);
                    active_current_graph.setVisible(true);

                    DynamicDataDemo current_magnitude_graph = new DynamicDataDemo("Amplitude du courant en fonction du temps", "Temps (s)", "Amplitude du courant (A)");
                    current_magnitude_graph.pack();
                    RefineryUtilities.centerFrameOnScreen(current_magnitude_graph);
                    current_magnitude_graph.setVisible(true);

                    //setting database;adding records
                    SimpleDateFormat formatter_date = new SimpleDateFormat("dd-MM-yyyy"); // your template here
                    SimpleDateFormat formatter_hour = new SimpleDateFormat("HH-mm-ss");

                    ConnectDatabase db = new ConnectDatabase("jdbc:mysql://localhost:3306/cimier?useSSL=false","root","ZUdug@H!");

                    db.addRecords(formatter_date.format(date),formatter_hour.format(date),KindOfData.ACTIVECURRENT);
                    db.addRecords(formatter_date.format(date),formatter_hour.format(date),KindOfData.CURRENTMAGNITUDE);

                    int id_active = db.getIdOfRecord(formatter_date.format(date),formatter_hour.format(date),KindOfData.ACTIVECURRENT);
                    int id_magnitude = db.getIdOfRecord(formatter_date.format(date),formatter_hour.format(date),KindOfData.CURRENTMAGNITUDE);

                    do {
                        page_final = webClient.getPage("http://192.168.130.182/US/4/parameters/menu.htm");
                        HtmlElement active_current = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");
                        HtmlElement current_magnitude = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[5]/td[2]");

                        //writing to list for static graphs
                        this.currentValues.add(parseCurrent(active_current.getTextContent()));
                        this.currentMagnitudeValues.add(parseCurrent(current_magnitude.getTextContent()));

                        //feeding database
                        Date d = new Date();
                        db.addDatas(id_active,formatter_hour.format(d),parseCurrent(active_current.getTextContent()));
                        db.addDatas(id_magnitude,formatter_hour.format(d),parseCurrent(current_magnitude.getTextContent()));

                        //writing to dynamic graph
                        active_current_graph.setLastValue(parseCurrent(active_current.getTextContent()));
                        current_magnitude_graph.setLastValue(parseCurrent(current_magnitude.getTextContent()));

                        //sleeping 1 second
                        Thread.sleep(1000);
                    }
                    while(!(this.currentMagnitudeValues.size()>1
                            && this.currentMagnitudeValues.get(currentMagnitudeValues.size()-1)==0
                            && this.currentMagnitudeValues.get(currentMagnitudeValues.size()-2)!=0 ));

                    System.out.println("RECORDS");
                    db.accessRecords();
                    System.out.println("DATAS");
                    db.accessDatas();
                    db.closeConnection();

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
            HtmlPage page_final = webClient.getPage("http://192.168.130.182/US/4/parameters/menu.htm");
            final HtmlElement button_logout = (HtmlElement) page_final.getElementById("mainnav7");
            HtmlPage page_out = button_logout.click();
            System.out.println(page_out.getTitleText());
        }
    }


    public double parseCurrent(String current) {
        return Double.parseDouble(current.substring(0, current.length() - 1));
    }

    public List<Double> getListCurrent() {
        return this.currentValues;
    }

    public List<Double> getListCurrentMagnitude() {
        return this.currentMagnitudeValues;
    }

}