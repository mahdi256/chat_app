package com.muc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FileQuery {

	private Server server;
	private static boolean hasBeenUsed = false;

	public FileQuery(Server server){
		this.server = server;
	}
	//Methode um den Dateinamen eines Chats aus einem mitgegebenen String[] bestehend aus allen Usernamen zu generieren
	public File getFilename(String[] participant) {
		String path = "";
		try {
			path = URLDecoder.decode(FileQuery.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
			if(server.getServerPort()==9999){
				path = path+"Server1/";
			}else if(server.getServerPort()==9998){
				path = path+"Server2/";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//sortiere participant[] lexikographisch
		String temp;
		for(int toBeSortedParticipant = 0; toBeSortedParticipant < participant.length ; toBeSortedParticipant++) {
			for(int toBeComparedToParticipant = toBeSortedParticipant + 1; toBeComparedToParticipant < participant.length; toBeComparedToParticipant++) {
				if(participant[toBeSortedParticipant].compareTo(participant[toBeComparedToParticipant]) > 0) {
					temp = participant[toBeSortedParticipant];
					participant[toBeSortedParticipant] = participant[toBeComparedToParticipant];
					participant[toBeComparedToParticipant] = temp;
				}
			}			
		}
		
		// erstelle Pfad aus Dateipfad und allen participants
		for (int i = 0; i < participant.length; i++) {
			if(!participant[i].isEmpty() || !(participant[i] == null) || !(participant[i] == "")) {
				path = path + participant[i] + "#"; // # is used as seperator
			}
		}

		File chatfile = new File(path + ".txt");
		return chatfile;
	}

	//Methode um zu prüfen ob ein bestimmter Chat schon als Datei existiert
	public Boolean findChatFile(String[] participant) {
		File chatfile = getFilename(participant);
		Boolean exits = chatfile.exists();
		return exits;
	}
	

	//Methode um eine Datei zu erstellen, die den Chatverlauf enthält
	public void createChatFile(String[] participant) {

		File chatfile = getFilename(participant);
		System.out.println("chatfile path: "+chatfile.getPath());
		String msg ="createChat ";
		for(int i = 0;i<participant.length;i++){
			msg=msg+participant[i]+" ";
		}
		server.redirectMsg(msg);
		try {
			if (chatfile.createNewFile()) {
				System.out.println("chatfile created succesfully at: " + chatfile);
			} else {
				System.out.println("chatfile could not be created");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//Methode um eine Nachricht einer bereits existierenden Chat-Datei hinzuzufügen
	public void writeChatMessage(String[] participant,String sender, String message, String time) {

		File chatfile = getFilename(participant);
		String msg="writeMessage ";
		for(int i = 0;i<participant.length;i++){
			msg=msg+participant[i]+"#";
		}
		msg=msg+" "+sender+" "+time+" "+message;
		if(!hasBeenUsed) {
			server.redirectMsg(msg);
			hasBeenUsed=true;
		}else {
			hasBeenUsed=false;
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(chatfile, true));
			writer.write(sender + "#!#%#" + time + "#!#%#" + message + "#!!!#");
			writer.close();
		} catch (IOException e) {
			System.out.println("Error while writing to file" + e.getStackTrace());
		}
	}

	// Methode, die überprüft ob ein Nutzername in der Nutzerliste vorkommt
	public Boolean checkUsername(String username){

		String path = null;
		try {
			path = URLDecoder.decode(FileQuery.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
			if(server.getServerPort()==9999){
				path = path+"/Server1";
			}else{
				path = path+"/Server2";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		File userlist = new File(path +"/userlist.txt");

		String[] temp = null;

		try {
			BufferedReader userlistreader = new BufferedReader(new FileReader(userlist));
			String userlistline = userlistreader.readLine();
			if(userlistline != null) {
				temp = userlistline.split("#");
				for (int i = 0; i < temp.length; i = i+2) {
					if(username.equals(temp[i])) {
						//System.out.println("Username exists in userlist");
						return true;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Methode um einen Chat-Verlauf aus einer Chat-Datei auszulesen
	public String readChat(String[] participant) {
		String[] temp = null;
		String chatfiletext = null;

		File chatfile = getFilename(participant);

		try {
			BufferedReader chatfilereader = new BufferedReader(new FileReader(chatfile));
			chatfiletext = chatfilereader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return chatfiletext;
	}
	
	
	//Diese Methode gibt die Namen aller Chats zurück in der sich ein Nutzer befindet
	public String[] getAllChatsOfUser(String user) {
		
		String path = null;
		try {
			path = URLDecoder.decode(FileQuery.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
			if(server.getServerPort()==9999){
				path = path+"/Server1";
			}else{
				path = path+"/Server2";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		String[] temp = new String[listOfFiles.length];
		
		int z = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
		    if(listOfFiles[i].getName().contains("#"+user+"#")) {
		    	temp[z] = listOfFiles[i].getName();
		    	z++;
		    }
		  }
		}
		
		String[] Chat = new String[z];
		
		for (int i = 0; i < Chat.length; i++) {
			Chat[i] = temp[i].substring(0, temp[i].length() - 4);
		}

		return Chat;
	}

	// Methode um eine Liste erstellen in der alle registrierten Nutzer gespeichert werden
	public String createUserlist() {
		
		String path = null;
		try {
			path = URLDecoder.decode(FileQuery.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
			if(server.getServerPort()==9999){
				path = path+"/Server1";
				System.out.println("userliste wird hier erstellt: "+path);
			}else{
				path = path+"/Server2";
				System.out.println("userliste wird hier erstellt: "+path);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File directory = new File(path);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		File userlist = new File(path +"/userlist.txt");
		try {
			userlist.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("userlist succesfully created or found");
		return "userlist succesfully created or found";
	}


	// Mehtode, die prüft ob ein Nutzer mit dem mitgegebenen Nutzernamen und Passwort in der Nutzerliste existiert
	public int checkLoginCredentials(String username, String password) {
		int loginCredentialsCorrect = 0; // 2 Nutzername und Passwort sind korrekt, 1 Nutzername existiert aber Passwort ist nicht korrekt, 0 Nutzername existiert nicht
		
		String path = null;
		try {
			path = URLDecoder.decode(FileQuery.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
			if(server.getServerPort()==9999){
				path = path+"/Server1";
			}else{
				path = path+"/Server2";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		File userlist = new File(path +"/userlist.txt");

		String[] temp = null;
		
		try {
			BufferedReader userlistreader = new BufferedReader(new FileReader(userlist));
			String userlistline = userlistreader.readLine();
			if(userlistline != null) {
				temp = userlistline.split("#");
				for (int i = 0; i < temp.length; i = i+2) {
					if(username.equals(temp[i])) {
						System.out.println("Username exists");
						loginCredentialsCorrect++;
						if(password.equals(temp[i+1])){
							System.out.println("Password ist also correct");
							loginCredentialsCorrect++;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		


		return loginCredentialsCorrect;
	}

	// Methode, die einen Nutzer mit Nutzername und Passwort in die Nutzerliste einfügt
	public String addUserToUserlist(String username, String password) {

		String path = null;
		try {
			path = URLDecoder.decode(FileQuery.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
			if(server.getServerPort()==9999){
				path = path+"/Server1";
			}else{
				path = path+"/Server2";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(!hasBeenUsed) {
			server.redirectMsg("addUser " + username + " " + password);
			hasBeenUsed = true;
		}else {
			hasBeenUsed = false;
		}
		try {
			File userlist = new File(path +"/userlist.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(userlist, true));
			writer.write(username+"#"+password+"#"); // # is used as seperator
			writer.close();
		} catch (IOException e) {
			System.out.println("Error while writing to file" + e.getStackTrace());
		}
		
		System.out.println(
				"user succesfully added to userlist with username: " + username + " and password: " + password);
		return "user succesfully added to userlist with username: " + username + " and password: " + password;
	}

}
