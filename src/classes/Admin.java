package classes;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Admin {
    
    private int id;
    private String name;
    private String username;
    private String password;

    public Admin(int id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }
    
    public Admin(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
    
    public static Admin authenticate(String username, String password)
    {
        String query = "SELECT * FROM admin WHERE username=? AND password=? LIMIT 1";
        ResultSet rs = null;
        
        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        )
        {
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            
            if(rs.next())
            {
                return instantiate(conn, rs);
            }
            
            return null;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;
        }
        finally
        {
            try
            {
                if(rs != null)
                {
                    rs.close();
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            
        }
    }
    
    public static Admin instantiate(Connection conn, ResultSet result) throws SQLException
    {
        int id = result.getInt("id");
        String name = result.getString("name");
        String username = result.getString("username");
        String password = result.getString("password");
        
        return new Admin(id, name, username, password);
    }
    
    public static ArrayList<Admin> all()
    {   
        String query = "SELECT * FROM admin";
        
        try
        (
            Connection conn = DB.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
        )
        {
            ArrayList<Admin> admins = new ArrayList<>();
            
            while(rs.next())
            {        
                admins.add(Admin.instantiate(conn, rs));
            }
            
            return admins;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    public static Admin findByID(int searchedID)
    {
        String query = "SELECT * FROM admin WHERE id=" + searchedID;
        try
        (
            Connection conn = DB.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(query);
        )
        {
            System.out.println(query);
            if(result.next())
            {
                Admin newAdmin = instantiate(conn, result);
                
                return newAdmin;
            }
            
            return null;
           
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        return null;
    }
    
    public boolean insert()
    {
        String query = "INSERT INTO admin (name, username, password) VALUES(?,?,?)";

        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        )
        {
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.executeUpdate();
            System.out.println(query);
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if(generatedKeys.next())
            {
                int id = generatedKeys.getInt(1); // id column
                this.id = id;
            }
            
            return true;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return false;
        }
    }
    
    public boolean update()
    {
        String query = "UPDATE admin SET name=?, username=?, password=?,  WHERE id=?";
        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        )
        {
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.executeUpdate();
            System.out.println(query);
            
            return true;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return false;
        }
    }
    
     public boolean save()
    {
        // if ID already exists means the book was inserted previously
        return id != 0 ? update() : insert();
    }
     
    public boolean delete()
    {
        String query = "DELETE FROM admin WHERE id=?";
        
        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        )
        {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println(query);
            
            return true;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return false;
        }
    }

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
