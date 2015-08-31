import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

	/**
	 * @param args
	 */
	final static String CRLF="\r\n";
	
	public static void main(String[] args) {
		//carriage return line feed
		
		
		try
		{

			int port=1234;
			ServerSocket ss=new ServerSocket(port);
			Socket socket= ss.accept();
			//Thread thread=new Thread(new MyThreadClass(socket, null, "", "", null));
			//thread.start();
			System.out.println("connected to a client.");
			//define readers and writers of socket:
			BufferedReader socket_reader=new BufferedReader
					(new InputStreamReader(socket.getInputStream()));
			DataOutputStream socket_writer = new 
					DataOutputStream(socket.getOutputStream());
			
			//define reader from std in.
			//BufferedReader reader = new BufferedReader
					//(new InputStreamReader(System.in));
			
			String file_name= socket_reader.readLine();
			
			//find out whether a file exists or not:
			File file=new File(file_name);
			if(file.exists())
			{
				//file exists:
				
				socket_writer.writeBytes("OK"+CRLF);
				int newPort=5678;
				ServerSocket dataS=new ServerSocket(newPort);
				socket_writer.writeBytes(newPort+CRLF);
				Socket dataSocket= dataS.accept();
				System.out.println("connected to a client on data port: "+newPort);
				Thread dataThread=new Thread(new MyThreadClass(dataSocket, file, "test.in", "Server", null));
				dataThread.start();
				//sendFile(file, dataSocket, socket_writer);
				
			}
			else
			{
				//file doesn't exist:
				socket_writer.writeBytes("not found!"+CRLF);
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	

}
