package aes128;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		System.out.println("please input keys:");
		String keyString = scanner.nextLine();
		System.out.println("please input plaintext:");
		String plaintextString = scanner.nextLine();
		System.out.println("The encrypted text:");
		Cipher cipher = new Cipher();
		cipher.encryptparsestring(plaintextString, keyString);
	}

}
