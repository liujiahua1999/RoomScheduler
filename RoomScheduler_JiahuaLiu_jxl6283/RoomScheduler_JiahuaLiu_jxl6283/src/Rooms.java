
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class Rooms {
    //RoomEntry
    
    
    private String name;
    private int seats;
    
    public Rooms() {
    }
    
    public Rooms(String name, int seats) {
        this.name = name;
        this.seats = seats;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getSeats() {
        return this.seats;
    }
    
    //define equals
    //fix waitlist disfunctional
    public boolean equals (Object o){
        Rooms room = (Rooms)o;
        return name.equals(room.name);
    }
}



class Room_Queries {
    
    private static Connection connection;
    private static PreparedStatement add_Room;
    
    private static PreparedStatement get_Room_List;
    private static ResultSet resultSet;
    
    public static ArrayList<Rooms> get_All_Rooms() {
        connection = DBConnection.getConnection();
        ArrayList<Rooms> rooms = new ArrayList<Rooms>();
        try
        {
            get_Room_List = connection.prepareStatement("select name, seats from rooms order by seats");
            resultSet = get_Room_List.executeQuery();
            
            while(resultSet.next())
            {
                Rooms room = new Rooms(resultSet.getString(1), resultSet.getInt(2));
                rooms.add(room);
            }
            //fetch all rooms
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        //return data
        return rooms;
    } 
    
    public static void add_Room(String room, int seats) {

        connection = DBConnection.getConnection();
        try
        {
            add_Room= connection.prepareStatement("insert into rooms (name, seats) values (?,?)");
            add_Room.setString(1, room);
            add_Room.setInt(2, seats);
            add_Room.executeUpdate();

        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static void drop_Room(String room) {
        //find room and drop
        System.out.println("Room: "+room);
        connection = DBConnection.getConnection();
        ArrayList<Waitlist> waitlist = new ArrayList<>();
        int i = 0;
        //avoid error
        try
        {
            get_Room_List = connection.prepareStatement("delete from rooms where name = ?");
            get_Room_List.setString(1, room);
            i = get_Room_List.executeUpdate();
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
}

