import java.io.*;
import java.net.Socket;
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
    public String getResponse() throws IOException {
        String result="";
        Socket socket = new Socket(host,port);

        //sending request
        PrintWriter request = new PrintWriter( socket.getOutputStream() );
        request.print(  "GET " + path + " HTTP/1.1\r\n" +
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
    }
}
