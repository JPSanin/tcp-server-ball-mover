package main;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import processing.core.PApplet;


public class Main extends PApplet {
	
	private Socket socket;
	private BufferedWriter bw;
	private BufferedReader br;
	private int x,y;
	private int r,g,b;
	
	
	public static void main(String[] args) {
		PApplet.main(Main.class.getName());

	}

	public void settings() {
		size(800,600);

	}

	public void setup() {
		initServer();
		x=400;
        y=300;
        r=200;
        g=100;
        b=25;
	}
	
	public void draw() {
		background(125);
		
		fill(r,g,b);
		ellipse(x,y,50,50);
	}
	
	
	public void initServer() {
		new Thread(
				()->{
					
					try {
						ServerSocket server= new ServerSocket(5000);
						System.out.println("Waiting for client...");
						socket=server.accept();
						System.out.println("Client connected");
						
						
						//Send
						InputStream is= socket.getInputStream();
						InputStreamReader isr= new InputStreamReader(is);
						br= new BufferedReader(isr);
						
						OutputStream os = socket.getOutputStream();
						OutputStreamWriter osw= new OutputStreamWriter(os);
						bw= new BufferedWriter(osw);
						
						while(true) {
							System.out.println("Waiting...");
							String line = br.readLine();
							String[] vals= line.split(":");
							int value= Integer.parseInt(vals[1]);
							
							if(vals[0].equals("y")) {
									y=value;
								
							}else if(vals[0].equals("x")) {
									x=value;
								
							}else if(vals[0].equals("c")) {
								r=Integer.parseInt(vals[1]);
								g=Integer.parseInt(vals[2]);
								b=Integer.parseInt(vals[3]);
							}
							
							
							System.out.println("Recieved");
							System.out.println("Msg: "+ line);
						}
						
						
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}).start();
		
	}
	
	
	public void sendMessage(String msg) {
		new Thread(
				()->{
					
					try {
						
						bw.write(msg+"\n");
						bw.flush();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					
				}).start();
	}
	
	public void mousePressed() {
		
		sendMessage("Hola soy server");
	}
	

}
