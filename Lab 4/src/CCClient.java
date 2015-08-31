import java.awt.Color;
import java.awt.image.SampleModel;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;



public class CCClient {

	static String host;
	static int port;
	final static String CRLF="\r\n";
	public static int wstart;
	static long totalTime;
	static int timeOut;
	public static int lastAck = 0;
	static int sent = 1;
	static long[] send_timer;
	
	static long startTime;
	static long endTime;
	public static int EstimatedRTT;
	public static int DevRTT;
	public static int SampleRTT;
	public static final double alpha=0.125;
	public static final double beta=0.25;
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		host="localhost";
		port=9876;
		try 
		{
			Socket socket= new Socket("128.100.13.249",port);
			socket.setTcpNoDelay(true); 
			System.out.println("Connected to : "+ host+ ":"+socket.getPort());

			//reader and writer:
			BufferedReader reader= new BufferedReader
					(new InputStreamReader(socket.getInputStream()));
			DataOutputStream writer=new DataOutputStream(socket.getOutputStream());
			Scanner scr = new Scanner(System.in);

			//define the thread and start it
			Thread thread= new Thread(new Listener(socket)); 
			thread.start();
			
					 

			System.out.println("Enter number of packets to be sent to the server [0-127], 0 to Quit: ");
			int noPackets = scr.nextInt();
			
			//the noPackets to the server
			//...
			writer.write(noPackets);
			
			send_timer = new long[noPackets];	
			
			
			EstimatedRTT=1200;
			DevRTT=0;
			timeOut = EstimatedRTT+4*DevRTT; //in milliseconds
			int index = 0;
			lastAck=0;
			sent=1;
			int cwnd=1;
			int ssthresh=1000;
			int RTT_count=0;

			startTime=System.currentTimeMillis();
			try {
				while(lastAck<noPackets)
				{
					//THE MAIN PART OF THE CODE!
					//send the packets with congestion control using the given instructions
					if (index < cwnd && sent <= noPackets)
					{
						System.out.println("Sending packet " + sent);
						writer.write(sent);
						send_timer[sent - 1] = System.currentTimeMillis();
						
						sent++;
						index++;
						
					}
					
					if ((sent - 1) == lastAck)
					{
						if (cwnd < ssthresh)
							cwnd *= 2;
						else
							cwnd += 1;
						
						index = 0;
					}
					
					else
					{
						if ((System.currentTimeMillis() - send_timer[lastAck]) > timeOut)
						{
							System.out.println("Timed out");
							ssthresh = cwnd/2;
							cwnd = 1;
							index = 0;
							sent = lastAck + 1;
						}
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				endTime = System.currentTimeMillis();
				totalTime = endTime - startTime;
			}
			
			
			//print the total taken time, number of sucessfully sent packets, etc. 
			//...
			System.out.println("No. of packets = " + noPackets);
			System.out.println("Total time = " + totalTime);
			
			writer.flush();
			socket.close();
			System.out.println("Quitting...");
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void update(int ackNum)
	{
		//update lastAck here. note that last ack is accumulative, 
		//i.e., if ack for packet 10 is previously received and now ack for packet 7 is received, lastAck will remain 10
		//...
		if (lastAck <= ackNum)
			lastAck = ackNum;
	}

}
