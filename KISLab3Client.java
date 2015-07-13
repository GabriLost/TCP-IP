import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class KISLab3Client { 
    private Socket sock = null; 
    private DataInputStream in = null;
    private DataOutputStream out = null;

 public void connect(String ip, int port) throws IOException 
 { 
     // Устанавливаем соединение 
     sock = new Socket(ip,port); 
     try
     {      
         in = new DataInputStream(sock.getInputStream());
         out = new DataOutputStream(sock.getOutputStream()); 

     } catch (IOException e) {} 
 } 
 public int addCategory(int id, String text) throws IOException{
    out.writeInt(1);
    out.writeInt(id);
    out.writeUTF(text);
    return in.readInt();
        };
 public int deleteCategory(int id) throws IOException{
     out.writeInt(2);
     out.writeInt(id);
     return in.readInt();
        };
 public int addNews(int id, String title, String text, String data, int id_fk) throws IOException{
    out.writeInt(3);
    out.writeInt(id);
    out.writeUTF(title);
    out.writeUTF(text);
    out.writeUTF(data);
    out.writeInt(id_fk);
    return in.readInt();
        };
 public int deleteNews(int id) throws IOException{
     out.writeInt(4);
     out.writeInt(id);
     return in.readInt();
        };
 public int UpdateNews(int id, String title,String text) throws IOException{
     out.writeInt(5);
     out.writeInt(id);
     out.writeUTF(title);
     out.writeUTF(text);
     return in.readInt();
        };
 public int CountNews(int id) throws IOException{
     out.writeInt(6);
     out.writeInt(id);
     return in.readInt();
        };
 public String FindNews(int id) throws IOException{
     out.writeInt(7);
     out.writeInt(id);
     return in.readUTF();
        };
 public String  ShowNews(int id) throws IOException{
     out.writeInt(8);
     out.writeInt(id);
     return in.readUTF();
        }; 
 public String  ShowCategories() throws IOException{
     out.writeInt(9);
     return in.readUTF();
        }; 
 public static void main(String[] args) throws IOException 
 { 
     KISLab3Client client = new KISLab3Client();
     client.connect("localhost",6677);
     
     System.out.println("Добавление категории "+
             client.addCategory(44,"mmmmm"));
     
     System.out.println("Добавление категории "+
             client.addCategory(45,"mmmmm1"));
     
     System.out.println("Удаление категории "+
             client.deleteCategory(44));
     
     System.out.println("Добавление новости"+
             client.addNews(56,"title1", "text1","2014-03-05",4));
     
     System.out.println("Добавление новости"+
             client.addNews(57,"title1", "text1","2014-03-05",4));
     
     System.out.println("Удаление новости"+
             client.deleteNews(56));
     
     System.out.println("Редактирование новости"+
             client.UpdateNews(57,"Update title1","Update text1"));
     
     System.out.println("Количество новостей в категории"+
             client.CountNews(7));
     
     System.out.println("Выдача новости по id: "+
             client.FindNews(1));
     
     System.out.println("Показать новости по категории: "+
             client.ShowNews(1));
     
     System.out.println("Показать новости по категории: "+
             client.ShowCategories());
     
      } 
} 
