package classes;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Book extends DBEntity {
    
    // atributes
    private int id;
    private String name;
    private ArrayList<Author> authors = new ArrayList<>();
    private double price;
    private String publisher;
    private int edition;
    private String language;
    private String ISBN;
    
    //public static methods
    
    public static Book instantiate(Connection conn, ResultSet result) throws SQLException
    {
        int id = result.getInt("id");
        String name = result.getString("name");
        ArrayList<Author> authors = Book.getAuthorsByBook(conn, id);
        double price = result.getDouble("price");
        String publisher = result.getString("publisher");
        int edition = result.getInt("edition");
        String language = result.getString("language");
        String ISBN = result.getString("ISBN");
        
        return new Book(id, name, authors, price, publisher, edition, language, ISBN);
    }

    public static ArrayList<Book> all() {
        
        String query = "SELECT * FROM book ORDER BY name";
        try
        (
            Connection conn = DB.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet results = stmt.executeQuery(query);
        )
        {
            System.out.println(query);
            ArrayList<Book> books = new ArrayList<>();
            
            while(results.next())
            {
                Book newBook = instantiate(conn, results);
                for(Author author : newBook.authors)
                {
                    author.getBooks().add(newBook);
                }
                books.add(newBook);
            }
            
            return books;
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        return null;
    }
    
    public static Book findByID(int searchedID)
    {
        String query = "SELECT * FROM book WHERE id=" + searchedID;
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
                Book newBook = instantiate(conn, result);
                for(Author author : newBook.authors)
                {
                    author.getBooks().add(newBook);
                }
                return newBook;
            }
            
            return null;
           
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        return null;
    }
    
    public static ArrayList<Book> findByStringField(String field, String value)
    {
        String query = "SELECT * FROM book WHERE " + field + " LIKE '%" + value + "%' ORDER BY name";
        
        return executeSelectQuery(query);
    }
    
    public static ArrayList<Book> findByIntField(String field, int value)
    {
        String query = "SELECT * FROM book WHERE " + field + "=" + value + " ORDER BY name";
        
        return executeSelectQuery(query);
    }
    
    public static ArrayList<Book> findByPrice(Search criteria, double price)
    {
        String query = "";
        
        switch(criteria)
        {
            case LESS_THAN:
                query = "SELECT * FROM book WHERE price <=" + price;
                break;
            case EQUAL_TO:
                query = "SELECT * FROM book WHERE price=" + price;
                break;
            case GREATER_THAN:
                query = "SELECT * FROM book WHERE price >=" + price;
                break;
        }
        
        return executeSelectQuery(query);
    }
    
    // private static methods
    
    private static ArrayList<Author> getAuthorsByBook(Connection conn, int bookID)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT author.* FROM author ");
        query.append("JOIN authorBook ON authorBook.author_id = author.id ");
        query.append("JOIN book ON authorBook.book_id = book.id ");
        query.append("WHERE book.id=" + bookID);
        
//        System.out.println("\ngetAuthorsByBook query:");
//        System.out.println(query);
        
        try
        (
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet results = stmt.executeQuery(query.toString());
        )
        {
            System.out.println(query);
            ArrayList<Author> authors = new ArrayList<>();
            while(results.next())
            {
                authors.add(resultToAuthor(results));
            }
            
            return authors;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    private static Author resultToAuthor(ResultSet result) throws SQLException
    {
        int id = result.getInt("id");
        String name = result.getString("name");
        String firstName = result.getString("first_name");
        String lastName = result.getString("last_name");
        int age = result.getInt("age");
        
        return new Author(id, name, firstName, lastName, age);
    }
    
    private static ArrayList<Book> executeSelectQuery(String query)
    {
        try
        (
            Connection conn = DB.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet results = stmt.executeQuery(query);
        )
        {
            System.out.println(query);
            ArrayList<Book> books = new ArrayList<>();
            while(results.next())
            {
                books.add(instantiate(conn, results));
            }
            
            return books;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    // constructor

    // instantiate methot constructor
    public Book(int id, String name, ArrayList<Author> authors, double price, String publisher, int edition, String language, String ISBN) {
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.price = price;
        this.publisher = publisher;
        this.edition = edition;
        this.language = language;
        this.ISBN = ISBN;
    }
    
    // user constructor
    public Book(String name, double price, String publisher, int edition, String language, String ISBN) {
        this.name = name;
        this.price = price;
        this.publisher = publisher;
        this.edition = edition;
        this.language = language;
        this.ISBN = ISBN;
    }
    
    // resultToBook constructor
    public Book(int id, String name, double price, String publisher, int edition, String language, String ISBN) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.publisher = publisher;
        this.edition = edition;
        this.language = language;
        this.ISBN = ISBN;
    }
    
    // public methods
    
    public boolean insert()
    {
        String query = "INSERT INTO book(name, price, publisher, edition, language, ISBN) VALUES(?,?,?,?,?,?)";
        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        )
        {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, publisher);
            stmt.setInt(4, edition);
            stmt.setString(5, language);
            stmt.setString(6, ISBN);
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
        String query = "UPDATE book SET name=?, price=?, publisher=?, edition=?, language=?, ISBN=? WHERE id=?";
        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        )
        {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, publisher);
            stmt.setInt(4, edition);
            stmt.setString(5, language);
            stmt.setString(6, ISBN);
            stmt.setInt(7, id);
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
        String query1 = "DELETE FROM book WHERE id=?";
        String query2 = "DELETE FROM authorBook WHERE book_id=?";
        try
        (
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
    
    public void addAuthor(Author author)
    {
        if(repeatedAuthor(author))
        {
            return;
        }
        
        // add author-book relation to the join table and to the authors ArrayList
        String query = "INSERT INTO authorBook (author_id, book_id) VALUES(?,?)";
        
        author.save();
        save();
        
        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        )
        {
            stmt.setInt(1, author.getID());
            stmt.setInt(2, id);
            stmt.executeUpdate();
            System.out.println(query);
            authors.add(author);
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
    }
    
    public void removeAuthor(Author author)
    {
        // delete author-book relation in the join table
        
        String query = "DELETE FROM authorBook WHERE book_id=? AND author_id=?";
        try
        (
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        )
        {
            stmt.setInt(1, id);
            stmt.setInt(2, author.getID());
            stmt.executeUpdate();
            System.out.println(query);
            
            //remove the author from the ArrayList
            authors.remove(author);
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
    
    public void removeAllAuthors()
    {
        for(int i = 0; i < authors.size(); i++)
        {
            removeAuthor(authors.get(i));
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

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    } 
    
    @Override
    public String toString()
    {
        return String.format("%d, %s, %f, %s, %d", id, name, price, publisher, 
                edition, language, ISBN);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof Book)) return false;
        
        Book book = (Book)o;
        return this.id == book.id;
    }
    
    private boolean repeatedAuthor(Author a)
    {
        for(Author author : authors)
        {
            if(author.equals(a))
            {
                return true;
            }
        }
        return false;
    }
}