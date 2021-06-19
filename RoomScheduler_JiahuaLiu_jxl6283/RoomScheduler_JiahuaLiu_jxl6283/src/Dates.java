
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Dates {
    
    //Prettymuch same as Faculty
    
    private static Connection connection;
    private static PreparedStatement addDate;
    private static PreparedStatement getDateList;
    private static ResultSet resultSet;
    
    public static boolean add_Dates(Date date)
    {
        connection = DBConnection.getConnection();
        try
        {
            addDate = connection.prepareStatement("insert into dates (date) values (?)");
            addDate.setDate(1, date);
            
            addDate.executeUpdate();
            
            
            return true;
            //similar to Faculty class, determine whatever it is addding successfully, giving feedback
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return false;
        
    }
    
    public static ArrayList<Date> get_Dates()
    {
        connection = DBConnection.getConnection();
        ArrayList<Date> dates = new ArrayList<Date>();
        try
        {
            getDateList = connection.prepareStatement("select date from dates order by date");
            resultSet = getDateList.executeQuery();
            
            while(resultSet.next())
            {
                dates.add(resultSet.getDate(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return dates;
        
    }
}
