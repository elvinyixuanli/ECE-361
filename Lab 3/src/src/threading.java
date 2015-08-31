import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.acl.LastOwnerException;


public class threading implements Runnable{
	Socket socket;
	
	public threading(Socket socket)
	{
		this.socket=socket;
//		this.lastAck = last;
	}
	final static String CRLF="\r\n";
	
	@Override
	public void run() 
	{
		try
		{
			BufferedReader read_socket=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			int from_server;
			
			while (socket.isConnected())
			{			
				from_server=read_socket.read();
				System.out.println("ack:"+from_server);
				client3.lastAck +=1;
				
				if (from_server == -1)
					break;
			}
			socket.close();
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}