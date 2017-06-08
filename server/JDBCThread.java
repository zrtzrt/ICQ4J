package server;

import java.util.Vector;

import javax.swing.*;

public class JDBCThread extends Thread{
	
	private Vector userInfoList = null , userNameList = null , GInfo = null;
	private JTextArea a1 = null , a2 = null;
	private JComboBox c = null;
	private SingleUserMegData d = null;
	private JLabel l = null;
	private String name = null;
	
	public JDBCThread(JTextArea a1 ,JTextArea a2 ,Vector userInfoList ,Vector userNameList ,Vector GInfo ,
			JComboBox c ,SingleUserMegData d ,String name ,JLabel l){
				this.userInfoList = userInfoList;
				this.userNameList = userNameList;
				this.GInfo = GInfo;
				this.a1 = a1;
				this.a2 = a2;
				this.c = c;
				this.d = d;
				this.l = l;
				this.name = name;
			}
	
	public void run(){
		try {
			if (!userNameList.contains(name)) {
				d.getDataOut().writeObject("success");
				d.getDataOut().flush();
				d.setUserName(name);
				c.addItem(name);
				if (!d.isGoust()) {
					userInfoList.add(d);
					userNameList.add(name);
					a1.append(name+"登陆了！\n\n");
					a1.setCaretPosition(a1.getDocument().getLength());
					String[] userList = (String[])userNameList.toArray(new String[0]);
					l.setText("在线人数："+Integer.toString(userInfoList.size())+"(隐身："+Integer.toString(GInfo.size())+")");
					for (int i = 0; i < userInfoList.size(); i++) {
						SingleUserMegData u =(SingleUserMegData)userInfoList.get(i);
						u.getDataOut().writeObject("userOn");
						u.getDataOut().flush();
						u.getDataOut().writeObject(name);
						u.getDataOut().flush();
	//					u.getDataOut().writeObject("userList");
	//					u.getDataOut().flush();
						u.getDataOut().writeObject(userList);
						u.getDataOut().flush();
					}
					for (int i = 0; i < GInfo.size(); i++) {
						SingleUserMegData u =(SingleUserMegData)GInfo.get(i);
						u.getDataOut().writeObject("userOn");
						u.getDataOut().flush();
						u.getDataOut().writeObject(name);
						u.getDataOut().flush();
	//					u.getDataOut().writeObject("userList");
	//					u.getDataOut().flush();
						u.getDataOut().writeObject(userList);
						u.getDataOut().flush();
					}
				}else{
					GInfo.add(d);
					a1.append(name+"隐身登录了！\n\n");
					a1.setCaretPosition(a1.getDocument().getLength());
					l.setText("在线人数："+Integer.toString(userInfoList.size())+"(隐身："+Integer.toString(GInfo.size())+")");
					String[] userList = (String[])userNameList.toArray(new String[0]);
					d.getDataOut().writeObject("userList");
					d.getDataOut().flush();
					d.getDataOut().writeObject(userList);
					d.getDataOut().flush();
				}
			}else{
				d.getDataOut().writeObject("fail");
				d.getDataOut().flush();
			}
			ServerReceiveThread r = new ServerReceiveThread(a1,a2,d,userInfoList,userNameList,GInfo,c,l);
			r.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
