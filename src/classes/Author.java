package classes;


import static classes.Book.instantiate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Author extends DBEntity{
    
    // attributes
    
    private int id;
    private String name;
    private String firstName;
    private String lastName;
    private int age;
    private ArrayList<Book> books = new ArrayList<>();
    
    //public static methods
    
    public static Author instantiate(Connection conn, ResultSet result) throws SQLException
    {
        int id = result.getInt("id");
        String name = result.getString("name");
        String firstName = result.getString("first_name");
        String lastName = result.getString("last_name");
        int age = result.getInt("age");
        ArrayList<Book> books = Author.getBooksByAuthor(conn, id);
        
        return new Author(id, name, firstName, lastName, age, books);
    }
    
    public static ArrayList<Author> all() {
        
        String query = "SELECT * FROM author";
        try
        (
            Connection conn = DB.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet results = stmt.executeQuery(query);
        )
        {
            System.out.println(query);
            ArrayList<Author> authors = new ArrayList<>();
            
            while(results.next())
            {
                Author newAuthor = instantiate(conn, results);
                for(Book book : newAuthor.getBooks())
                {
                    book.getAuthors().add(newAuthor);
                }
                authors.add(newAuthor);
            }
            
            return authors;
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        return null;
    }
    
    public static Author findByID(int searchedID)
    {
        String query = "SELECT * FROM author WHERE id=" + searchedID;
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
                Author newAuthor = instantiate(conn, result);
                for(Book book : newAuthor.getBooks())
                {
                    book.getAuthors().add(newAuthor);
                }
                return newAuthor;
            }
            
            return null;
           
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        return null;
    }
    
    public static Author findByName(Search criteria, String authorName)
    {
        if(authorName.trim().equals("")) return null;
        
        String query = "";
        String name = "";
        String firstName = "";
        String lastName = "";
        
        switch(criteria)
        {
            case NAME:
                name = authorName.trim();
                query = "SELECT * FROM author WHERE name='" + name + "'";
                break;
            case SURNAME:
                firstName = authorName.split(" ")[0];
                lastName = authorName.split(" ")[1];
                query = "SELECT * FROM author WHERE first_name='" + firstName + "' AND last_name='" + lastName + "'";
                break;
            case FIRST_NAME:
                firstName = authorName.trim();
                query = "SELECT * FROM author WHERE first_name='" + firstName + "'";
                break;
            case LAST_NAME:
                lastName = authorName.trim();
                query = "SELECT * FROM author WHERE last_name='" + lastName + "'";
                break;
        }
        
        ArrayList<Author> foundedAuthors = executeSelectQuery(query);
        
        if(foundedAuthors.size() > 0)
        {
            return executeSelectQuery(query).get(0);
        }
        else
        {
            return null;
        }
    }
    
    private static ArrayList<Book> getBooksByAuthor(Connection conn, int authorID)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT book.* FROM book ");
        query.append("JOIN authorBook ON authorBook.book_id = book.id ");
        query.append("JOIN author ON authorBook.author_id = author.id ");
        query.append("WHERE author.id=" + authorID);

        try
        (
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet results = stmt.executeQuery(query.toString());
        )
        {
            System.out.println(query);
            ArrayList<Book> books = new ArrayList<>();
            while(results.next())
            {
                books.add(resultToBook(results));
            }
            
            return books;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    private static Book resultToBook(ResultSet result) throws SQLException
    {
        int id = result.getInt("id");
        String name = result.getString("name");
        double price = result.getDouble("price");
        String publisher = result.getString("publisher");
        int edition = result.getInt("edition");
        String language = result.getString("language");
        String ISBN = result.getString("ISBN");
        
        return new Book(id, name, price, publisher, edition, language, ISBN);
    }
    
    private static ArrayList<Author> executeSelectQuery(String query)
    {
        try
        (
            Connection conn = DB.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet results = stmt.executeQuery(query);
        )
        {
            System.out.println(query);
            ArrayList<Author> authors = new ArrayList<>();
            while(results.next())
            {
                authors.add(instantiate(conn, results));
            }
            
            return authors;
        }
        catch(SQLException e)
        {
            System.out.println(query);
            System.out.println(e);
            return null;
        }
    }

    // contructor
    
    public Author(int id, String name, String firstName, String lastName, int age, ArrayList<Book> books) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.books = books;
    }
    
    public Author(String name, String firstName, String lastName, int age) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
    
    public Author(int id, String name, String firstName, String lastName, int age) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
 
    // public methods
    @Override
    public boolean insert()
    {
        String query = "INSERT INTO author(name, first_name, last_name, age) VALUES(?,?,?,?)";
        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        )
        {
            stmt.setString(1, name);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setInt(4, age);
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
    
    @Override
    public boolean update()
    {
        String query = "UPDATE author SET name=?, first_name=?, last_name=?, age=? WHERE id=?";
        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        )
        {
            stmt.setString(1, name);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setInt(4, age);
            stmt.setInt(5, id);
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
    
    @Override
    public boolean save()
    {
        // if ID already exists means the book was inserted previously
        return id != 0 ? update() : insert();
    }
    
    @Override
    public boolean delete()
    {
        String query1 = "DELETE FROM author WHERE id=?";
        String query2 = "DELETE FROM authorBook WHERE author_id=?";
        try(
                Connection conn = DB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query1);
                PreparedStatement authorBookStmt = conn.prepareStatement(query2);
            )
        {
            authorBookStmt.setInt(1, id);
            authorBookStmt.executeUpdate();
            System.out.println(query2);
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println(query1);
            
            return true;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return false;
        }
    }
    
    public void addBook(Book book)
    {
        if(repeatedBook(book))
        {
            return;
        }
        
        // add book-author relation to the join table and to the books ArrayList
        String query = "INSERT INTO authorBook (author_id, book_id) VALUES(?,?)";
        
        book.save();
        save();
        
        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        )
        {
            stmt.setInt(1, id);
            stmt.setInt(2, book.getID());
            stmt.executeUpdate();
            System.out.println(query);
            books.add(book);
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
    
    public void removeBook(Book book)
    {
        // delete book-author relation in the join table
        
        String query = "DELETE FROM authorBook WHERE author_id=? AND book_id=?";
        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        )
        {
            stmt.setInt(1, id);
            stmt.setInt(2, book.getID());
            stmt.executeUpdate();
            System.out.println(query);
            
            //remove the author from the ArrayList
            books.remove(book);
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
    
    // getters and setters

    public int getID() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
    
    public String upperCase(String string)
    {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
    
    @Override
    public String toString()
    {
//        return String.format("%d, %s, %s, %s, %d", id, name, firstName, lastName, age);
        return String.format("%s %s, %s", upperCase(name), upperCase(lastName), upperCase(name));
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof Author)) return false;
        
        Author author = (Author)o;
        return this.id == author.id;
    }
    
    private boolean repeatedBook(Book b)
    {
        for(Book book : books)
        {
            if(book.equals(b))
            {
                return true;
            }
        }
        return false;
    }
}