package server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class ServerReceiveThread extends Thread{

	private Vector userInfoList = null , userNameList = null , GInfo = null;
	private JTextArea a1 = null , a2 = null;
	private JComboBox c = null;
	private SingleUserMegData d = null;
	private JLabel l = null;
	public boolean isStop;
	
	public ServerReceiveThread(JTextArea a1 ,JTextArea a2 ,SingleUserMegData d ,Vector userInfoList ,Vector userNameList ,
			Vector GInfo, JComboBox c ,JLabel l){
				this.d = d;
				this.userInfoList = userInfoList;
				this.userNameList = userNameList;
				this.GInfo = GInfo;
				this.a1 = a1;
				this.a2 = a2;
				this.c = c;
				this.l = l;
				isStop = false;
			}
	
	public void run(){
		while (!isStop && !d.getSocket().isClosed()) {
			try {
				String m = (String)d.getDataIn().readObject();
				if (m.equalsIgnoreCase("off")) {
					String n =d.getUserName();
					c.removeItem(n);
					
					if (userInfoList.size()==0) {
						return;
					}
					if (!d.isGoust()) {
						userInfoList.remove(d);
						userNameList.remove(n);
						a1.append(n+"下线了！\n\n");
						a1.setCaretPosition(a1.getDocument().getLength());
						String[] userList = (String[])userNameList.toArray(new String[0]);
						l.setText("在线人数："+Integer.toString(userInfoList.size())+"(隐身："+Integer.toString(GInfo.size())+")");
						for (int i = 0; i < userInfoList.size(); i++) {
							SingleUserMegData u =(SingleUserMegData)userInfoList.get(i);
							u.getDataOut().writeObject("userOff");
							u.getDataOut().flush();
							u.getDataOut().writeObject(n);
							u.getDataOut().flush();
	//						u.getDataOut().writeObject("userList");
	//						u.getDataOut().flush();
							u.getDataOut().writeObject(userList);
							u.getDataOut().flush();
						}
						for (int i = 0; i < GInfo.size(); i++) {
							SingleUserMegData u =(SingleUserMegData)GInfo.get(i);
							u.getDataOut().writeObject("userOff");
							u.getDataOut().flush();
							u.getDataOut().writeObject(n);
							u.getDataOut().flush();
	//						u.getDataOut().writeObject("userList");
	//						u.getDataOut().flush();
							u.getDataOut().writeObject(userList);
							u.getDataOut().flush();
						}
					}else{
						a1.append("隐身用户"+n+"下线了！\n\n");
						a1.setCaretPosition(a1.getDocument().getLength());
						GInfo.remove(d);
						l.setText("在线人数："+Integer.toString(userInfoList.size())+"(隐身："+Integer.toString(GInfo.size())+")");
					}
					break;
				}
				if (m.equalsIgnoreCase("msg")) {
					Date t = new Date();
					SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
					String tg =(String) d.getDataIn().readObject();
					String msg =(String) d.getDataIn().readObject();
					String time = f.format(t) , n = d.getUserName();
					String allm = time+"\n"+n+"对"+tg+"说：\n"+msg+"\n\n";
					a2.append(allm);
					a2.setCaretPosition(a2.getDocument().getLength());
					
					if (tg.equalsIgnoreCase("所有人")) {
						for (int i = 0; i < userInfoList.size(); i++) {
							SingleUserMegData u =(SingleUserMegData)userInfoList.get(i);
//							if (n.equals(u)) 
//								continue;
							u.getDataOut().writeObject("chat");
							u.getDataOut().flush();
							u.getDataOut().writeObject(allm);
							u.getDataOut().flush();
						}
						for (int i = 0; i < GInfo.size(); i++) {
							SingleUserMegData u =(SingleUserMegData)GInfo.get(i);
//							if (n.equals(u)) 
//								continue;
							u.getDataOut().writeObject("chat");
							u.getDataOut().flush();
							u.getDataOut().writeObject(allm);
							u.getDataOut().flush();
						}
					}
					else {
						SingleUserMegData u = (SingleUserMegData) userInfoList.get(userNameList.indexOf(tg));
						if (u.getSocket() != null && !u.getSocket().isClosed()) {
							u.getDataOut().writeObject("chat");
							u.getDataOut().flush();
							u.getDataOut().writeObject(allm);
							u.getDataOut().flush();
						}
						if (d.getSocket() != null && !d.getSocket().isClosed()) {
							d.getDataOut().writeObject("chat");
							d.getDataOut().flush();
							d.getDataOut().writeObject(allm);
							d.getDataOut().flush();
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				//System.exit(0);
				break;
			}
		}
	}

}
