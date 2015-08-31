import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String CRLF="\r\n";

		try
		{

			//write something:(std output)
			//like: cout<<
//			System.out.println("Hello world!");
			
			
			//Read somehting.(std input)
			//like: cin>>
			BufferedReader reader=new BufferedReader
					(new InputStreamReader(System.in));
			//equivalent to:
			//InputStreamReader is=new InputStreamReader(System.in);
			//BufferedReader reader=new BufferedReader(is);

			//
			ServerSocket serverSocket = new ServerSocket(9876);
			
			Socket socket = serverSocket.accept();
			
			System.out.println("connected successfully!");
			
			//defining things to read something from the socket:
			BufferedReader read_socket=new BufferedReader
				(new InputStreamReader(socket.getInputStream()));
			
			//defining things to write somehting to the socket:
			DataOutputStream write_socket=new DataOutputStream(socket.getOutputStream());
			
			
			//actually reading from the socket:
			//reading the welcome message
//			int fromClient;
//			int lastAck = 0;
			int lastAck = 0;
			while(true)
			{
				int fromClient;
				
				
				fromClient=read_socket.read();
				System.out.println("Client said: "+fromClient);
				
				if(fromClient == -1)
					break;
//				fromClient=read_socket.read();
//				System.out.println("Client said (sent): "+fromClient);
				
				if (fromClient == (lastAck + 1))
				{
					write_socket.write(fromClient);
					lastAck++;
				}
				else if(fromClient == 0)
					lastAck =0;
					

//				System.out.print("Server: ");
//				//acutally reading from the std input
//				toClient = reader.readLine();

				//actually writing to socket
//				write_socket.writeBytes(toClient+CRLF);
				
//				if(toClient.equals("quit"))
//					break;
			}
			socket.close();
			serverSocket.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}




	}

}