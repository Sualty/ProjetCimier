public class Main {

    public static void main(String[] args) throws Exception {

        GetDataFromUnidrive getter = null;
        getter = new GetDataFromUnidrive();
        getter.start();
        getter.join();

    }
}
