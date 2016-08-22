package controleur;

/**
 * of datas which can be used
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
