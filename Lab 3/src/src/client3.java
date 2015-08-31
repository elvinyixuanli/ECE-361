import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;


public class client3 {

	/**
	 * @param args
	 */
	static int lastAck = 0;

	
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
			Socket socket=new Socket("128.100.13.249", 9876);
			
//			Thread thread = new Thread(new RunnableClass(socket));
			
			System.out.println("connected successfully!");
			
			//defining things to read something from the socket:
			BufferedReader read_socket=new BufferedReader
				(new InputStreamReader(socket.getInputStream()));
			
			//defining things to write somehting to the socket:
			DataOutputStream write_socket=new DataOutputStream(socket.getOutputStream());
			
			
			//actually reading from the socket:
			//reading the welcome message
			int sent = 1;
			
			lastAck = 0;
			
//			while(true)
//			{
				System.out.print("Packets: ");
				Scanner scr = new Scanner(System.in);
				int noPackets = scr.nextInt();
				write_socket.write(noPackets);

				System.out.print("Error: ");
				int probError = scr.nextInt();
				write_socket.write(probError);
				
				Thread thread=new Thread(new threading(socket));
				thread.start();
				
				while (sent <= noPackets)
				{
					write_socket.write(sent);
				
					sent++;
				}
				

				
//			}
//			socket.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}




	}

}