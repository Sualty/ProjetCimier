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

        this.url = "jdbc:mysql://"+Configuration.ipCimier+":"+Configuration.portDatabase+"/cimier?useSSL=false";
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

            java.util.Date dateStr = formatter_date.parse(day_of_year);
            java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());

            String updateString = "INSERT INTO records(day_of_year,begin_hour,kind) VALUES (?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setDate(1,dateDB);
            preparedStatement.setString(2, begin_hour);
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
     * get the id (primary key) of a record, given its other parameters
     * @param day_of_year
     * @param begin_hour
     * @param kind
     * @return
     */
    public int getIdOfRecord(String day_of_year,String begin_hour,KindOfData kind) {
        int id = -1;
        try {
            SimpleDateFormat formatter_date = new SimpleDateFormat("dd-MM-yyyy");

            java.util.Date dateStr = formatter_date.parse(day_of_year);
            java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());

            String updateString = "SELECT records.id FROM records WHERE records.day_of_year=? AND records.begin_hour=? AND records.kind=?";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setDate(1,dateDB);
            preparedStatement.setString(2,begin_hour);
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
     * @param hour
     * @param val
     */
    public void addDatas(int id,String hour,double val) {
        try {
            String updateString = "INSERT INTO datas VALUES (?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, hour);
            preparedStatement.setDouble(3, val);

            preparedStatement.execute();
        }

        catch (SQLException ex) {
            ex.printStackTrace();

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
