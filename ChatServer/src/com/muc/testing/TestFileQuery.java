package com.muc.testing;

import com.muc.FileQuery;

public class TestFileQuery {
	
	public static void main(String[] args) {
		
		FileQuery TestFileQuery = new FileQuery();
		
//		TestFileQuery.createUserlist();
//		TestFileQuery.addUserToUserlist("Luca", "secret");
//		TestFileQuery.addUserToUserlist("Ruben", "passwort");
//		TestFileQuery.addUserToUserlist("Daniel", "67123781263");
		
		System.out.println(TestFileQuery.checkLoginCredentials("Daniel","67123781263"));
		
		String[] participant = {"Luca", "Ruben", "Lucas", "Daniel", "Nina", "Rosa", "Leon", "Raphael", "masterxx34", "1superturtle", "126762731", "gang$ter"};
		
//		TestFileQuery.createChatFile(participant);
//		TestFileQuery.writeChatMessage(participant, "Luca", "Hallo Daniel!");
//		TestFileQuery.writeChatMessage(participant, "Luca", "Wie geht es dir?");
//		TestFileQuery.writeChatMessage(participant, "Daniel", "Hallo Luca, mir gehts gut :)");
//		TestFileQuery.writeChatMessage(participant, "Luca", "Das freut mich.");
		String[][] Chat = TestFileQuery.readChat(participant);
		for(int i = 0; i < Chat.length; i++) {
			System.out.print(Chat[i][0] + " sagt: "); 
			System.out.println(Chat[i][1]);
		}
		
	}

}
