import java.sql.*;

/**
 * Created by Gunsillie on 21/07/2016.
 */
public class ConnectDatabase {

    private Connection con;
    private String url;
    private String username;
    private String password;

    public ConnectDatabase(String url, String username, String password){

        this.url = url;
        this.username = username;
        this.password = password;

        try  {
            this.con = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

    public void addData() {
        try {
            Statement st = (Statement) con.createStatement();

            st.executeUpdate("INSERT INTO datas " + "VALUES (1,NOW(),2.0)");

            //con.close();
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeData() {

    }

    public void accessData() {
        try {
            Statement st = (Statement) con.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery("SELECT * FROM datas");
            // iterate through the java resultset
            while (rs.next())
            {
                int id = rs.getInt("id");
                Date date = rs.getDate("hour");
                double val = rs.getDouble("val");

                // print the results
                System.out.println(id+" "+date.toString()+" "+val);
            }

            con.close();
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
