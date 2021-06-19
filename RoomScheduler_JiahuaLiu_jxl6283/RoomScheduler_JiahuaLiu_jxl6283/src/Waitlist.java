

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;

public class Waitlist {
    //Waitlist Entry
    
    private final String faculty;
    private final Date dates;
    private final int seats;
    private final Timestamp timestamp;
    
    public Waitlist(String faculty, Date dates, int seats, Timestamp timestamp) {
        this.faculty = faculty;
        this.dates = dates;
        this.seats = seats;
        this.timestamp = timestamp;
    }
    

    public String get_Faculty() {
        return this.faculty;
    }

    public Timestamp get_Times() {
        return this.timestamp;
    }
    
    public Date get_Date() {
        return this.dates;
    }
    
    public int get_Seats() {
        return this.seats;
    }
    
    
    
}
class Waitlist_Queries {
    
    private static Connection connection;
    private static PreparedStatement getWaitlist;
    private static PreparedStatement addWaitlist;
    private static ResultSet resultSet;
    
    

         public static ArrayList<Waitlist> All() 
        {connection = DBConnection.getConnection();
            
                      ArrayList<Waitlist> listing = new ArrayList<>();
            try
            {
                getWaitlist = connection.prepareStatement("select faculty, date, seats, timestamp from waitlist order by date, timestamp");
                
                
                resultSet = getWaitlist.executeQuery();

                while(resultSet.next())
                {
                    Waitlist entry = new Waitlist(resultSet.getString(1), resultSet.getDate(2), resultSet.getInt(3), resultSet.getTimestamp(4));
                    listing.add(entry);
                }
                
                
            }
            
       
            catch(SQLException sqlException)
            {
                sqlException.printStackTrace();
            }
            return listing;
        }
         
         
         
        public static ArrayList<Waitlist> Waitlist_Faculty(String faculty) 
        {connection = DBConnection.getConnection();
        
                      ArrayList<Waitlist> Listing = new ArrayList<>();
        try
        {
                getWaitlist = connection.prepareStatement("select date, seats, timestamp from waitlist where faculty = ?");
                getWaitlist.setString(1, faculty);
                
                resultSet = getWaitlist.executeQuery();
            
                
                
            while(resultSet.next())
            {
                
                    Waitlist entry = new Waitlist(faculty, resultSet.getDate(1), resultSet.getInt(2), resultSet.getTimestamp(3));
                    Listing.add(entry);

            }
        }
        

        
                    catch(SQLException sqlException)
                    {
                        sqlException.printStackTrace();
                    }
                    return Listing;
        }

        

    
    public static ArrayList<Waitlist> Waitlist_Date(Date date) {connection = DBConnection.getConnection();
                  ArrayList<Waitlist> waitlist = new ArrayList<>();
                  

        try
        {
            getWaitlist = connection.prepareStatement("select faculty, seats, timestamp from waitlist where date = ?");
            getWaitlist.setDate(1, date);
            resultSet = getWaitlist.executeQuery();
            
            while(resultSet.next())
            {
                Waitlist entry = new Waitlist(resultSet.getString(1), date, resultSet.getInt(2), resultSet.getTimestamp(3));
                waitlist.add(entry);
               
            }
        }
        

        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return waitlist;
    }
    

    
    public static void add_Waitlist_Entry(String faculty, Date date, int seats) {connection = DBConnection.getConnection();
                        java.sql.Timestamp NOW = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                        // declare a timestamp field.
        
                    ArrayList<Rooms> Rooms = Room_Queries.get_All_Rooms();
                    
                    
        try {

            addWaitlist = connection.prepareStatement("insert into waitlist ( timestamp,seats,  faculty, date) values (?,?,?,?)");
            addWaitlist.setInt(2, seats);
            addWaitlist.setTimestamp(1, NOW);
            addWaitlist.setString(3, faculty);
            addWaitlist.setDate(4, date);

            
            addWaitlist.executeUpdate();
            //return True;
            
        }
        
        
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        //return False;
         
    }
    
    
    public static void delete_Waitlist_Entry(String faculty, Date date) {connection = DBConnection.getConnection();
                       ArrayList<Waitlist> waitlist = new ArrayList<>();
                       
                       int i = 0;
        try
        {
            
            getWaitlist = connection.prepareStatement("delete from waitlist where date = ? and faculty = ?");
          
            getWaitlist.setDate(1, date);
            getWaitlist.setString(2, faculty);
            i = getWaitlist.executeUpdate();
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    //Help avoid EOF error when add room
    //
        public static void EOF_delete_Waitlist_Entry(String faculty, Date date) {
        connection = DBConnection.getConnection();
        int count = 0;
        try
        {
            getWaitlist = connection.prepareStatement("delete from waitlist where faculty = ? and date = ?");
            getWaitlist.setString(1, faculty);
            getWaitlist.setDate(2, date);
            count = getWaitlist.executeUpdate();
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}


