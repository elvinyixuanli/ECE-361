import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class MainClass {

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
			Socket socket=new Socket("127.0.0.1", 9876);
			System.out.println("connected successfully!");
			
			//defining things to read something from the socket:
			BufferedReader read_socket=new BufferedReader
				(new InputStreamReader(socket.getInputStream()));
			
			//defining things to write somehting to the socket:
			DataOutputStream write_socket=new DataOutputStream(socket.getOutputStream());
			
			
			//actually reading from the socket:
			//reading the welcome message
			String from_server=read_socket.readLine();
			System.out.println("server said: "+from_server);
			
			while(true)
			{
				//acutally reading from the std input
				String to_server=reader.readLine();


				//actually writing to socket
				write_socket.writeBytes(to_server+CRLF);
				from_server=read_socket.readLine();
				System.out.println("server said:"+from_server);
				
				if(to_server.equals("quit"))
					break;
			}
			socket.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}




	}

}
