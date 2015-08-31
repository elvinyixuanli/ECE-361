import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String CRLF="\r\n";
		try
		{
			
			//to define a new thread and run it:
			//Thread thread=new Thread(new MyThreadClass(null, null, null));
			//thread.start();
			
			for(int i=1;i<3;i++)
				System.out.println("this is the main thread:"+ i);
			
			
			
			////to do the lab instructions:
			int port=1234;
			Socket socket=new Socket("localhost",port);
			System.out.println("connected to server.");
			
			
			//define readers and writers of socket:
			BufferedReader socket_reader=new BufferedReader
					(new InputStreamReader(socket.getInputStream()));
			DataOutputStream socket_writer = new 
					DataOutputStream(socket.getOutputStream());
			
			//Thread thread=new Thread(new MyThreadClass(socket, null, "", "", socket_writer));
			//thread.start();
			
			//define reader from std in.
			BufferedReader reader = new BufferedReader
					(new InputStreamReader(System.in));
			
			System.out.print("write a file name: ");
			
			
			String file_name=reader.readLine();
			long open_time = System.currentTimeMillis();
			System.out.println("Open time: " + open_time);
			
			socket_writer.writeBytes(file_name+CRLF);
			String server_response=socket_reader.readLine();
			System.out.println("Server said: "+ server_response);
			
			//analyzing the response from the server:
			if(server_response.equalsIgnoreCase("OK"))
			{
				//server has the file! starting to receive it:
				int dataPort = Integer.parseInt(socket_reader.readLine()); // Cause socket_reader.readInt() is a piece of shit
				Socket newSocket=new Socket("localhost",dataPort);
				System.out.println("connected to server for data transfer on port: "+dataPort);
				Thread dataThread=new Thread(new MyThreadClass(newSocket, null, "test.out", "Client", socket_writer));
				dataThread.start();
				
				//receiveFile("test.out", newSocket);
				
			}
			else
			{
				//server doesn't have the file. quitting.
				;
			}
			
			long close_time = System.currentTimeMillis();
			System.out.println("Close time: " + close_time);
			
			System.out.println("Rount trip time: " + (close_time - open_time));
			socket.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	

}
