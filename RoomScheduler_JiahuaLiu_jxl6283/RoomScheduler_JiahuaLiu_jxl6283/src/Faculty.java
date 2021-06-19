
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Faculty
        
        //Prettymuch same as Template
        
{
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement addFaculty;
    private static PreparedStatement getFacultyList;
    private static ResultSet resultSet;
    

    public static boolean add_Faculty(String name)
    {
        connection = DBConnection.getConnection();
        try
        {
            addFaculty = connection.prepareStatement("insert into faculty (name) values (?)");

            addFaculty.setString(1, name);
            addFaculty.executeUpdate();
            //adding successfully 
            return true;
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        //return failed to add
        return false;
        // adding True and False value inoder to pop out dialog box to tell whatever it is adding successfully or not
    }
    
    public static ArrayList<String> get_Faculty()
    {
        connection = DBConnection.getConnection();
        ArrayList<String> faculty = new ArrayList<String>();
        try
        {
            getFacultyList = connection.prepareStatement("select name from faculty order by name");

            resultSet = getFacultyList.executeQuery();
            
            while(resultSet.next())
            {
                faculty.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return faculty;
        
    }
    
    
}