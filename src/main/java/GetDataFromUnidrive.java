import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
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
/*        String result="";
        Socket socket = new Socket(host,port);

        //encoding login and password
        String username = "root";
        String password = "ut72";
        String auth = username + ":" + password;
        String encodedAuth = Base64.encode(auth.getBytes());

        //sending request
        PrintWriter request = new PrintWriter( socket.getOutputStream() );
        request.print(  "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "Authorization: Basic " + encodedAuth + "\r\n" +
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
        return result;*/
        String url = "http://192.168.130.182/US/4.02/dynamic/readparval.xml";

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

      /*  String result = "";
        String line = "";
        while ((line = rd.readLine()) != null) {
            result+=line;
        }*/
        String result = response.getEntity().toString();
        return result;
    }
}
