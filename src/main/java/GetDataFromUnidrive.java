import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.net.URISyntaxException;
import java.util.List;

/**
 * This class opens a socket for sending a request to the Unidrive SP .
 */
public class GetDataFromUnidrive {

    /**
     * Constructor
     * @param addr the URI request
     * @throws URISyntaxException
     */
    public GetDataFromUnidrive(String addr) throws URISyntaxException {

    }

    /**
     * Connecting using username and password ; then getting the active current each second .
     * @throws Exception
     */
    public void displayCurrent() throws Exception {
        final WebClient webClient = new WebClient();

        // Get the first page
        final HtmlPage page1 = webClient.getPage("http://192.168.130.182/main/login.htm");

        //Connecting using administrator account
        List<HtmlForm> arr = page1.getForms();
        for(HtmlForm form : arr) {
            final HtmlTextInput username = form.getInputByName("UserLogin_InputUsername");
            if(username==null) {
                System.out.println("NULL USER");
            }
            final HtmlPasswordInput password = form.getInputByName("UserLogin_InputPassword");
            if(password==null) {
                System.out.println("NULL PASSWORD");
            }
            final HtmlElement button = page1.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/form/table/tbody/tr/td/table/tbody/tr[5]/td[2]/table/tbody/tr[2]/td[2]/table/tbody/tr/td[2]/a");
            if(button !=null) {
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
                for (int i=0;i<=5;i++) {
                    page_final = webClient.getPage("http://192.168.130.182/US/4/parameters/menu.htm");
                    HtmlElement active_current = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");
                    System.out.println("LE COURANT EST "+active_current.getTextContent());
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                final HtmlElement button_logout = (HtmlElement)page_final.getElementById("mainnav7");
                HtmlPage page_out = button_logout.click();
                System.out.println(page_out.getTitleText());
            }

            final HtmlElement button_logout = (HtmlElement)page_final.getElementById("mainnav7");
            HtmlPage page_out = button_logout.click();
            System.out.println(page_out.getTitleText());
        }
    }
}