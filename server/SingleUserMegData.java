package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SingleUserMegData {
	private String userName,msg,pass,chath;
	private Socket socket = null;
	private ObjectInputStream dataIn = null;
	private ObjectOutputStream dataOut = null;
	private boolean isGoust = false , isKeep = false;
	
	public boolean isKeep() {
		return isKeep;
	}
	public void setKeep(boolean iskeep) {
		this.isKeep = iskeep;
	}
	public String getChath() {
		return chath;
	}
	public void setChath(String chath) {
		this.chath = chath;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public ObjectInputStream getDataIn() {
		return dataIn;
	}
	public void setDataIn(ObjectInputStream dataIn) {
		this.dataIn = dataIn;
	}
	public ObjectOutputStream getDataOut() {
		return dataOut;
	}
	public void setDataOut(ObjectOutputStream dataOut) {
		this.dataOut = dataOut;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public boolean isGoust() {
		return isGoust;
	}
	public void setGoust(boolean isGoust) {
		this.isGoust = isGoust;
	}
	
	SingleUserMegData(){
		
	}
	
}