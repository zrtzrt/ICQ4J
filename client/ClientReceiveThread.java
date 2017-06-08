package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class ClientReceiveThread extends Thread{

	private JTextArea a = null;
	private JComboBox c = null;
	private Socket sk = null;
	private ObjectInputStream in = null;
//	private ObjectOutputStream out = null;
	private JLabel l = null;
	private IMPanelClient p = null;
	public boolean isStop;
	String n;
	
	public ClientReceiveThread(Socket sk ,ObjectInputStream in ,ObjectOutputStream out ,JTextArea a ,JComboBox c,String n ,JLabel l ,IMPanelClient p){
		this.sk = sk;
		this.in = in;
//		this.out = out;
		this.a = a;
		this.c = c;
		this.n = n;
		this.l = l;
		this.p = p;
		isStop = false;
	}
	
	public void run(){
		if (sk!=null && !sk.isClosed()) {
			while (!isStop && sk!=null && !sk.isClosed()) {
				try {
					String m = (String)in.readObject();
					if (m.equalsIgnoreCase("userOn")) {
						String n = (String)in.readObject();
						a.append("欢迎"+n+"来到此聊天室\n\n");
						//c.addItem(n);
						String[] userList = (String[])in.readObject();
						c.removeAllItems();
						c.addItem("所有人");
						l.setText("当前在线人数："+userList.length);
						for (int i = 0; i < userList.length; i++) {
							c.addItem(userList[i]);
						}
					}
					if (m.equalsIgnoreCase("userOff")) {
						String n = (String)in.readObject();
						a.append(n+"离开了此聊天室\n\n");
						//c.removeItem(n);
						String[] userList = (String[])in.readObject();
						c.removeAllItems();
						c.addItem("所有人");
						l.setText("当前在线人数："+userList.length);
						for (int i = 0; i < userList.length; i++) {
							c.addItem(userList[i]);
						}
					}
					if (m.equalsIgnoreCase("userList")) {
						String[] userList = (String[])in.readObject();
						c.removeAllItems();
						c.addItem("所有人");
						for (int i = 0; i < userList.length; i++) {
							c.addItem(userList[i]);
						}
						l.setText("当前在线人数："+userList.length);
					}
					if (m.equalsIgnoreCase("ban")) {
						String name = (String)in.readObject();
						c.removeItem(name);
						a.append(name+"被管理员踢出此聊天室！\n\n");
						if (name.equals(n)) {
							JOptionPane.showMessageDialog(null,"你已被管理员踢出此聊天室！","错误提示",JOptionPane.ERROR_MESSAGE);
							p.dispose();
							new UserLogin();
							sk.close();
						}
					}
					if (m.equalsIgnoreCase("chat")) {
						String n = (String)in.readObject();
						a.append(n);
						a.setCaretPosition(a.getDocument().getLength());
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}
}
