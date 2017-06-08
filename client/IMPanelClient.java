package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;

public class IMPanelClient extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final Socket sk;
	final ObjectInputStream in;
	final ObjectOutputStream out;
	final String n;
	ClientReceiveThread r;
	JTabbedPane tp = new JTabbedPane();
	final JTextArea a1 = new JTextArea(20,100);
	final JTextArea a2 = new JTextArea(10,100);
	final JComboBox c =new JComboBox();
	
	public IMPanelClient(final Socket sk ,ObjectInputStream in ,final ObjectOutputStream out ,final String n ,String t){
		
		this.sk = sk;
		this.in = in;
		this.out = out;
		this.n = n;
		
		JButton bs = new JButton("����");
		JPanel p = new JPanel();
		JScrollPane sp1 = new JScrollPane(a1);
		JLabel l = new JLabel("��ǰ����������0");
		JSplitPane s =new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		this.setTitle(n+"�����촰��");
		this.setLayout(new BorderLayout());
		a1.setEditable(false);
		a2.setEditable(true);
		s.setTopComponent(sp1);
		s.setBottomComponent(tp);
		
		c.addItem("������");
		p.add(l);
		p.add(bs);
		p.add(c); 
		tp.addTab("�����",a2);
		this.add(s);
		this.add(p,BorderLayout.SOUTH);
		this.setSize(300, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		r = new ClientReceiveThread(sk,in,out,a1,c,n,l,this);
		r.start();
		
		String[] face = {"_(������:)_","_(:3 ����)_","(��y����)~*","(�𡯦ء���)","�t(*�㨌��*)�s","o(*�R���Q)��",
				"�B���B","�B��B","�Ѧء�","�ѩn��","�ѡ���","��_��","��_��","������","�R�بQ"};
		makepane("����",face);
		String[] word = {"��Һã�","�þò���","��������","��Ҫ���ˣ��ټ�","������������"};
		makepane("������",word);
//		setting();
		
		a1.append(t);
		
		bs.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				send(a2.getText());
				a2.setText(null);
			}
		});
		addWindowListener(new WindowListener(){
			public void windowClosing(WindowEvent arg0) {
				if (sk!=null && !sk.isClosed()) {
					try {
						out.writeObject("off");
						out.flush();
						Thread.sleep(1000);
						sk.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			public void windowOpened(WindowEvent arg0) {
			}
			public void windowIconified(WindowEvent arg0) {
			}
			public void windowDeiconified(WindowEvent arg0) {
			}
			public void windowActivated(WindowEvent arg0) {
			}
			public void windowDeactivated(WindowEvent arg0) {
			}
			public void windowClosed(WindowEvent arg0) {
			}
		});
	}

//	private void setting() {
//		JPanel p = new JPanel();
//		p.setLayout(new GridLayout(6,1));
//		
//		tp.addTab("����",p);
//	}


	private void makepane(String name, String[] face) {
		
		JPanel p = new JPanel();
		JPanel p1 = new JPanel();
		final JPanel p2 = new JPanel();
		JLabel l = new JLabel("ֱ�ӷ���");
		final JRadioButton br = new JRadioButton();
		JButton ba = new JButton("���");
		//final JScrollPane sp = new JScrollPane(p2);
		final JTextField f= new JTextField(8);
		
		JButton[] b = new JButton[face.length];
		for (int i = 0; i < face.length; i++) {
			b[i] = new JButton(face[i]);
			p2.add(b[i]);
			
			final String m = face[i];
			b[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if (!br.isSelected()) {
						a2.append(m);
					}else
						send(m);
				}
			});
		}
		
		ba.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				final String text = f.getText();
				if (text.length()==0) {
					JOptionPane.showMessageDialog(null,"��ӵ����ݲ���Ϊ�գ�","����",JOptionPane.ERROR_MESSAGE);
				}
				JButton bm = new JButton(text);
				p2.add(bm);
				
				bm.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						if (!br.isSelected()) {
							a2.append(text);
						}else
							send(text);
					}
				});
			}
		});
		
		p.setLayout(new BorderLayout());
		p.add(p1,BorderLayout.SOUTH);
		p.add(p2);
		p1.add(br);
		p1.add(l);
		p1.add(f);
		p1.add(ba);
		tp.addTab(name,p);
	}
	
	private void send(String text) {
//		Date d = new Date();
//		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		String tg = (String) c.getSelectedItem() ;
		//String time = f.format(d);
		//a1.append(time+"\n"+n+"��"+tg+"˵��\n"+text+"\n\n");
		if (text.length()==0) {
			JOptionPane.showMessageDialog(null,"���͵����ݲ���Ϊ�գ�","����",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (sk!=null && !sk.isClosed()) {
			try {
				out.writeObject("msg");
				out.flush();
				out.writeObject(tg);
				out.flush();
				out.writeObject(text);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
//	 public static void Write(String fileName, String content) {
//	        try {
//	            //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
//	            FileWriter writer = new FileWriter(fileName, true);
//	            writer.write(content);
//	            writer.close();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	}
}
