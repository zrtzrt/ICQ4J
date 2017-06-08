package server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Vector;

import javax.swing.*;

public class IMServerFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Vector userInfoList,userNameList,GInfo;
	final JTextArea a1 = new JTextArea(22,15);
	final JTextArea a2 = new JTextArea(22,30);
	JComboBox c =new JComboBox();
	final JButton b3 = new JButton("踢出");
	JLabel lr = new JLabel("服务器未启动！");
	ServerListenThread listenThread = null;
	
	public IMServerFrame(){
		
		final JButton b1 = new JButton("启动服务");
//		final JButton b2 = new JButton("停止服务");
		final JTextField f= new JTextField(3);
		JLabel lip = new JLabel();
		JLabel lw = new JLabel("用户信息");
		JLabel le = new JLabel("聊天内容");
		JScrollPane sp1 = new JScrollPane(a1);
		JScrollPane sp2 = new JScrollPane(a2);
		JPanel pn = new JPanel();
//		JPanel ps = new JPanel();
		JPanel pl = new JPanel();
		JPanel pr = new JPanel();
		JSplitPane sp =new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		InetAddress ip = null;
		try {
			ip=InetAddress.getLocalHost();
			lip.setText(ip.getHostAddress()+":");
		} catch (Exception e) {
			lip.setText("127.0.0.1:");
		}
		
		this.setTitle("服务器");
		f.setText("6544");
		a1.setEditable(false);
		a2.setEditable(false);
//		b2.setEnabled(false);
		b3.setEnabled(false);
		a1.setCaretPosition(0);
		a2.setCaretPosition(0);
		sp.setDividerLocation(160);
		
		this.setLayout(new BorderLayout());
		c.addItem("所有人");
		pl.add(lw);
		pl.add(sp1);
		pr.add(le);
		pr.add(sp2);
		pn.add(lip);
		pn.add(f);
		pn.add(b1);
//		pn.add(b2);
		pn.add(c);
		pn.add(b3);
		pn.add(lr);
//		ps.setLayout(new GridLayout(1,2));
//		ps.add(pl);
//		ps.add(pr);
//		this.add(ps);
		sp.setLeftComponent(pl);
		sp.setRightComponent(pr);
		this.add(sp);
		this.add(pn,BorderLayout.SOUTH);
		this.setSize(520, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				b1.setEnabled(false);
//				b2.setEnabled(true);
				b3.setEnabled(true);
				f.setEditable(false);
				lr.setText("在线人数：0(隐身：0)");
				int port = Integer.parseInt(f.getText());
				if (port < 1) {
					port = 6544;
					f.setText("6544");
				}
				try {
					startServer(port);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
//		b2.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent arg0) {
//				b1.setEnabled(true);
//				b2.setEnabled(false);
//				b3.setEnabled(false);
//				f.setEditable(true);
//				a1.append("服务器已关闭！\n\n");
//				listenThread.interrupt();
//			}
//		});
	}
	
	public void startServer(int p) throws IOException{
		ServerSocket server = new ServerSocket(p,100);
		a1.append("服务器已启动、、、、、、\n\n");
		userInfoList = new Vector();
		GInfo = new Vector();
		userNameList = new Vector();
		listenThread = new ServerListenThread(server,a1,a2,b3,userInfoList,userNameList,GInfo,c,lr);
		listenThread.start();
	}

	public static void main(String[] args) {
		new IMServerFrame();
	}

}
