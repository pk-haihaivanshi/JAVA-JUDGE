import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Client
{
	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				MainFrame mFrame = new MainFrame();
				mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mFrame.setVisible(true);
			}
		});
	}
}
class MainFrame extends JFrame implements ActionListener
{
	JButton submit;
	JTextArea code_area;
	JScrollPane scroll;
	public MainFrame()
	{
		/****************************Setting resolutions*************************/
		setTitle("Java Judge");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int H = screenSize.height;
		int W = screenSize.width;
		setBounds(W/4,H/4,W/2,H/2);
		setResizable(false);
		
		/*****************************Setting icon*******************************/
		
		 Image icon = kit.getImage("icon.jpg");
		 setIconImage(icon);
		 
		/*****************************Setting the Content Pane********************/
		submit = new JButton("Compile and Run");
		submit.addActionListener(this);
		code_area = new JTextArea("//Code here.........\n",8,40);
		code_area.append("//Class name should be Solution\n");
		code_area.append("//Don't declare public class");
		scroll = new JScrollPane(code_area);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroll,BorderLayout.CENTER);
		add(submit,BorderLayout.SOUTH);
	}
	public void actionPerformed(ActionEvent e)
	{
		String txt = code_area.getText();
		try
		{      
			Socket s = new Socket("localhost",5566);  
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
			BufferedReader din=new BufferedReader(new InputStreamReader(s.getInputStream())); 
			
			dout.writeUTF(txt); 
			dout.flush();
			code_area.setText("");
			int line;
			while ((line = din.read()) != -1) 
			{
				code_area.append((char)line+"");
			}
			dout.close();  
			din.close();
			s.close();  
		}
		catch(Exception ex)
		{
			code_area.setText("Error Server Not Available: Please Try Again Later");
		}  
	}
}
