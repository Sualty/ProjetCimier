import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jfree.ui.RefineryUtilities;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * This class connects to the Unidrive for reading in real time active current and current magnitude .
 */
public class GetDataFromUnidrive {

    private double last_value_current;
    private double last_last_value_current;

    private double last_value_magnitude;
    private double last_last_value_magnitude;

    private DynamicDataDemo current_magnitude_graph;
    private DynamicDataDemo active_current_graph;

    /**
     * Constructor
     *
     * @throws URISyntaxException
     */
    public GetDataFromUnidrive() throws URISyntaxException {
        last_last_value_current = 0;
        last_value_current = 0;
        last_last_value_magnitude =0;
        last_value_magnitude = 0;

        active_current_graph = new DynamicDataDemo("Courant actif en fonction du temps", "Temps (s)", "Courant actif (A)");
        active_current_graph.pack();
        RefineryUtilities.centerFrameOnScreen(active_current_graph);
        active_current_graph.setVisible(false);

        current_magnitude_graph = new DynamicDataDemo("Amplitude du courant en fonction du temps", "Temps (s)", "Amplitude du courant (A)");
        current_magnitude_graph.pack();
        RefineryUtilities.centerFrameOnScreen(current_magnitude_graph);
        current_magnitude_graph.setVisible(false);
    }

    /**
     * Connecting using username and password ; then getting the active current each second, while there is no more current .
     *
     * @throws Exception
     */
    public void displayCurrent() throws IOException {
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
                    if (page2.getTitleText().equals("Drive Description (root)")) {
                        System.out.println("PERMISSION GRANTED");
                    }
                }

                //accessing to the data
                HtmlPage page_final = webClient.getPage("http://"+Configuration.ipUnidrive+"/US/4/parameters/menu.htm");

                while(last_value_current ==0 && last_last_value_current ==0) {
                    page_final = webClient.getPage("http://"+Configuration.ipUnidrive+"/US/4/parameters/menu.htm");
                    HtmlElement active_current = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");

                    //updating values
                    last_last_value_current = last_value_current;
                    last_value_current = parseCurrent(active_current.getTextContent());
                }

                current_magnitude_graph.setVisible(true);
                active_current_graph.setVisible(true);
                try {
                    do {
                        page_final = webClient.getPage("http://"+Configuration.ipUnidrive+"/US/4/parameters/menu.htm");
                        HtmlElement active_current = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");
                        HtmlElement current_magnitude = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[5]/td[2]");

                        //writing to dynamic graph
                        active_current_graph.setLastValue(parseCurrent(active_current.getTextContent()));
                        current_magnitude_graph.setLastValue(parseCurrent(current_magnitude.getTextContent()));

                        //setting values in order to see if we need to stop
                        last_last_value_current = last_value_current;
                        last_value_current = parseCurrent(active_current.getTextContent());

                        last_last_value_magnitude = last_value_magnitude;
                        last_value_magnitude = parseCurrent(current_magnitude.getTextContent());

                        //sleeping 1 second
                        Thread.sleep(1000);
                    }
                    while(!(last_value_current==0  && last_value_magnitude==0 && last_last_value_magnitude ==0 && last_last_value_current==0 ));
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
            HtmlPage page_final = webClient.getPage("http://"+Configuration.ipUnidrive+"/US/4/parameters/menu.htm");
            final HtmlElement button_logout = (HtmlElement) page_final.getElementById("mainnav7");
            HtmlPage page_out = button_logout.click();
            System.out.println(page_out.getTitleText());
        }
    }

    public double parseCurrent(String current) {
        return Double.parseDouble(current.substring(0, current.length() - 1));
    }
}