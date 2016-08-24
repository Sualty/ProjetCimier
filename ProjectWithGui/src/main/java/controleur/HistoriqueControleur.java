package controleur;

import modele.ResultatRecherche;
import modele.configuration.Configuration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by blou on 10/08/16.
 */
public class HistoriqueControleur {

    private Connection con;
    private String url;
    private String username;
    private String password;

    public HistoriqueControleur() {
        this.url = "jdbc:mysql://"+ Configuration.ipCimier+":"+Configuration.portDatabase+"/cimier?useSSL=false";
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

    public String rechercheSQL(String requete) {
        String result="";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(requete);

            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) result=result+",  ";
                    String columnValue = rs.getString(i);
                    result=result+columnValue + " " + rsmd.getColumnName(i);
                }
                result=result+"\n";
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param dates
     * @return
     */
    public ArrayList<ResultatRecherche> rechercheDates(String dates) {
        ArrayList<ResultatRecherche> result = new ArrayList<>();

        ArrayList<String> dates_array = parseDateString(dates);
        if(dates_array==null) {
            return null;
        }

        System.out.println(dates_array.toString());

        for(String d : dates_array) {
            ArrayList<int[]> ids = getIdOfRecord(d);
            for(int i=0;i<ids.size();i++) {

                int[] tab = ids.get(i);
                LinkedHashMap<String,Double> active_record=getRecord(tab[0]);
                LinkedHashMap<String,Double> amplitude_record = getRecord(tab[1]);
                String hour = getHourFromId(tab[0]);
                ResultatRecherche resultat= new ResultatRecherche(d+"-"+hour,active_record,amplitude_record);
                result.add(resultat);
            }
        }

        return result;
    }


    public String getHourFromId(int id) {
        String result="";
        try {
            String updateString = "SELECT records.begin_hour " +
                    "FROM records " +
                    "WHERE records.id=? ";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setInt(1,id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                String string = rs.getString("begin_hour");
                return string;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LinkedHashMap<String,Double> getRecord(int id) {

        LinkedHashMap<String,Double> result = new LinkedHashMap<>();

        try {
            String updateString = "SELECT * " +
                    "FROM datas " +
                    "WHERE datas.id=? "+
                    "ORDER BY datas.hour ASC";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setInt(1,id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                String d = rs.getString("hour");
                double value = rs.getDouble("val");

                result.put(d,value);

            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }


    /**
     * Retourne des paires d'id qui coorespondent aux ids des enregistrements de courant actif/amplitude .
     * @param date_of_day
     * @return
     */
    public ArrayList<int[]> getIdOfRecord(String date_of_day) {
        ArrayList<int[]> results = new ArrayList<>();

        try {
            SimpleDateFormat format_of_date = new SimpleDateFormat("dd/MM/yyyy");
            Date d = (Date)format_of_date.parse(date_of_day);

            format_of_date.applyPattern("dd-MM-yyyy");
            String s = format_of_date.format(d);

            java.util.Date dateStr = format_of_date.parse(s);
            java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());

            String updateString = "SELECT records.id " +
                    "FROM records " +
                    "WHERE records.day_of_year=? ";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setDate(1,dateDB);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                int id1 = rs.getInt("id");
                rs.next();
                int id2 = rs.getInt("id");
                int[] id = {id1,id2};
                results.add(id);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    public ArrayList<String> parseDateString(String dates) {
        //exemple : 01/03/2016;04/06/2016;08/08/2016:04/09/2016
        ArrayList<String> result = new ArrayList<>();

        String splitted_first[] = dates.split(";");
        for(String s : splitted_first) {
            if(s.contains(":")) {
                String splitted[] = s.split(":");
                if(splitted.length!=2
                        || !verifDate(splitted[0])
                        || !verifDate(splitted[1]))
                    return null;
                else {
                    //si la chaîne est correcte
                    try {
                        ArrayList<String> between = getDatesInBetween(splitted[0], splitted[1]);
                        result.addAll(between);
                    } catch (ParseException e) {
                        return null;
                    }
                }
            }
            else {
                if(!verifDate(s)) {
                    return null;
                }
                else
                    result.add(s);
            }
        }
        return result;
    }

    private ArrayList<String> getDatesInBetween(String debut,String fin) throws ParseException {
        ArrayList<String> dates = new ArrayList<String>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date  startDate = (Date)formatter.parse(debut);
        Date  endDate = (Date)formatter.parse(fin);
        long interval = 24*1000 * 60 * 60; // 1 hour in millis
        long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
        long curTime = startDate.getTime();
        while (curTime <= endTime) {
            dates.add(formatter.format(new Date(curTime)));
            curTime += interval;
        }
        return dates;
    }

    private boolean verifDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d = (Date)simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void supprimer(ArrayList<String> list) {
        for(String s : list) {
            String[] result = s.split("-",2);//0 date , 1 heure
            java.sql.Date dateDB= new java.sql.Date(0);
            try {
                SimpleDateFormat format_of_date = new SimpleDateFormat("dd/MM/yyyy");
                Date d = (Date) format_of_date.parse(result[0]);

                format_of_date.applyPattern("dd-MM-yyyy");
                String str = format_of_date.format(d);

                java.util.Date dateStr = format_of_date.parse(str);
                dateDB = new java.sql.Date(dateStr.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //get id of records
            int[] id = getIdOfRecordFromDateHour(dateDB,result[1]);

            suppressionRecord(dateDB,result[1]);
            suppressionDatas(id);

        }
    }

    private void suppressionRecord(java.sql.Date day_of_year, String begin_hour) {
        System.out.println(day_of_year.toString());
        System.out.println(begin_hour);
        try {
            String updateString = "DELETE FROM records " +
                    "WHERE records.day_of_year=? AND records.begin_hour=?";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setDate(1,day_of_year);
            preparedStatement.setString(2,begin_hour);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void suppressionDatas(int[] id) {
        System.out.println(id+"");
        try {
            String updateString = "DELETE FROM datas " +
                    "WHERE datas.id=? OR datas.id=?";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setInt(1,id[0]);
            preparedStatement.setInt(2,id[1]);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int[] getIdOfRecordFromDateHour(java.sql.Date day_of_year, String begin_hour) {
        System.out.println(day_of_year.toString());
        System.out.println(begin_hour);
        int[] result = new int[2];//forcément 2 car il y a deux id correspondant : un pour actif un pour amplitude
        try {
            String updateString = "SELECT records.id " +
                    "FROM records " +
                    "WHERE records.day_of_year=? AND records.begin_hour=?";
            PreparedStatement preparedStatement = con.prepareStatement(updateString);

            preparedStatement.setDate(1,day_of_year);
            preparedStatement.setString(2,begin_hour);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                int id1 = rs.getInt("id");
                result[0]=id1;
                rs.next();
                int id2 = rs.getInt("id");
                result[1] = id2;
                System.out.println(id1+" "+id2);
                return result;

            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createLog(ArrayList<ResultatRecherche> r) {
        String result="";

        for(int i=0;i<r.size();i++) {
            HashMap<String,Double> actif = r.get(i).getActif_graphe();
            HashMap<String,Double> amplitude = r.get(i).getAmplitude_graphe();
            int size = actif.size();

            String[] array = actif.keySet().toArray(new String[size]);

            for(int j=0;j<size;j++) {
                result = result+array[j]+"    -    Courant actif : "+actif.get(array[j])+"    -    Amplitude : "+amplitude.get(array[j])+"\n";
            }
        }
        return result;
    }

    public ArrayList<Double> getValues(HashMap<String,Double> hash) {
        String[] keys = hash.keySet().toArray(new String[hash.size()]);
        ArrayList<Double> result = new ArrayList<>();
        for(int i=0;i<keys.length;i++) {
            result.add(hash.get(keys[i]));
        }
        return result;
    }

    public void enregistrerLog(String log,String kind) {
        try {
            FileUtils.writeStringToFile(new File("log."+kind), log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
