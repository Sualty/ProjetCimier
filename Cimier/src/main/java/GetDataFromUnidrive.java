import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.net.URISyntaxException;
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

                    Date date = new Date();

                    //setting database;adding records
                    SimpleDateFormat formatter_date = new SimpleDateFormat("dd-MM-yyyy"); // your template here
                    SimpleDateFormat formatter_hour = new SimpleDateFormat("HH-mm-ss");

                    ConnectDatabase db = new ConnectDatabase();


                    while(true) {
                        //waiting for not zero current values
                        while (this.currentMagnitudeValues.get(currentMagnitudeValues.size() - 1) == 0
                                && this.currentValues.get(currentValues.size() - 1) == 0) {
                            page_final = webClient.getPage("http://" + Configuration.ipUnidrive + "/US/4/parameters/menu.htm");
                            HtmlElement active_current = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");
                            HtmlElement current_magnitude = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[5]/td[2]");

                            double ac = parseCurrent(active_current.getTextContent());
                            double cm = parseCurrent(current_magnitude.getTextContent());
                            //writing to list for static graphs
                            if (ac != 0.0 && cm != 0.0) {
                                this.currentValues.add(parseCurrent(active_current.getTextContent()));
                                this.currentMagnitudeValues.add(parseCurrent(current_magnitude.getTextContent()));
                            }
                        }


                        //adding records to Database
                        db.addRecords(formatter_date.format(date), formatter_hour.format(date), KindOfData.ACTIVECURRENT);
                        db.addRecords(formatter_date.format(date), formatter_hour.format(date), KindOfData.CURRENTMAGNITUDE);

                        int id_active = db.getIdOfRecord(formatter_date.format(date), formatter_hour.format(date), KindOfData.ACTIVECURRENT);
                        int id_magnitude = db.getIdOfRecord(formatter_date.format(date), formatter_hour.format(date), KindOfData.CURRENTMAGNITUDE);


                        //here comes the serious stuff
                        do {
                            page_final = webClient.getPage("http://" + Configuration.ipUnidrive + "/US/4/parameters/menu.htm");
                            HtmlElement active_current = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");
                            HtmlElement current_magnitude = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[5]/td[2]");

                            double ac = parseCurrent(active_current.getTextContent());
                            double cm = parseCurrent(current_magnitude.getTextContent());

                            //writing to list
                            this.currentValues.add(ac);
                            this.currentMagnitudeValues.add(cm);

                            //feeding database
                            Date d = new Date();
                            db.addDatas(id_active, formatter_hour.format(d), ac);
                            db.addDatas(id_magnitude, formatter_hour.format(d), cm);

                            //sleeping 1 second
                            Thread.sleep(1000);
                        }
                        while (!(this.currentMagnitudeValues.get(currentMagnitudeValues.size() - 1) == 0
                                && this.currentMagnitudeValues.get(currentMagnitudeValues.size() - 2) == 0
                                && this.currentValues.get(currentValues.size() - 1) == 0
                                && this.currentValues.get(currentValues.size() - 2) == 0));
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