package controleur;

import modele.configuration.Configuration;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Class used for accessing to the database
 */
public class ConnectDatabase {

    private Connection con;
    private String url;
    private String username;
    private String password;

    /**
     * Connects to the database
     */
    public ConnectDatabase(){

        this.url = "jdbc:mysql://"+ Configuration.ipUnidrive+":"+Configuration.portDatabase+"/cimier?useSSL=false";
        this.username = Configuration.user_bd;
        this.password = Configuration.password_bd;

        boolean connected = false;
        while(!connected) {
            try  {
                Thread.sleep(10000);
                this.con = DriverManager.getConnection(url, username, password);
                System.out.println("Database connected!");
                connected = true;
            }
            catch (SQLException e) {
                //do nothing, juste pass the exception.
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * adding a record
     * @param day_of_year
     * @param begin_hour
     * @param kind
     */
    public void addRecords(String day_of_year,String begin_hour,KindOfData kind) {
        try {
            SimpleDateFormat formatter_date = new SimpleDateFormat("dd-MM-yyyy"); // your template here
            SimpleDateFormat formatter_hour = new SimpleDateFormat("HH-mm-ss");

            java.util.Date dateStr = formatter_date.parse(day_of_year);
            Date dateDB = new Date(dateStr.getTime());

            java.util.Date hourStr = formatter_date.parse(begin_hour);
            Date hourDB = new Date(hourStr.getTime());

            String updateString = "INSERT INTO records(day_of_year,begin_hour,kind) VALUES (?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setDate(1,dateDB);
            preparedStatement.setDate(2, hourDB);
            preparedStatement.setString(3, kind.getTxt());

            preparedStatement.execute();
        }

        catch (SQLException ex) {
            ex.printStackTrace();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * showing all records
     */
    public void accessRecords() {
        try {
            Statement st = (Statement) con.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery("SELECT * FROM records");
            // iterate through the java resultset
            while (rs.next())
            {
                int id = rs.getInt("id");
                Date date = rs.getDate("day_of_year");
                Date hour = rs.getDate("begin_hour");
                String kind = rs.getString("kind");

                // print the results
                System.out.println(id+" "+date.toString()+" "+hour.toString()+" "+kind);
            }
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * empty table of records
     */
    public void emptyRecords() {
        try {
            Statement st = (Statement) con.createStatement();

            st.executeUpdate("DELETE FROM records");
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * get the id (primary key) of a record, given its other parameters
     * @param day_of_year
     * @param begin_hour
     * @param kind
     * @return
     */
    public int getIdOfRecord(String day_of_year,String begin_hour,KindOfData kind) {
        int id = -1;
        try {
            SimpleDateFormat formatter_date = new SimpleDateFormat("dd-MM-yyyy"); // your template here
            SimpleDateFormat formatter_hour = new SimpleDateFormat("HH-mm-ss");

            java.util.Date dateStr = formatter_date.parse(day_of_year);
            Date dateDB = new Date(dateStr.getTime());

            java.util.Date hourStr = formatter_date.parse(begin_hour);
            Date hourDB = new Date(hourStr.getTime());

            String updateString = "SELECT records.id FROM records WHERE records.day_of_year=? AND records.begin_hour=? AND records.kind=?";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setDate(1,dateDB);
            preparedStatement.setDate(2, hourDB);
            preparedStatement.setString(3, kind.getTxt());

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                id = rs.getInt("id");
            }
            }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return id;
    }

    /**
     * add a data
     * @param id
     * @param date
     * @param val
     */
    public void addDatas(int id,String date,double val) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); // your template here
            java.util.Date dateStr = formatter.parse(date);
            Date dateDB = new Date(dateStr.getTime());


            String updateString = "INSERT INTO datas VALUES (?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setInt(1, id);
            preparedStatement.setDate(2, dateDB);
            preparedStatement.setDouble(3, val);

            preparedStatement.execute();
        }

        catch (SQLException ex) {
            ex.printStackTrace();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * empty data table
     */
    public void emptyDatas() {
        try {
            Statement st = (Statement) con.createStatement();

            st.executeUpdate("DELETE FROM datas");
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * display table of datas
     */
    public void accessDatas() {
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
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * close connection with BD
     */
    public void closeConnection () {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return this.con.isValid(0);
        } catch (SQLException e) {
            return false;
        }
    }
}
