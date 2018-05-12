package ft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
 
 
public class ftServer {
	public static void main(String[] args) throws Exception {
				
		// 创建ServerSocket, 循环中接收客户端请求, 开启新线程
		ServerSocket serverSocket =new ServerSocket(12306);
        while (true) {
        	final Socket socket = serverSocket.accept();   //阻塞   监听客户端连接
        	new Thread(){
        		public void run() {
					try {
						// 封装获取输入输出流
						InputStream is = socket.getInputStream();
						OutputStream os = socket.getOutputStream();
						// 封装文本文件
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						PrintStream ps = new PrintStream(os);

						// 接收文件名和文件大小, 查找文件是否存在, 是否已完成上传
						String filename = br.readLine();
						long filelength = Long.parseLong(br.readLine());

						File file = new File("F:/ftUpload", filename);
						if (file.exists() && file.length() == filelength) {
							ps.println("存在");
							return;
							}
						else {
							ps.println(file.length()); // 写回文件大小(0或已完成的大小)
							}

						String ip = socket.getInetAddress().getHostAddress();
						System.out.println(ip);
						System.out.println((file.exists() ? "文件-----" + filename + "-----开始断点续传!!!" : "开始上传文件:-----"+ filename));
						long start = System.currentTimeMillis();

						// 定义输出流指向文件, 从网络中读取数据写出到文件
						FileOutputStream fos = new FileOutputStream(file, true);
						byte[] buffer = new byte[1024];
						int len;
						while ((len = is.read(buffer)) != -1)
							// 读取网络
							fos.write(buffer, 0, len); // 写出文件
						fos.close();
						socket.close();

						long stop = System.currentTimeMillis();
						System.out.println(ip);
						System.out.println(" 文件上传完毕: " + filename + ", 耗时: " + (stop - start) + "毫秒.");
						} catch (IOException e) {
							e.printStackTrace();    //在命令行打印异常信息在程序中出错的位置及原因。
							}
								}
                       }.start();
                   }
         }
}