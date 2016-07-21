/**
 * Created by blou on 21/07/16.
 */
public enum KindOfData {

    ACTIVECURRENT("active"),
    CURRENTMAGNITUDE("magnitude");

    private String txt;

    KindOfData(String txt) {
        this.txt = txt;
    }

    public String getTxt() {
        return this.txt;
    }
}
