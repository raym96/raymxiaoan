package model.myfoodora;

import java.util.Date;

import model.user.MessageBoardUser;
import model.user.User;

public class Message {

	private Date date;
	private String msgContent;
	private String poster; //message poster's name
	
	public Message(String msgContent) {
		//when the poster is not specified it's the Myfoodora system
		super();
		this.date = new Date();
		this.msgContent = msgContent;
		this.poster = "SYSTEM";
	}
	
	public Message(String poster, String msgContent) {
		super();
		this.date = new Date();
		this.msgContent = msgContent;
		this.poster = poster;
	}

	@Override
	public String toString() {
		return date + ", "+poster+": "+msgContent;
	}
	
	
}