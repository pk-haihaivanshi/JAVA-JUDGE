import java.io.*;
import java.util.*;
import java.net.*;
public class Server
{
	static int _PORT = 5566;
	static boolean _SERVER_ON = true;
	public static void main(String args[])
	{
		try
		{
			ServerSocket ss = new ServerSocket(_PORT);
			System.out.println("Running Server.......");
			int count=0;
			while(_SERVER_ON)
			{
				Socket s = ss.accept();
				count++;
				System.out.println("Client "+count+" Connected.......");
				new Thread(new NewServerThread(s,count)).start();
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}
class NewServerThread implements Runnable
{
	Socket newClient;
	DataOutputStream dout;
	File f;
	int clientNumber;
	StringBuffer outputFile = new StringBuffer("Code");
	StringBuffer outputFileRun = new StringBuffer("Code");
	public NewServerThread(Socket newClient,int clientNumber)
	{
		this.newClient = newClient;
		this.clientNumber = clientNumber;
		try
		{
			dout = new DataOutputStream(newClient.getOutputStream());
		}
		catch(Exception e)
		{
			try
			{
				dout.writeUTF("Error : Server Not Found");
				dout.flush();
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
			return;
		}
	}
	public void run()
	{
		String code_file = null;
		String coderun_file = null;
		try
		{
			
			DataInputStream dis = new DataInputStream(newClient.getInputStream());
			String input = (String)dis.readUTF();
			
			outputFile.append(clientNumber+"");
			outputFile.append(".java");
			outputFileRun.append(clientNumber+"");
			
			try
			{
				
				f = new File("C:\\Users\\Acer\\Desktop\\JAVAJUDGE\\ClientData\\Folder"+clientNumber);
				boolean isCreated = f.mkdir();
				code_file = f.getAbsolutePath()+"\\"+outputFile.toString();
				coderun_file = f.getAbsolutePath()+"\\"+outputFileRun.toString();
				FileWriter fw = new FileWriter(code_file);
				fw.write(input);
				fw.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			dout = new DataOutputStream(newClient.getOutputStream());
			runProcess("javac -cp src C:\\Users\\Acer\\Desktop\\JAVAJUDGE\\ClientData\\Folder"+clientNumber+"\\"+outputFile.toString());	
			runProcess("java -cp C:\\Users\\Acer\\Desktop\\JAVAJUDGE\\ClientData\\Folder"+clientNumber+" Solution");
			dout.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		new DeletionThread(f).start();
	}
	public void printLines(InputStream ins) throws Exception 
	{
        int line;
        BufferedReader in = new BufferedReader(
            new InputStreamReader(ins));
		
        while ((line = in.read()) != -1) {
            dout.write(line);
        }
		
		
    }

	  public void runProcess(String command) throws Exception 
	  {
		Process pro = Runtime.getRuntime().exec(command);
		printLines(pro.getInputStream());
		printLines(pro.getErrorStream());
		pro.waitFor();
		System.out.println(command + " exitValue() " + pro.exitValue());
		dout.flush();
	  }
	static class DeletionThread extends Thread
	{
		File f;
		public DeletionThread(File f)
		{
			this.f = f;
		}
		public void run()
		{
			deleteFolder(f);
		}
		public void deleteFolder(File f)
		{
			for(File subFile : f.listFiles())
			{
				if(subFile.isDirectory())
					deleteFolder(subFile);
				else
					subFile.delete();
			}
			f.delete();
		}
	}
	
}