package kis.lab3.server;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KISLab3Server {
    private ServerSocket server = null; 
    private Socket sock = null; 
    private DBServer DB = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;


    public void Start(String DBname, String DBip, int DBport,int port) {
        try {
        server = new ServerSocket(6677);        } 
        catch(IOException e) {}
        try {
        
            DB = new DBServer(DBname,DBip,DBport);    }
        catch (ClassNotFoundException ex) { Logger.getLogger(KISLab3Server.class.getName()).log(Level.SEVERE, null, ex);     } 
        catch (InstantiationException ex) {  System.out.println(2);      }
        catch (IllegalAccessException ex) {   System.out.println(3);     } 
        catch (SQLException ex) {   System.out.println(4);     }
        while(true){
            try {  
                // Принимаем соединение от нового клиента
                sock = server.accept();
                in = new DataInputStream(sock.getInputStream()); 
                out = new DataOutputStream(sock.getOutputStream());        } 
            catch (IOException ex) {
               // Logger.getLogger(KISLab3Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            while (processQuery()){}
        }
    }
    public boolean processQuery(){
         try 
            {
                int code = in.readInt();
                System.out.println("код "+ code);
                
                int id;
                int id_fk;
                String text;
                String title;
                String data;


                switch (code){
//1. Добавление новой категории 
                    case 1: 
                        id = in.readInt();
                        text = in.readUTF();
                        out.writeInt(DB.addCategory(id, text));
                        break;
//2. Удаление категории 
                    case 2:
                        id = in.readInt();
                        out.writeInt(DB.deleteCategory(id));
                        break;
                    
//3. Добавление новости заданной категории 
                    case 3:
                        id = in.readInt();
                        title = in.readUTF();
                        text = in.readUTF();
                        data = in.readUTF();
                        id_fk = in.readInt();
                        out.writeInt(DB.addNews(id, title, text, data, id_fk));            
                        break;   
//4. Удаление новости 
                    case 4:
                        id = in.readInt();
                        out.writeInt(DB.deleteNews(id));
                        break;
//5. Редактирование новости 
                    case 5:
                        id = in.readInt();
                        title = in.readUTF();
                        text = in.readUTF();
                        //int id, String title, String text
                        out.writeInt(DB.UpdateNews(id,title,text));
                        break;
//6. Подсчет количества новостей в категории 
                    case 6:
                        id = in.readInt();
                        out.writeInt(DB.CountNews(id));
                        break;   
//7. Выдача новости по идентификатору 
                    case 7:
                        id = in.readInt();
                        out.writeUTF(DB.FindNews(id));
                        
                        break;    
//8. Выдача полного списка новостей для заданной категории 
                    case 8:
                        id = in.readInt();
                        out.writeUTF(DB.ShowNews(id));
                        break; 
//9. Выдача полного списка категорий
                    case 9:
                        out.writeUTF(DB.ShowCategories());
                        break; 
                        
                        default: System.out.println("дефолт");
                }
                        
                return true;

            } catch (IOException ex) { 
            //Logger.getLogger(KISLab3Server.class.getName()).log(Level.SEVERE, null, ex);
                return false;
        } 
         
    }
    public static void main(String[] args)
    {
        KISLab3Server srv = new KISLab3Server();
        srv.Start("news_agency","localhost",3306,6677); 
    } 
}
