import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;


public class MyThreadClass implements Runnable{
	Socket socket;
	File file;
	String global_file_name;
	String type;
	DataOutputStream socket_writer;
	
	public MyThreadClass(Socket socket, File file, String file_name, String type, DataOutputStream socket_writer) {
		this.socket=socket;
		this.file=file;
		this.global_file_name=file_name;
		this.type=type;
		this.socket_writer = socket_writer;
	}
	final static String CRLF="\r\n";
	
	@Override
	public void run() {
		
		//thsi part of the code runs on a new thread.
//		for(int i=1;i<3;i++)
//			System.out.println("this is the new thread:"+ i);
		//global_file_name = "test.out";
		
		if(type.equalsIgnoreCase("Client"))
			receiveFile(global_file_name, socket);
		else if(type.equalsIgnoreCase("Server"))
			sendFile(file, socket, socket_writer);
		
	}
	static void receiveFile(String file_name, Socket socket)
	{
		//
		try
		{
			FileOutputStream file_writer=new FileOutputStream(file_name);
			DataInputStream socket_data_reader=new DataInputStream(socket.getInputStream());
			byte[] buff=new byte[100];
			int read_bytes = socket_data_reader.read(buff, 0, 100);
			
			while(read_bytes > 0)
			{
				System.out.println("receiving: "+ read_bytes+ " bytes");
				file_writer.write(buff, 0, read_bytes);
				read_bytes = socket_data_reader.read(buff, 0, 100);
			} 
			
			
			
			socket.close();
			
			file_writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	static void sendFile(File file,Socket socket, DataOutputStream socket_writer)
	{
		try
		{
		//reading data from a file:
		//instantiation:
		FileInputStream file_reader=new FileInputStream(file);
		//reading:
		System.out.println("file has: "+ file.length()+ " bytes");
		byte[] buff=new byte[100];
		int read_bytes = file_reader.read(buff, 0, 100);
		//check if read_bytes>0
		//...
		
		while(read_bytes > 0)
		{
			//this should be done in client! 
			//writing dat to a file:
			
			//reading/writing data from socket:
			if(read_bytes == 0)
			{
				socket_writer.writeBytes("Empty File"+CRLF);
				System.out.println("Empty file");
			}
			else
			{
				DataOutputStream socket_data_writer=new DataOutputStream(socket.getOutputStream());
				socket_data_writer.write(buff, 0, read_bytes);
				read_bytes=file_reader.read(buff, 0, 100);
			}
		}
		socket.close();
		//after this is done, CLOSE the file writer/reader
		file_reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
