package kis.lab3.server;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBServer {

    private Connection con = null; // соединение с БД 
    private Statement stmt = null; // оператор 

    public DBServer(String DBName, String ip, int port) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
        try {
            System.out.println("подключаю драйвер");
            Class.forName("com.mysql.jdbc.Driver").newInstance();        
            String url = "jdbc:mysql://"+ip+":"+port+"/"+DBName;// устанавливаю соединение с БД  
            System.out.println("устанавливаю соединение с БД");
            con = DriverManager.getConnection(url, "root", "aker6230"); 
            stmt = con.createStatement();
        } catch (SQLException e)
        {
            System.out.println("Невозможно подключиться к базе данных!");
            System.out.println(" >> "+e.getMessage());
             System.exit(0);

        } 
    };
    public String ShowNews(int id_fk){
        String sql = "select * from news where category_id_fk = " + id_fk ; 
        ResultSet rs; 
        String Result = "0#";
        try {
            rs = stmt.executeQuery(sql);

            while (rs.next()) 
            { 
                int id = rs.getInt("news_id");
                String name = rs.getString("title");
                String fKey = rs.getString("category_id_fk");
                Result += (id + " " + name + " "+ fKey+ "\n");
            } 
            rs.close(); 
        } catch (SQLException ex) {
            return ex.getMessage()+'#error';
        }
        return Result;
    };
    public String ShowCategories() {
        String sql = "select * from category"; 
        String Result = "0#";
        ResultSet rs; 
        try {
            rs = stmt.executeQuery(sql);
       
        while (rs.next()) 
        { 
            int id = rs.getInt("category_id");
            String name = rs.getString("title");
            Result += id + " " + name +"\n";
        } 
        rs.close();
         } catch (SQLException ex) {
            return ex.getMessage()+'#error';
        }
        return Result;
    };
 
    public String FindNews(int news_id){
        String sql = "select * from news where news_id = " + news_id; 
        ResultSet rs; 
        try {
            rs = stmt.executeQuery(sql);
        
            while (rs.next()) 
            { 
                int id = rs.getInt("news_id");
                String name = rs.getString("title");
                return id + " " + name;
            } 
            rs.close();
        } 
        catch (SQLException ex) {
            Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);}
        return null;
    }; 
    public int addNews(String title, String text, String data, int category_id ){
        return addNews(0, title, text, data, category_id );
    };
    public int addNews(int id, String title, String text, String data, int category_id ){
        String id_string;
        if (id==0) id_string = "null";
        else id_string = Integer.toString(id);
            
        String sql = "INSERT INTO `news_agency`.`news` (`news_id`, `title`, `text`, `data`, `category_id_fk`) "
                 + "VALUES ("+id_string+", '"+title+"', '"+text+"', '"+data+"', '"+ category_id+"')";
        try 
        { 
            stmt.executeUpdate(sql);
            System.out.println("Новость "+title+ " успешно добавлена!"); 
            return 0;
        } catch (SQLException e)
        {
            System.out.println("ОШИБКА! Новость "+title+ " не добавлена!");
            System.out.println(" >> "+e.getMessage());
            
            return e.getErrorCode();
        } 
    };
    public int addCategory(int id, String title ){

        String sql = "INSERT INTO `news_agency`.`category` (`category_id`, `title`) "
                 + "VALUES ("+id +", '"+title+"')";
        try 
        { 
            stmt.executeUpdate(sql);
            System.out.println("Категория "+title+ " успешно добавлена!"); 
            return 0;
        } catch (SQLException e)
        {
            System.out.println("ОШИБКА! Категория "+title+ " не добавлена!");
            System.out.println(" >> "+e.getMessage());
            return e.getErrorCode();
        } 
    };
    public int deleteCategory(int id){
        String sql = "DELETE FROM CATEGORY WHERE category_id = "+id;
        try
            {
                int c = stmt.executeUpdate(sql);
                if (c>0){
                    System.out.println("Категория с идентификатором "+ id +" успешно удалена!");
                    return 0;
                } 
                else{
                    System.out.println("Категория с идентификатором "+ id +" не найдена!");
                    return 1;
                }
            } catch (SQLException e){
                System.out.println("ОШИБКА при удалении категории с идентификатором "+id);
                System.out.println(" >> "+e.getMessage());
                return e.getErrorCode();
            }
        };
    public int deleteNews(int id){
        String sql = "DELETE FROM NEWS WHERE news_id = "+id;
        try
            {
                int c = stmt.executeUpdate(sql);
                if (c>0){
                    System.out.println("Новость с идентификатором "+ id +" успешно удалена!");
                    return 0;
                } 
                else{
                    System.out.println("Новость с идентификатором "+ id +" не найдена!");
                    return 1;
                }
            } catch (SQLException e){
                System.out.println("ОШИБКА при удалении новости с идентификатором "+id);
                System.out.println(" >> "+e.getMessage());
                return 1;
            }
        };
    public int UpdateNews(int id, String title, String text){
         String sql = "UPDATE  `news_agency`.`news`"
                 + " SET  `text` = '"+text+"',  "
                 + " `title` = '"+title+"' " 
                 + "WHERE  news_id = " + id;
        try
            {
                int c = stmt.executeUpdate(sql);
                if (c>0){
                    System.out.println("Новость с идентификатором "+ id +" успешно изменена!");
                    return 0;
                } 
                else{
                    System.out.println("Новость с идентификатором "+ id +" не найдена!");
                    return 1;
                }
            } catch (SQLException e){
                System.out.println("ОШИБКА при изменении новости с идентификатором "+id);
                System.out.println(" >> "+e.getMessage());
                System.out.println(" >> "+e.getErrorCode());
                return e.getErrorCode();
               // return 1;
            }
    };
    public int CountNews(int id){
        String sql = "SELECT COUNT( * ) as count FROM  `news` WHERE  `category_id_fk` =" + id; 
        int count = 0;
        try {
        ResultSet rs = stmt.executeQuery(sql); 
        
        while (rs.next()) 
        { 
            count = rs.getInt("count");
            System.out.println(id + " " + count );
        } 
        rs.close(); 
        }
        catch (SQLException e){ return e.getErrorCode();}
        return count;
    }; 
    
    
    public boolean Disconnect() throws SQLException{
        con.close();  // завершаю соединение
        return true;
    };
  
}
