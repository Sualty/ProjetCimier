import com.opencsv.CSVWriter;

import java.io.FileWriter;

/**
 * Created by blou on 20/07/16.
 */
public class TestCSV {

    public static void main(String[] args) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"), '\t');
        // feed in your array (or convert your data to an array)
        String[] entries = "first#second#third".split("#");
        writer.writeNext(entries);
        writer.writeNext(entries);
        writer.close();
    }
}
