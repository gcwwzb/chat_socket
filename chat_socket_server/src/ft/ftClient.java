package ft;

/*
 * 要上传的文件路径 ----F:\ft\eclipse.zip
 *               ----F:\ft\哈利波特3阿兹卡班的囚徒.mkv
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
 
public class ftClient {
	public static void main(String[] args) throws Exception {
		// 创建Socket, 指定服务端地址和端口, 发起请求
		// Socket socket = new Socket("192.168.43.44", 12306);
		Socket socket = new Socket("gcww", 12306);
		// 获取输入输出流
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		PrintStream ps = new PrintStream(os); // 创建一个新的打印流。

		// 从键盘输入读取一个文件路径, 验证该文件是否存在, 是否是文件夹
		File file = getFile();     

		// 把文件名和文件大小发送到服务端
		ps.println(file.getName());
		ps.println(file.length());

		// 接收结果, 如果已存在给予提示, 程序退出
		String result = br.readLine();
		if ("存在".equals(result)) {
			System.out.println("文件已存在, 请不要重复上传!");
			return;
			}

		// 定义输入流指向文件, 读取文件写出到网络
		FileInputStream fis = new FileInputStream(file);
		long filelength = Long.parseLong(result); // 服务端已完成的大小     Long.parseLong()将字符串参数作为一个符号的十进制
		fis.skip(filelength); // 上次完成了多少就跳过多少 skip方法就是跳过并丢弃 n字节输入流中的数据。
		byte[] buffer = new byte[1024];
		int len;
		while ((len = fis.read(buffer)) != -1){
		// 读取文件
			os.write(buffer, 0, len); // 写到网络
			}
		
		fis.close();
		socket.close();
		System.out.println("上传完毕!");
        }

	private static File getFile() {
		System.out.println("请输入要上传的文件路径:");
		Scanner scanner = new Scanner(System.in);
		while (true) {
			File file = new File(scanner.nextLine());
			//检查文件或目录是否存在这种抽象路径名
			if (!file.exists())        
				System.out.println("您输入的路径不存在, 请重新输入:");
			else if (file.isDirectory())       //测试文件是否通过这种抽象路径名表示是一个目录。 
				System.out.println("暂不支持文件夹上传, 请输入一个文件路径:");
			else
				return file;
		}
	}
}
