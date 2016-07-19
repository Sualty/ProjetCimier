import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class opens a socket for sending a request to the Unidrive SP .
 */
public class GetDataFromUnidrive {

    private List<Double> currentValues;
    /**
     * Constructor
     * @param addr the URI request
     * @throws URISyntaxException
     */
    public GetDataFromUnidrive(String addr) throws URISyntaxException {
        this.currentValues = new ArrayList<>();
    }

    /**
     * Connecting using username and password ; then getting the active current each second .
     * @throws Exception
     */
    public void displayCurrent() throws IOException {
        final WebClient webClient = new WebClient();
        try {


            // Get the first page
            final HtmlPage page1 = webClient.getPage("http://192.168.130.182/main/login.htm");

            //Connecting using administrator account
            List<HtmlForm> arr = page1.getForms();
            for(HtmlForm form : arr) {
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

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    File file = new File(dateFormat.format(date) + ".txt");
                    file.getParentFile().mkdirs();
                    PrintWriter writer = new PrintWriter(file, "UTF-8");


                    for (int i = 0; i <= 5; i++) {
                        page_final = webClient.getPage("http://192.168.130.182/US/4/parameters/menu.htm");
                        HtmlElement active_current = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");

                        //writing to list
                        this.currentValues.add(parseCurrent(active_current.getTextContent()));

                        //writing to file
                        Date d = new Date();
                        writer.println(dateFormat.format(d) + "   " + active_current.getTextContent());

                        //sleeping 1 second
                        Thread.sleep(1000);
                    }
                    writer.close();
                } catch (InterruptedException e) {
                    final HtmlElement button_logout = (HtmlElement) page_final.getElementById("mainnav7");
                    HtmlPage page_out = button_logout.click();
                    System.out.println(page_out.getTitleText());
                }

                for (int i = 0; i < this.currentValues.size(); i++) {
                    System.out.println(currentValues.get(i));
                }

                final HtmlElement button_logout = (HtmlElement) page_final.getElementById("mainnav7");
                HtmlPage page_out = button_logout.click();
                System.out.println(page_out.getTitleText());
            }

        }
        catch(Exception e) {
            HtmlPage page_final = webClient.getPage("http://192.168.130.182/US/4/parameters/menu.htm");
            final HtmlElement button_logout = (HtmlElement) page_final.getElementById("mainnav7");
            HtmlPage page_out = button_logout.click();
            System.out.println(page_out.getTitleText());
        }
    }


    public double parseCurrent(String current) {
        return Double.parseDouble(current.substring(0,current.length()-1));
    }

    public List<Double> getListCurrent() {
        return this.currentValues;
    }
}