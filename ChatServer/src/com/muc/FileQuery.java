package com.muc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileQuery {

//	important notes:
//	participants[0] is reserved for the sender of the message
//	participants[1] to participants[n] are the users reciving the messages
//	returnStringArray[0] is reserved for the username of the sender of the message in returnStringArray[1]

	public File getFilename(String[] participants) {
		String path = "./";

		for (int i = 0; i < participants.length; i++) {
			path = path + participants[i] + "#"; // # is used as seperator
		}

		File chatfile = new File(path + ".txt");
		return chatfile;
	}

	// Method for creating a file for groupchats
	public void createGroupChatFile(String[] participants) {

		File chatfile = getFilename(participants);

		try {
			if (chatfile.createNewFile()) {
				System.out.println("groupchatfile created succesfully");
			} else {
				System.out.println("groupchatfile could not be created");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Method for writing a croupchatmessage to file
	public void writeGroupChatMessage(String[] participants, String message) {

		File chatfile = getFilename(participants);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(chatfile, true));
			writer.write(participants[0] + "~" + message + "#"); // # is used as seperator for messages and ~ is used as
																	// seprator between sender and message
			writer.close();
		} catch (IOException e) {
			System.out.println("Error while writing to file" + e.getStackTrace());
		}

	}

	// Method for reading a croupchat from file
	public String[] readGroupChat(String[] participants) {
		String[] returnStringArray = null;
		String chatfiletext = null;

		File chatfile = getFilename(participants);

		try {
			BufferedReader chatfilereader = new BufferedReader(new FileReader(chatfile));
			chatfiletext = chatfilereader.readLine();
			returnStringArray = chatfiletext.split("~");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return returnStringArray; // returnStringArray[n] is reserved for the username of the sender of the
									// message in returnStringArray[n+1]
	}

	// Method for creating the userlist (should only be executed once)
	public String createUserlist() {
		
		File userlist = new File("./userlist.txt");
		try {
			userlist.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("userlist succesfully created");
		return "userlist succesfully created";
	}

	// Method for searching for matching userdata in the userlist
	public String[] searchForUserInUserlist(String username) {
		String[] userData = { "username", "password" };

		return userData;
	}

	// Method for adding a user to the userlist
	public String addUserToUserlist(String username, String password) {

		try {
			File userlist = new File("./userlist.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(userlist, true));
			writer.write(username+"~"+password+"#"); // # is used as seperator for entriys, ~ is used as seperator between username and password
			writer.close();
		} catch (IOException e) {
			System.out.println("Error while writing to file" + e.getStackTrace());
		}
		
		System.out.println(
				"user succesfully added to userlist with username: " + username + " and password: " + password);
		return "user succesfully added to userlist with username: " + username + " and password: " + password;
	}

}
