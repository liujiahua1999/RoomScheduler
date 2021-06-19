
import java.sql.Timestamp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class Reservation 

{
    //Reservation entry
    
        private final int seats;
        //添加新的field，用来保存预定时客户标注的需求座位数
        private int expection;
        private final String faculty;
        private final String room;
        private final Date date;
        private final Timestamp timestamp;
    

    public Reservation(String faculty, String room, Date date, int seats, Timestamp timestamp, int expection) {
        this.faculty = faculty;
        this.room = room;
        this.date = date;
        this.seats = seats;
        this.timestamp = timestamp;
        this.expection = expection;
    }
    
    public String get_Faculty() {
        return this.faculty;
    }
    
    public int get_Seats() {
        return this.seats;
    }
        
    public String get_Room() {
        return this.room;
    }
    
    public Date get_Date() {
        return this.date;
    }
    
    public int get_Expection(){
        return this.expection;
    }

    public Timestamp get_Timestamp() {
        return this.timestamp;
    }
    
}


class Reservation_Queries {
    
    private static Connection connection;
    private static PreparedStatement Add_Reservation;
    private static PreparedStatement Reservation_List;
    private static PreparedStatement Room_List;
    private static ResultSet resultSet;
    
    public static ArrayList<Reservation> get_Reservations_By_Date(Date date) {
        connection = DBConnection.getConnection();
        ArrayList<Reservation> Reserve = new ArrayList<>();
        
            try
            {
                Reservation_List = connection.prepareStatement("select faculty, room, seats, timestamp, expection from reservations where date = ?");
                Reservation_List.setDate(1, date);
                resultSet = Reservation_List.executeQuery();

                while(resultSet.next())
                {
                    Reservation reserve = new Reservation(resultSet.getString(1), resultSet.getString(2), date, resultSet.getInt(3), resultSet.getTimestamp(4), resultSet.getInt(5));
                    Reserve.add(reserve);
                }
            }
        //return the ReservationList by date
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return Reserve;
    }

    
    public static ArrayList<Rooms> get_Rooms_Reserved_By_Date(Date date) {
        connection = DBConnection.getConnection();
        ArrayList<Rooms> rooms = new ArrayList<Rooms>();
        
        //System.out.println(date);
        //print out date stat, easier debugging
        try
        {
            Room_List = connection.prepareStatement("select room, seats from reservations where date = ?");
            Room_List.setDate(1, date);
            resultSet = Room_List.executeQuery();
            
            while(resultSet.next())
            {
                Rooms room = new Rooms(resultSet.getString(1), resultSet.getInt(2));
                rooms.add(room);
                //return Rooms list by date
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        
        }
        return rooms;
    }
    
    /**
     * Add a reservation
     * @param F
     * @param Dates
     * @param S
     * @return true if successfully added, false if out of room and the faculty should be added into the wait list
     */
    public static boolean add_Reservation_Entry(String F, Date Dates, int S) {
        connection = DBConnection.getConnection();
        //Room reservation
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        //Get current time
        
        //c = 0;
        //Avoid EOF error
        
        ArrayList<Rooms> Rooms = Room_Queries.get_All_Rooms();

        for (int i = 0; i < Rooms.size(); i++) {
            
            Rooms R = Rooms.get(i);
            
            if (R.getSeats() >= S) {
                //seat number comparison
                ArrayList<Rooms> rooms_Reserved = get_Rooms_Reserved_By_Date(Dates);
                
                //System.out.println(rooms_Reserved.size() + " " + rooms_Reserved.contains(R));
                //print out the operation. The R status
                if (rooms_Reserved.isEmpty() || !rooms_Reserved.contains(R)) {
                    //if Rooms is empty or contained = False
                    
                    try
                    {
                        Add_Reservation = connection.prepareStatement("insert into reservations (faculty, room, date, seats, timestamp, expection) values (?,?,?,?,?,?)");
                        Add_Reservation.setString(1, F);
                        Add_Reservation.setString(2, R.getName());
                        Add_Reservation.setDate(3, Dates);
                        Add_Reservation.setInt(4, R.getSeats());
                        Add_Reservation.setTimestamp(5, currentTimestamp);
                        Add_Reservation.setInt(6, S);
                        //c = 
                        Add_Reservation.executeUpdate();
                        return true;
                        //return true if successfully added

                    }
                    //Catch
                    catch(SQLException sqlException){
                        sqlException.printStackTrace();
                    }
                    //return true;
                }
            }
        }
        return false;
        //false otherwaise, add to waitlist
    }
    
    public static void cancel_Reservation(String F, Date date) {
        connection = DBConnection.getConnection();
        int i = 0;
        try
        {
            Room_List = connection.prepareStatement("delete from reservations where faculty = ? and date = ?");
            Room_List.setString(1, F);
            Room_List.setDate(2, date);
            i = Room_List.executeUpdate();
            
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<Reservation> get_Reservations_By_Faculty(String F) {
        //similar to get_Rooms_Reserved_By_Date
        connection = DBConnection.getConnection();
        ArrayList<Reservation> reservation = new ArrayList<Reservation>();
        try
        {
            Reservation_List = connection.prepareStatement("select room, date, seats, timestamp, expection from reservations where faculty = ?");
            Reservation_List.setString(1, F);
            
            resultSet = Reservation_List.executeQuery();
            
            while(resultSet.next())
            {
                
                Reservation reserve = new Reservation(F, resultSet.getString(1), resultSet.getDate(2), resultSet.getInt(3), resultSet.getTimestamp(4), resultSet.getInt(5));
                reservation.add(reserve);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        return reservation;
    }
    
    public static void delete_Reservation(String room) {connection = DBConnection.getConnection();
    
        //Avoid EOF error
        int i = 0;
        
            try{
                Room_List = connection.prepareStatement("delete from reservations where room = ?");
                Room_List.setString(1, room);
                i = Room_List.executeUpdate();

            }
            
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
        public static ArrayList<Reservation> get_Reservations_By_Room(String R) {
        //similar to get_Rooms_Reserved_By_Date
        connection = DBConnection.getConnection();
        ArrayList<Reservation> reservation = new ArrayList<Reservation>();
        try
        {
            Reservation_List = connection.prepareStatement("select faculty, date, seats, timestamp, expection from reservations where room = ?");
            Reservation_List.setString(1, R);
            
            resultSet = Reservation_List.executeQuery();
            
            while(resultSet.next())
            {
                
                Reservation reserve = new Reservation( resultSet.getString(1),R, resultSet.getDate(2), resultSet.getInt(3), resultSet.getTimestamp(4),resultSet.getInt(5));
                reservation.add(reserve);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return reservation;
    }
    
}

