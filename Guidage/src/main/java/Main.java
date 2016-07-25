
public class Main {

    public static void main(String[] args) throws Exception {
        GetDataFromUnidrive getter = new GetDataFromUnidrive("http://192.168.130.182/US/4.02/dynamic/readparval.xml");
        getter.displayCurrent();
    }
}
