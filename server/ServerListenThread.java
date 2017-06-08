package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;

public class ServerListenThread extends Thread{
	
	ServerSocket server = null;
	private Vector userInfoList = null , userNameList = null , GInfo = null;
	private JTextArea a1 = null , a2 = null;
	private JComboBox c = null;
	private JButton b = null;
	private JLabel l = null;
	public boolean isStop;
	
	public ServerListenThread(ServerSocket server ,JTextArea a1 ,JTextArea a2 ,JButton b ,Vector userInfoList ,Vector userNameList ,Vector GInfo ,
	JComboBox c ,JLabel l){
		this.server = server;
		this.userInfoList = userInfoList;
		this.userNameList = userNameList;
		this.GInfo = GInfo;
		this.a1 = a1;
		this.a2 = a2;
		this.c = c;
		this.b = b;
		this.l = l;
		isStop = false;
	}

	public void run(){
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String n = (String) c.getSelectedItem();
				if (n.equals("������")) {
					JOptionPane.showMessageDialog(null,"�㲻�ܽ�������ȫ���߳���","������ʾ",JOptionPane.ERROR_MESSAGE);
					return;
				}
				int o = JOptionPane.showOptionDialog(null,"��ȷ����"+n+"�߳���","��ѡ��",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane. INFORMATION_MESSAGE, null, null, null);
				if (o==0) {
					for (int i = 0; i < userInfoList.size(); i++) {
						SingleUserMegData u =(SingleUserMegData)userInfoList.get(i);
						try {
							u.getDataOut().writeObject("ban");
							u.getDataOut().flush();
							u.getDataOut().writeObject(n);
							u.getDataOut().flush();
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null,"��Ϣ����ʧ��","404 NOT FIND",JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					for (int i = 0; i < GInfo.size(); i++) {
						SingleUserMegData u =(SingleUserMegData)GInfo.get(i);
						try {
							u.getDataOut().writeObject("ban");
							u.getDataOut().flush();
							u.getDataOut().writeObject(n);
							u.getDataOut().flush();
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null,"��Ϣ����ʧ��","404 NOT FIND",JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					a1.append("���ѽ�"+n+"�߳���\n\n");
					a1.setCaretPosition(a1.getDocument().getLength());
					SingleUserMegData d = (SingleUserMegData) userInfoList.get(userNameList.indexOf(n));
					userInfoList.remove(d);
					userNameList.remove(n);
					c.removeItem(n);
					l.setText("����������"+Integer.toString(userInfoList.size())+"(����"+Integer.toString(userInfoList.size()-userNameList.size())+")");
				}
			}
		});
		
		while(!isStop && !server.isClosed()){
			try {
				SingleUserMegData d = new SingleUserMegData();
				Socket s = server.accept();
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
				out.flush();
				ObjectInputStream in = new ObjectInputStream(s.getInputStream());
				d.setSocket(s);
				d.setDataOut(out);
				d.setDataIn(in);
				String m = (String)in.readObject();
				if (m.equalsIgnoreCase("login")) {
					String name = (String)in.readObject();
					String g = (String)in.readObject();
					String pass = (String)in.readObject();
					if (g.equals(true))
						d.setGoust(true);
					if (pass.length()!=0) {
						d.setKeep(true);
						d.setPass(pass);
					}
					JDBCThread jt = new JDBCThread(a1,a2,userInfoList,userNameList,GInfo,c,d,name,l);
					jt.start();
//					l.setText("����������"+Integer.toString(userInfoList.size()+1)+"(����"+Integer.toString(userInfoList.size()-userNameList.size())+")");
				}
//				if (m.equalsIgnoreCase("Glogin")) {
//					d.setGoust(true);
//					String name = (String)in.readObject();
//					JDBCThread jt = new JDBCThread(a1,a2,userInfoList,userNameList,GInfo,c,d,name,l);
//					jt.start();
////					l.setText("����������"+Integer.toString(userInfoList.size()+1)+"(����"+Integer.toString(userInfoList.size()-userNameList.size()+1)+")");
//				}
				if (m.equalsIgnoreCase("test")) {
					try {
						d.getDataOut().writeObject(Integer.toString(userNameList.size()));
						d.getDataOut().flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,"���Ѿ��ر��˼����׽���","ϵͳ����",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}

