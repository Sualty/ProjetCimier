import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * This class opens a socket for sending a request to the Unidrive SP .
 */
public class GetDataFromUnidrive {

    //du style http://129.111.0.136/US/1.21/dynamic/readparval.xml
    private String host;
    private String path;
    private int port;

    /**
     * Constructor
     * @param addr the URI request
     * @throws URISyntaxException
     */
    public GetDataFromUnidrive(String addr) throws URISyntaxException {
        URI uri = new URI(addr);
        host = uri.getHost();
        path = uri.getPath();

        if(path==null || path.length()==0) {
            path = "/";
        }
        String query = uri.getRawQuery();
        if(query !=null && query.length()>0) {
            path += "?" + query;
        }

        String protocol = uri.getScheme();
        port = uri.getPort();
        if(port == -1) {
            if(protocol.equals("http")) {
                port =80;
            }
            else if(protocol.equals("https")) {
                port = 443;
            }
        }
    }

    //TODO : on suppose que y a pas de mot de passe pour le moment ... si y en a un, la requête est à modiifer .

    /**
     * Send to the Unidrive a request and wait until it gets a response .
     * @return the response of the Unidrive
     * @throws IOException
     */
    private final String USER_AGENT = "Mozilla/5.0";
    public String getResponse() throws IOException {
        String result="";
        Socket socket = new Socket(host,port);

        //encoding login and password
      /*  String username = "root";
        String password = "ut72";
        String auth = username + ":" + password;
        String encodedAuth = Base64.encode(auth.getBytes());*/
        //sending request
        //   "Authorization: Basic " + encodedAuth + "\r\n" +
        PrintWriter request = new PrintWriter( socket.getOutputStream() );
        request.print(  "GET " +path + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "Connection: close\r\n\r\n");
        request.flush( );

        //waiting for response
        InputStream inStream = socket.getInputStream( );
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(inStream));
        String line;
        while ((line = rd.readLine()) != null) {
            result = result+"\n"+line;
        }
        return result;


     /*   String url = "http://192.168.130.182/US/4.02/dynamic/readparval.xml";

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));


        String result = response.getEntity().toString();

        return result;*/
    }

    public String submittingForm() throws Exception {
        final WebClient webClient = new WebClient();

        // Get the first page
        final HtmlPage page1 = webClient.getPage("http://192.168.130.182/main/login.htm");

        // Get the form that we are dealing with and within that form,
        // find the submit button and the field that we want to change.
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
            final HtmlElement button = findElementFromXPath(form,"/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/form/table/tbody/tr/td/table/tbody/tr[5]/td[2]/table/tbody/tr[2]/td[2]/table/tbody/tr/td[2]/a");
            if(button !=null) {
                username.setValueAttribute("root");
                password.setValueAttribute("ut72");
                HtmlPage page2 = button.click();
                if(page2.getTitleText().equals("Drive Description (root)")) {
                    System.out.println("PERMISSION GRANTED");
                }
                final HtmlElement button_parameters = (HtmlElement)page2.getElementById("mainnav2");
                HtmlPage page_params = button_parameters.click();

                final HtmlElement menu_4 = findElementFromXPath(page_params.getBody(),"/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[9]/td[2]/a");
                HtmlPage page_final = menu_4.click();

                HtmlElement active_current = findElementFromXPath(page_final.getBody(),"/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");

                try {
                    for (int i=0;i<=59;i++) {
                        page_final=webClient.getPage(page_final.getBaseURL());
                        active_current = findElementFromXPath(page_final.getBody(),"/html/body/table/tbody/tr[1]/td/table[9]/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[7]/td[2]");
                        System.out.println("LE COURANT EST "+active_current.getTextContent());
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    final HtmlElement button_logout = (HtmlElement)page2.getElementById("mainnav7");
                    HtmlPage page_out = button_logout.click();
                    System.out.println(page_out.getTitleText());
                }



                final HtmlElement button_logout = (HtmlElement)page2.getElementById("mainnav7");
                HtmlPage page_out = button_logout.click();
                System.out.println(page_out.getTitleText());
            }
            else {
                System.out.println("try again");
            }

        }
        //return getResponse();
        return "blou";
    }


    private HtmlElement findElementFromXPath (HtmlElement element_root,String path) {
        final HtmlElement toBefound = null;
        return findElementRecFromXPath(element_root,toBefound,path);
    }
    private HtmlElement findElementRecFromXPath(HtmlElement element_root,HtmlElement toBeFound, String path) {
        Iterable<HtmlElement> arr = element_root.getHtmlElementDescendants();
        boolean isFound = false;
        for(HtmlElement e : arr) {
            if(e.getCanonicalXPath().equals(path)) {
                toBeFound = e;
                isFound = true;
            }
        }
        if(isFound) {
            return toBeFound;
        }
        else {
            for(HtmlElement e : arr) {
                findElementRecFromXPath(e,toBeFound,path);
            }
        }
        return null;
    }
}