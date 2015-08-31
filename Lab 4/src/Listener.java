import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Listener implements Runnable {

	public Socket socket;
	
	public Listener(Socket socket) {
		this.socket=socket;
	}
	
	@Override
	public void run() {
		try {
			
			BufferedReader read_socket=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			int from_server;
			
			while (socket.isConnected())
			{			
				from_server=read_socket.read();
				System.out.println("ack:"+from_server);
				CCClient.update(from_server);
//				client3.lastAck +=1;
				
				if (from_server == -1)
					break;
			}
			socket.close();
			
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		
	}
	
}
