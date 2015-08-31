import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;


public class Client {

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
			
//			Thread thread = new Thread(new RunnableClass(socket));
			
			System.out.println("connected successfully!");
			
			//defining things to read something from the socket:
			BufferedReader read_socket=new BufferedReader
				(new InputStreamReader(socket.getInputStream()));
			
			//defining things to write somehting to the socket:
			DataOutputStream write_socket=new DataOutputStream(socket.getOutputStream());
			
			
			//actually reading from the socket:
			//reading the welcome message
//			int from_server;
//			int sent = 1;
			
			while(true)
			{
				int from_server;
				int sent = 1;
				
				System.out.print("Input to send to server: ");
				Scanner scr = new Scanner(System.in);
				int noPackets = scr.nextInt();
				
				if(noPackets == -1)
					break;
//				System.out.print("Input to send to server: ");
				//acutally reading from the std input
//				String to_server=reader.readLine();


//				long open_time = System.currentTimeMillis();
//				System.out.println("Open time: " + open_time);
				//actually writing to socket
				while (sent <= noPackets)
				{
//					write_socket.write(noPackets);
					
					write_socket.write(sent);
					
					from_server=read_socket.read();
					System.out.println("server said:"+from_server);
					
					if (from_server == sent)
						sent++;
				}
				int reset = 0;
				write_socket.write(reset);
				
				
				
				
//				long close_time = System.currentTimeMillis();
//				System.out.println("Close time: " + close_time);
				
//				System.out.println("Rount trip time: " + (close_time - open_time));
				
				
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