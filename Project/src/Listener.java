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
			
			String from_server;
			
			while (socket.isConnected())
			{			
				from_server = read_socket.readLine();
				System.out.println("Received ack no: " + from_server);
				MainClient.update(Integer.parseInt(from_server));
				
				if (Integer.parseInt(from_server) == -1)
					break;
			}
			socket.close();
			
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		
	}
	
}
