package io.anuke.ucore.bots;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public interface MessageHandler{
	void send(String message, String chatid);
	void sendFile(File file, String chatid);
	void edit(String message, String chatid, String messageid);
	void setMessageListener(MessageListener listener);
	String getUserName(String id);
	
	public interface MessageListener{
		void onMessageRecieved(String message, String username, String chatid, String userid, String messageid);
		void onFileRecieved(String userid, String chatid, String fileid);
	}
	
	public abstract static class TimedMessageHandler implements MessageHandler{
		String lastmessage;
		ConcurrentHashMap<String, String> messages = new ConcurrentHashMap<String, String>();

		
		public void send(String message, final String id){
			if(messages.containsKey(id)){
				messages.put(id, messages.get(id) + "\n" + message);
				return;
			}else{
				messages.put(id, message);
			}
			new java.util.Timer().schedule(new java.util.TimerTask(){
				String chat = id;

				@Override
				public void run(){
					sendRaw(messages.get(chat), chat);
					messages.remove(chat);
				}
			}, 50);
		}
		
		abstract public String sendRaw(String message, String chatid);
	}
}
