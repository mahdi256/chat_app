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

//	important notes:
//	returnStringArray[0] is reserved for the username of the sender of the message in returnStringArray[1]

	// Method for getting filename of a chat-file by providing all involved users in a String-Array
	private static File getFilename(String[] participant) {
		String path = null;
		try {
			path = URLDecoder.decode(FileQuery.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//sort participant[] lexicographically 
		
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
		
		// build path
		for (int i = 0; i < participant.length; i++) {
			path = path + participant[i] + "#"; // # is used as seperator
		}

		File chatfile = new File(path + ".txt");
		return chatfile;
	}
	
	
	// Method to check if a chatfile exists
	public Boolean findChatFile(String[] participant) {
		File chatfile = getFilename(participant);
		Boolean exits = chatfile.exists();
		return exits;
	}
	

	// Method for creating a file that contains a chat
	public static void createChatFile(String[] participant) {

		File chatfile = getFilename(participant);

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

	// Method for writing a chatmessage to file
	public static void writeChatMessage(String[] participant,String sender, String message) {

		File chatfile = getFilename(participant);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(chatfile, true));
			writer.write(sender + "#" + message + "#");
			writer.close();
		} catch (IOException e) {
			System.out.println("Error while writing to file" + e.getStackTrace());
		}

	}

	// Method for reading a chat from file
	public static String[][] readChat(String[] participant) {
		String[] temp = null;
		String chatfiletext = null;

		File chatfile = getFilename(participant);

		try {
			BufferedReader chatfilereader = new BufferedReader(new FileReader(chatfile));
			chatfiletext = chatfilereader.readLine();
			temp = chatfiletext.split("#");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//combine Messages and Senders into returnStringArray[][]
		String[][] returnStringArray = new String[temp.length / 2][2];
		
		int x = 0;
		for(int i = 0; i < temp.length/2; i++){
			returnStringArray[i][0] = temp[x];
			x++;
			returnStringArray[i][1] = temp[x];
			x++;
		}

		return returnStringArray;
	}

	// Method for creating the userlist (should only be executed once)
	public String createUserlist() {
		
		String path = null;
		try {
			path = URLDecoder.decode(FileQuery.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		File userlist = new File(path +"/userlist.txt");
		try {
			userlist.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("userlist succesfully created");
		return "userlist succesfully created";
	}

	// Method for checking Login credentials
	public static int checkLoginCredentials(String username, String password) {
		int loginCredentialsCorrect = 0; // 2 login credetials are correct, 1 user exists but password is incorrect, 0 user does not exist
		
		String path = null;
		try {
			path = URLDecoder.decode(FileQuery.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		File userlist = new File(path +"/userlist.txt");

		String[] temp = null;
		
		try {
			BufferedReader userlistreader = new BufferedReader(new FileReader(userlist));
			String userlistline = userlistreader.readLine();
			temp = userlistline.split("#");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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

		return loginCredentialsCorrect;
	}

	// Method for adding a user to the userlist
	public String addUserToUserlist(String username, String password) {

		String path = null;
		try {
			path = URLDecoder.decode(FileQuery.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
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
