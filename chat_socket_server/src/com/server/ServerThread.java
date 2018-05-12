package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ServerThread extends Thread {

	public ServerSocket serverSocket;
	public Vector<String> messages;
	public Vector<ClientThread> clients;
	public Map<Integer, String> users;
	public BroadCast broadcast;
	public int Port = 5000;
	public boolean login = true;
	public ServerFrame serverFrame;
	private boolean flag_exit = false;
	public ServerThread(ServerFrame serverFrame){
		this.serverFrame = serverFrame;
		messages = new Vector<String>();
		clients = new Vector<ClientThread>();
		users = new HashMap<Integer, String>();
		try {
			serverSocket = new ServerSocket(Port);
		} catch (IOException e) {
			this.serverFrame.setStartAndStopUnable();
			System.exit(0);
		}
		broadcast = new BroadCast(this);
		broadcast.setFlag_exit(true);
		broadcast.start();
	}
	
	@Override
	public void run() {
		Socket socket;
		while(flag_exit){
				try {
					if(serverSocket.isClosed()){
						flag_exit = false;
					}else{
						try{
							socket = serverSocket.accept();
						}catch(SocketException e){
							socket = null;
							flag_exit = false;
						}
						
						if(socket != null){
							ClientThread clientThread = new ClientThread(socket, this);
							clientThread.setFlag_exit(true);
							clientThread.start();
							/*
							 * synchronized
							 * 代表这个方法加锁,相当于不管哪一个线程(例如线程A)，
							 * 运行到这个方法时,都要检查有没有其它线程B(或者C、 D等)正在用这个方法(或者该类的其他同步方法)，
							 * 有的话要等正在使用synchronized方法的线程B(或者C 、D)运行完这个方法后再运行此线程A,没有的话,锁定调用者,然后直接运行。
							 */
							synchronized (clients) {
								clients.addElement(clientThread);
							}
							synchronized (messages) {
								users.put((int) clientThread.getId(), "@login@");
								messages.add(clientThread.getId() + "@clientThread");
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public void stopServer() {
		try {
			if(this.isAlive()){
				serverSocket.close();
				setFlag_exit(false);
			}
		} catch (Throwable e) {}
	}

	public void setFlag_exit(boolean b) {
		flag_exit = b;
	}
}
