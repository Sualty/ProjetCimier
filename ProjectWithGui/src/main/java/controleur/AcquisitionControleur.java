
package controleur;

import modele.DynamicDataDemo;

import java.io.IOException;
import java.util.List;

/**
 * Created by blou on 09/08/16.
 */
public class AcquisitionControleur {

    private boolean isActive;
    private List<Double> currentValues;
    private List<Double> currentMagnitudeValues;
    public AcquisitionControleur() {
        this.isActive = false;
    }

    /**
     * reads values on the unidrive in real time and display on real time graphs
     * @param nb_mesures
     * @param actif_graphe
     * @param amplitude_graphe
     * @return the log file
     */
    public String lancerAcquisition(int nb_mesures, DynamicDataDemo actif_graphe, DynamicDataDemo amplitude_graphe) throws IOException {
        this.isActive = true;
        actif_graphe.startGraph();
        amplitude_graphe.startGraph();

   /*     //connecting to the Unidrive

        final WebClient webClient = new WebClient();
        HtmlPage page_final = webClient.getPage("http://" + Configuration.ipUnidrive + "/US/4/parameters/menu.htm");
        try {
            // Get the first page
            final HtmlPage page1 = webClient.getPage("http://" + Configuration.ipUnidrive + "/main/login.htm");

            //Connecting using administrator account
            List<HtmlForm> arr = page1.getForms();
            for (HtmlForm form : arr) {//TODO remplacer par récup par xpath?
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
                    if (page2.getTitleText().equals("Drive Description (" + Configuration.login + ")")) {
                        System.out.println("PERMISSION GRANTED");
                    }
                }

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

                //initialize dynamic graphs
                DynamicDataDemo active_current_graph = new DynamicDataDemo("Courant actif en fonction du temps", "Temps (s)", "Courant actif (A)");
                DynamicDataDemo current_magnitude_graph = new DynamicDataDemo("Amplitude du courant en fonction du temps", "Temps (s)", "Amplitude du courant (A)");

                do {
                    page_final = webClient.getPage("http://" + Configuration.ipUnidrive + "/US/4/parameters/menu.htm");
                    HtmlElement active_current = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");
                    HtmlElement current_magnitude = page_final.getBody().getFirstByXPath("/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[5]/td[2]");

                    double ac = parseCurrent(active_current.getTextContent());
                    double cm = parseCurrent(current_magnitude.getTextContent());

                    //writing to list for static graphs
                    this.currentValues.add(ac);
                    this.currentMagnitudeValues.add(cm);

                    //writing to rt graphs
                    current_magnitude_graph.setLastValue(cm);
                    active_current_graph.setLastValue(ac);

                    //sleeping 1 second
                    Thread.sleep(1000/nb_mesures);//ne s'arrête jamais...nyényényé
                }
                while(isActive);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        catch (InterruptedException e) {
            e.printStackTrace();
            final HtmlElement button_logout = (HtmlElement) page_final.getElementById("mainnav7");
            HtmlPage page_out = button_logout.click();
            System.out.println(page_out.getTitleText());
        }

        //stopping connection with Unidrive

        final HtmlElement button_logout = (HtmlElement) page_final.getElementById("mainnav7");
        HtmlPage page_out = button_logout.click();
        System.out.println(page_out.getTitleText());
*/
   while(isActive) {
       System.out.println();
   }
        //stopping graphs
        actif_graphe.stopGraph();
        amplitude_graphe.stopGraph();

        //returning log string
        return "log has been updated";
    }



    public double parseCurrent(String current) {
        return Double.parseDouble(current.substring(0, current.length() - 1));
    }
    public void stopperAcquisition() {
        this.isActive = false;
    }
}