import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import model.user.encryptionAES;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import JsonClasses.*;

public class TCPClient {
	public static void main(String[] args) throws Exception {
		String modifiedSentence;
		Gson gson = new GsonBuilder().create();
		CreateCalendar CC = new CreateCalendar();
		encryptionAES aes = new encryptionAES();
		AuthUser authUser = new AuthUser();
		Scanner userInput = new Scanner(System.in);
		Scanner userInput2 = new Scanner(System.in);
		System.out.println("Indtast dit brugernavn :");
		String brugernavn = userInput.nextLine();
		authUser.setAuthUserEmail(brugernavn);
		System.out.println("Indtast dit password : ");
//		String password = aes.encrypt(userInput.nextLine());
		String password = userInput2.nextLine();
		authUser.setAuthUserPassword(password);
		if(brugernavn.equalsIgnoreCase("mads@cbs.dk")){
			authUser.setAuthUserIsAdmin(true);
		}
		String gsonString = gson.toJson(authUser);
		System.out.println(authUser);
		System.out.println(gsonString);
		

		Socket clientSocket = new Socket("localhost", 8888);
		DataOutputStream outToServer = new DataOutputStream(
				clientSocket.getOutputStream());
		byte[] input = gsonString.getBytes();
		byte key = (byte) 3.1470;
		byte[] encrypted = input;
		for (int i = 0; i < encrypted.length; i++)
			encrypted[i] = (byte) (encrypted[i] ^ key);

		System.out.println(encrypted);
		outToServer.write(encrypted);
		outToServer.flush();
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		modifiedSentence = inFromServer.readLine();
		System.out.println(modifiedSentence);
		System.out.println("FROM SERVER: " + modifiedSentence);
		clientSocket.close();
	}
}