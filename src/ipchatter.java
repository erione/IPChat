/**
 * @(#)ipchatter.java
 *
 *
 * @author Brijesh Patel  
 * @version 1.00 2009/7/27
 */

import java.sql.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

public class ipchatter 
{
	String userName = null,password = null,code = null;
	int cnt = 1,cnt1 = 0;
	Connection conn;
	
	public void pass()
	{
		try
		{
			String nativeLF = UIManager.getSystemLookAndFeelClassName(); // Install the look and feel 
			 UIManager.setLookAndFeel(nativeLF); 
		 
			   pass = new JFrame("Login");
			   pass.setLocation(250,250);
			   pass.setIconImage(Toolkit.getDefaultToolkit().getImage("chat_32.png"));
           	   pass.setLayout( new GridLayout(4,2,3,3) );
           	
           	   pass.setSize(250,115);
           	   pass.setAlwaysOnTop(true);
           	   pass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           	   p1 = new JTextField();
           	   p3 = new JTextField();
           	   p2 = new JPasswordField();
           	   b1 = new JButton(" OK ");
           	   b1.setToolTipText("Login");
           	   pass.add( new JLabel("Enter Username : ",SwingConstants.CENTER) );
           	   pass.add( p1 );
           	   pass.add( new JLabel("Enter Password : ",SwingConstants.CENTER) );
           	   pass.add( p2 );
           	   pass.add( new JLabel("Enter Code : ",SwingConstants.CENTER) );
           	   pass.add( p3);
           	   pass.add( new JLabel("") );
           	   pass.add( b1 );
           	   KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false);
           	   p1.registerKeyboardAction(new passListener(), ks, JComponent.WHEN_FOCUSED);
			   p2.registerKeyboardAction(new passListener(), ks, JComponent.WHEN_FOCUSED);
			   p3.registerKeyboardAction(new passListener(), ks, JComponent.WHEN_FOCUSED);
			   b1.registerKeyboardAction(new passListener(), ks, JComponent.WHEN_FOCUSED);
           	   b1.addActionListener( new passListener() );        	     			         	   
		   pass.setVisible(true);
		}
		
		catch(Exception e)
		{	e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	class passListener implements ActionListener 
	{
		public void actionPerformed( ActionEvent event )
		{
			try
			{
				userName = p1.getText();
			password = p2.getText();
			code = p3.getText();
			String url = "jdbc:mysql://10.100.99.55/test";
          		  Class.forName ("com.mysql.jdbc.Driver").newInstance ();
		            conn = DriverManager.getConnection (url, userName, password);
		            System.out.println ("Database connection established");	
										          
		            Statement ps1 = conn.createStatement();
		       	    String sql = "CREATE TABLE IF NOT EXISTS " + code +"(cnt INT,chat VARCHAR(255))";
			        ps1.executeUpdate(sql);
		            pass.hide();
		            main_frame();
			}
			catch(Exception e)
			{
				System.err.println( e.getMessage() );
			}
		}
	}	
	public void main_frame()
	{
		try
		{
			ip = new JFrame("IP-Chat");
			ip.setLocation(250,0);
			ip.setLayout( new BorderLayout() );
			ip.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			ip.setSize(400,350);
			ip.setIconImage(Toolkit.getDefaultToolkit().getImage("chat_32.png"));
			t1 = new JTextArea();
			t1.setEditable( false );
			pane = new JScrollPane( t1 );
			p4 = new JTextField("Type Here");
			ip.add(p4,BorderLayout.SOUTH);
			p4.setFocusable( true );
			ip.add(pane,BorderLayout.CENTER);
			KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false);
			p4.registerKeyboardAction( new ipListener() ,ks,JComponent.WHEN_FOCUSED);
			Timer t = new Timer(1000,new refresh() );
			t.start();
			ip.setVisible( true );
		}
		catch(Exception e)
		{
			System.err.println( e.getMessage() );
		}
	}
	class ipListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event)
		{
			try
			{
				cnt1 = 1;
				String string,sql1;
				Statement ps1 = conn.createStatement();
				sql1 = "SELECT * FROM "+code;
				ps1.executeQuery(sql1);
				ResultSet rs = ps1.getResultSet();
				while(rs.next())
				{
					++cnt1;
				}
				Statement ps2 = conn.createStatement();
				String str = "" + userName + " : " + p4.getText() +""; 
				String sql = "INSERT INTO "+code+"(cnt,chat) VALUES("+cnt1+",'"+str+"'"+")";
				ps2.executeUpdate(sql);
				ps2.close();	
				p4.setText( null );
			}
			catch(Exception e)
			{
				System.err.println( e.getMessage() );
			}
		}
		
	}
	class refresh implements ActionListener
	{
		public void actionPerformed( ActionEvent event)
		{
			try
			{
				String string,sql;
				Statement ps2 = conn.createStatement();
				sql = "SELECT * FROM "+code;
				ps2.executeQuery(sql);
				ResultSet rs = ps2.getResultSet();				
				while( rs.next() )
				{
					if( cnt == rs.getInt("cnt") )
					{
						string = rs.getString("chat")+"\n";
						t1.append(string);
						cnt++;
					}
				}
				
			}
			catch(Exception e)
			{
	
				System.err.println( e.getMessage() );
			}
		}
		
	}
      public static void main( String [] args)
      {
      	ipchatter ip = new ipchatter();
      	ip.pass();
      }
      JFrame ip,pass;
      JTextField p1,p3,p4;
      JTextArea t1;
      JScrollPane pane;
      JPasswordField p2;
      JButton b1;
}