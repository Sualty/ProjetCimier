
public class Main {

    public static void main(String[] args) throws Exception {
        GetDataFromUnidrive getter = null;
        while(true) {
            getter = new GetDataFromUnidrive();
            getter.start();
            getter.join();
        }
    }
}
