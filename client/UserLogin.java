package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;

public class UserLogin extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserLogin(){
		
		final JTextField f1= new JTextField(12);
		final JTextField f2= new JTextField(11);
		final JTextField f3= new JTextField(4);
		final JTextField f4= new JTextField(12);
		final JLabel lt = new JLabel("��ӭʹ������ͻ��ˣ�");
		JLabel l1 = new JLabel("�ǳƣ�");
		JLabel l2 = new JLabel("��������");
		JLabel l3 = new JLabel("�˿ڣ�");
		JLabel l4 = new JLabel("�����¼");
		JLabel l5 = new JLabel("��������");
		JLabel l6 = new JLabel("���룺");
		JButton b1 = new JButton("��½");
		JButton b2 = new JButton("ȡ��");
		JButton bt = new JButton("��������");
		final JRadioButton br1 = new JRadioButton();
		final JRadioButton br2 = new JRadioButton();
		JPanel p1  = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		JPanel p5 = new JPanel();
		JPanel p6 = new JPanel();
		JPanel pt = new JPanel();
		
		this.setTitle("��½");
		f2.setText("127.0.0.1");
		f3.setText("6544");
		f4.setEditable(false);
		
		this.setLayout(new GridLayout(7,1));
		pt.add(lt);
		p1.add(l1);
		p1.add(f1);
		p2.add(l2); 
		p2.add(f2);
		p3.add(l3); 
		p3.add(f3);
		p3.add(bt);
		p4.add(br1); 
		p4.add(l4);
		p4.add(br2); 
		p4.add(l5);
		p5.add(b1);
		p5.add(b2);
		p6.add(l6);
		p6.add(f4);
		this.add(pt);
		this.add(p1);
		this.add(p2);
		this.add(p3);
		this.add(p4);
		this.add(p6); 
		this.add(p5); 
		this.setSize(300, 250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String n = f1.getText() , ip = f2.getText();
				int port = Integer.parseInt(f3.getText());
				if (n==null || n.equals("")) {
					JOptionPane.showMessageDialog(null,"�ǳƲ���Ϊ��","������ʾ",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (br2.isSelected() && (n==null || n.equals(""))) {
					JOptionPane.showMessageDialog(null,"���벻��Ϊ��","������ʾ",JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					Socket s = new Socket(ip,port);
					ObjectInputStream in = new ObjectInputStream(s.getInputStream());
					ObjectOutputStream out= new ObjectOutputStream(s.getOutputStream());
					if (isLogin(in,out,n,br1.isSelected(),f4.getText())) {
						dispose();
						if (br1.isSelected())
							new IMPanelClient(s,in,out,n,"������������ģʽ���ʴ�������\nע�⣺�����û��޷����㵥������\n\n");
						else
							new IMPanelClient(s,in,out,n,null);
					}else{
						JOptionPane.showMessageDialog(null,"�ǳ��ѱ�ʹ��","������ʾ",JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"������û����Ӧ","404 NOT FIND",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {dispose();}
		});
		bt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String ip = f2.getText();
				int port = Integer.parseInt(f3.getText());
				try {
					Socket s = new Socket(ip,port);
					ObjectInputStream in = new ObjectInputStream(s.getInputStream());
					ObjectOutputStream out= new ObjectOutputStream(s.getOutputStream());
					out.writeObject("test");
					out.flush();
					String nop = (String)in.readObject();
					lt.setText("���ӳɹ� : ��ǰ��������"+nop+"������");
				} catch (Exception e) {
					lt.setText("����ʧ�ܣ�404��: ������û����Ӧ");
				}
			}
		});
		br2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (br2.isSelected())
					f4.setEditable(true); 
				else
					f4.setEditable(false);
			}
		});
		
	}
	public static void main(String[] args) {
		new UserLogin();
	}
	
//	public boolean isLogin(ObjectInputStream in ,ObjectOutputStream out ,String name){
//		boolean r =false;
//		try {
//			out.writeObject("login");
//			out.flush();
//			out.writeObject(name);
//			out.flush();
//			String msg = (String)in.readObject();
//			if (msg.equalsIgnoreCase("success"))
//				r=true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return r;
//	}
	
	public boolean isLogin(ObjectInputStream in ,ObjectOutputStream out ,String name ,boolean g ,String pass){
		boolean r =false;
		try {
			out.writeObject("login");
			out.flush();
			out.writeObject(g);
			out.flush();
			out.writeObject(pass);
			out.flush();
			out.writeObject(name);
			out.flush();
			String msg = (String)in.readObject();
			if (msg.equalsIgnoreCase("success"))
				r=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
		
	}

}
