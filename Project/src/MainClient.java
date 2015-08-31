import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Random;

import javax.xml.ws.handler.MessageContext.Scope;

public class MainClient {

	static String mode;
	static String host;
	static int port;
	static Socket socket;
	
	static BufferedReader socket_reader;
	static DataOutputStream writer;
	static BufferedReader input_reader;
	
	static File file;
	static byte[][] fileBuffer;
	
	final static String CRLF="\r\n";
	final static int PACKET_SIZE = 1004;
	
	static int timeOut;
	public static int lastAck = 0;
	static int sent = 1;
	static long[] send_timer;
	
	public static void adjacenyToEdges(double[][] matrix, List<Node> v)
	{
		for(int i = 0; i < matrix.length; i++)
		{
			v.get(i).neighbors = new Edge[matrix.length];
			for(int j = 0; j < matrix.length; j++)
			{
				v.get(i).neighbors[j] =  new Edge(v.get(j), matrix[i][j]);
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println("");
		}
	}
	public static void computePaths(Node source)
	{
		source.minDistance = 0;
		
		PriorityQueue<Node> NodeQueue = new PriorityQueue<Node>();
		NodeQueue.add(source);
		
		while(!NodeQueue.isEmpty())
		{
			Node sourceNode = NodeQueue.poll();
			
			for(Edge e: sourceNode.neighbors)
			{
				
				Node targetNode = e.target;
				double weight = e.weight;
				double distanceThroughSource = sourceNode.minDistance + weight;
				
				if (distanceThroughSource < targetNode.minDistance) 
				{
					NodeQueue.remove(targetNode);
					targetNode.minDistance = distanceThroughSource;
					targetNode.previous = sourceNode;
					NodeQueue.add(targetNode);
				}
			}
		}
	}

	public static List<Integer> getShortestPathTo(Node target)
	{
		List<Integer> path = new ArrayList<Integer>();
		
		for (Node node = target; node != null; node = node.previous)
			path.add(node.name);
		Collections.reverse(path);
		return path;
	}

	static void sendFile(int noPackets)
	{
		send_timer = new long[noPackets];			
		
		int index = 0;
		int cwnd = 1;
		int ssthresh = 1000;
		
		try
		{
			//reading data from a file:
			//instantiation:
			FileInputStream file_reader = new FileInputStream(file);
			//reading:
			for (byte [] b: fileBuffer)
				file_reader.read(b, 4, PACKET_SIZE - 4);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try {
			while(lastAck < noPackets)
			{
				if (index < cwnd && sent <= noPackets)
				{
					System.out.println("sending packet no:" + sent);
					ByteBuffer buf = ByteBuffer.allocate(4);
					buf.putInt(sent);
					for (int i = 0; i < 4; i++)
						fileBuffer[sent - 1][i] = buf.get(i);
					writer.write(fileBuffer[sent - 1]);
					send_timer[sent - 1] = System.currentTimeMillis();
					
					sent++;
					index++;
				}
				
				if ((sent - 1) == lastAck)
				{
					System.out.println("cwnd= " + cwnd);
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
	}
	
	public static void update(int ackNum)
	{
		//update lastAck here. note that last ack is accumulative, 
		//i.e., if ack for packet 10 is previously received and now ack for packet 7 is received, lastAck will remain 10
		//...
		if (lastAck <= ackNum)
			lastAck = ackNum;
		System.out.println("last ack: " + lastAck);
	}

	public static void main(String[] args) {

		if(args.length<=0)
		{
			mode="client";
			host="localhost";
			port=9876;
		}
		else if(args.length==1)
		{
			mode=args[0];
			host="localhost";
			port=9876;
		}
		else if(args.length==3)
		{
			mode=args[0];
			host=args[1];
			port=Integer.parseInt(args[2]);
		}
		else
		{
			System.out.println("improper number of arguments.");
			return;
		}

		try 
		{
			socket = new Socket(host, port);
			socket.setTcpNoDelay(true); 
			
			System.out.println("Connected to : "+ host+ ":"+socket.getPort());
			System.out.println("waiting to receive the number of nodes...");

			//reader and writer:
			socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //for reading lines
			writer = new DataOutputStream(socket.getOutputStream());	//for writing lines.
			input_reader = new BufferedReader(new InputStreamReader(System.in));
			String from_server;
			
			while(socket != null && socket.isConnected() && !socket.isClosed())
			{
				int noNodes;
				
				from_server = socket_reader.readLine();
				noNodes = Integer.parseInt(from_server);
				System.out.println("number of nodes:" + noNodes);
				System.out.println("");
				from_server=socket_reader.readLine();
				
				// Create an adjacency matrix after reading from server
				double[][] matrix = new double[noNodes][noNodes];
				
				// Use StringToenizer to store the values read from the server in matrix
				StringTokenizer st = new StringTokenizer(from_server);
				
				int i = 0;

				while(st.hasMoreTokens())
				{
					int j = 0;
					for(; j < noNodes; j++)
						matrix[i][j] = Double.parseDouble(st.nextToken());
					
					i++;
				}
				
				//The nodes are stored in a list, nodeList
				List<Node> nodeList = new ArrayList<Node>();
				for(i = 0; i < noNodes; i++){
					nodeList.add(new Node(i));
				}
				
				// Create edges from adjacency matrix
				System.out.println("Adjacency Matrix");
				adjacenyToEdges(matrix, nodeList); 
				System.out.println("");
				
				computePaths(nodeList.get(0));
				
				System.out.println("Node " + nodeList.get(0).name);
				List<Integer> path = getShortestPathTo(nodeList.get(0));
				Node last = null;
				for (Node v : nodeList)
				{
					System.out.print("Total time to reach node " + v.name + ": " + v.minDistance + " ms, ");
					path = getShortestPathTo(v);
					System.out.println("Path: " + path);
					last = v;
				}
				
				writer.writeBytes(path.toString() + CRLF);
				timeOut = 2 * (int) last.minDistance + 200;
				
				System.out.println("");
				System.out.print("Enter the name of the file: ");
				
				String file_name=input_reader.readLine();
				file = new File(file_name);
				
				if (file.exists())
				{
					writer.writeBytes(file_name + CRLF);
					long size = file.length();
					System.out.println("file length: " + size);
					long packets = size / PACKET_SIZE + 1;
					writer.writeBytes(packets + CRLF);
					Thread thread= new Thread(new Listener(socket)); 
					thread.start();
					fileBuffer = new byte[(int) packets][PACKET_SIZE];
					sendFile((int)packets);
				}
				else
					System.out.println("File not found");

				socket.close();
			}
			System.out.println("Quitting...");


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
