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
				
		// ����ServerSocket, ѭ���н��տͻ�������, �������߳�
		ServerSocket serverSocket =new ServerSocket(12306);
        while (true) {
        	final Socket socket = serverSocket.accept();   //����   �����ͻ�������
        	new Thread(){
        		public void run() {
					try {
						// ��װ��ȡ���������
						InputStream is = socket.getInputStream();
						OutputStream os = socket.getOutputStream();
						// ��װ�ı��ļ�
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						PrintStream ps = new PrintStream(os);

						// �����ļ������ļ���С, �����ļ��Ƿ����, �Ƿ�������ϴ�
						String filename = br.readLine();
						long filelength = Long.parseLong(br.readLine());

						File file = new File("F:/ftUpload", filename);
						if (file.exists() && file.length() == filelength) {
							ps.println("����");
							return;
							}
						else {
							ps.println(file.length()); // д���ļ���С(0������ɵĴ�С)
							}

						String ip = socket.getInetAddress().getHostAddress();
						System.out.println(ip);
						System.out.println((file.exists() ? "�ļ�-----" + filename + "-----��ʼ�ϵ�����!!!" : "��ʼ�ϴ��ļ�:-----"+ filename));
						long start = System.currentTimeMillis();

						// ���������ָ���ļ�, �������ж�ȡ����д�����ļ�
						FileOutputStream fos = new FileOutputStream(file, true);
						byte[] buffer = new byte[1024];
						int len;
						while ((len = is.read(buffer)) != -1)
							// ��ȡ����
							fos.write(buffer, 0, len); // д���ļ�
						fos.close();
						socket.close();

						long stop = System.currentTimeMillis();
						System.out.println(ip);
						System.out.println(" �ļ��ϴ����: " + filename + ", ��ʱ: " + (stop - start) + "����.");
						} catch (IOException e) {
							e.printStackTrace();    //�������д�ӡ�쳣��Ϣ�ڳ����г����λ�ü�ԭ��
							}
								}
                       }.start();
                   }
         }
}