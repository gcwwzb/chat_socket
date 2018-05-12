package ft;

/*
 * Ҫ�ϴ����ļ�·�� ----F:\ft\eclipse.zip
 *               ----F:\ft\��������3���ȿ������ͽ.mkv
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
		// ����Socket, ָ������˵�ַ�Ͷ˿�, ��������
		// Socket socket = new Socket("192.168.43.44", 12306);
		Socket socket = new Socket("gcww", 12306);
		// ��ȡ���������
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		PrintStream ps = new PrintStream(os); // ����һ���µĴ�ӡ����

		// �Ӽ��������ȡһ���ļ�·��, ��֤���ļ��Ƿ����, �Ƿ����ļ���
		File file = getFile();     

		// ���ļ������ļ���С���͵������
		ps.println(file.getName());
		ps.println(file.length());

		// ���ս��, ����Ѵ��ڸ�����ʾ, �����˳�
		String result = br.readLine();
		if ("����".equals(result)) {
			System.out.println("�ļ��Ѵ���, �벻Ҫ�ظ��ϴ�!");
			return;
			}

		// ����������ָ���ļ�, ��ȡ�ļ�д��������
		FileInputStream fis = new FileInputStream(file);
		long filelength = Long.parseLong(result); // ���������ɵĴ�С     Long.parseLong()���ַ���������Ϊһ�����ŵ�ʮ����
		fis.skip(filelength); // �ϴ�����˶��پ��������� skip������������������ n�ֽ��������е����ݡ�
		byte[] buffer = new byte[1024];
		int len;
		while ((len = fis.read(buffer)) != -1){
		// ��ȡ�ļ�
			os.write(buffer, 0, len); // д������
			}
		
		fis.close();
		socket.close();
		System.out.println("�ϴ����!");
        }

	private static File getFile() {
		System.out.println("������Ҫ�ϴ����ļ�·��:");
		Scanner scanner = new Scanner(System.in);
		while (true) {
			File file = new File(scanner.nextLine());
			//����ļ���Ŀ¼�Ƿ�������ֳ���·����
			if (!file.exists())        
				System.out.println("�������·��������, ����������:");
			else if (file.isDirectory())       //�����ļ��Ƿ�ͨ�����ֳ���·������ʾ��һ��Ŀ¼�� 
				System.out.println("�ݲ�֧���ļ����ϴ�, ������һ���ļ�·��:");
			else
				return file;
		}
	}
}
