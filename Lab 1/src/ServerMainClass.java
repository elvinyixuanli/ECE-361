import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerMainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String CRLF="\r\n";

		try
		{

			//write something:(std output)
			//like: cout<<
			System.out.println("Hello world!");
			
			
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
			String fromClient;
			
			while(true)
			{
				fromClient=read_socket.readLine();
				System.out.println("Client said: "+fromClient);

				System.out.print("Server: ");
				//acutally reading from the std input
				String toClient = reader.readLine();

				//actually writing to socket
				write_socket.writeBytes(toClient+CRLF);
				
				if(toClient.equals("quit"))
					break;
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