package controleur;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by blou on 16/08/16.
 */
public class HistoriqueControleurTest {

    private HistoriqueControleur controleur;

    @Before
    public void beforeTest() {
        controleur = new HistoriqueControleur();
    }
    @Test
    public void rechercheSQL() throws Exception {

    }

    @Test
    public void rechercheDates() throws Exception {

    }

    @Test
    public void parseDateString() throws Exception {
        ArrayList<String> a = controleur.parseDateString("02/08/2016");
        System.out.println(a.toString());

        a = controleur.parseDateString("42/08/1999");//TODO date pas ok...
       // assertNull(a);

        a = controleur.parseDateString("01/03/2016;04/06/2016;08/08/2016:10/08/2016");
        assertNotNull(a);
        ArrayList<String> test = new ArrayList<>();
        test.add("01/03/2016");
        test.add("04/06/2016");
        test.add("08/08/2016");
        test.add("09/08/2016");
        test.add("10/08/2016");
        assertEquals(a,test);
    }
}