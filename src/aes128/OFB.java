package aes128;

import java.util.ArrayList;
import java.util.List;


public class OFB {
	Cipher cipher ;
	
	
	public OFB() {
		cipher = new Cipher();
	}
	
	public void testOFB(String plaintext, String inputkey, String IV) {
		String encryption = encryptstringtobytes(plaintext, inputkey, IV);
		System.out.println(encryption);
		String decryption = decryptparseString(encryption, inputkey, IV);
		System.out.println(decryption);
	}
	
	public String encryptstringtobytes(String plaintext, String inputkey, String IV) {
		StringBuilder stringBuilder = new StringBuilder();
		String sb;
		int start = 0;
		int end = 2;
		byte[] key = new byte[cipher.Nk*4];
		if(inputkey.length() != 32) {
			System.out.println("please input correct key!");
			return "";
		}
		//iterate over key two chars a time, add their hex value to matrix
		for (int i = 0; i < key.length; i++) {
				sb = inputkey.substring(start, end);
				key[i] = (byte)(Integer.parseInt(sb.toString(), 16));
				start += 2;
				end += 2;
		}
		
		cipher.Keyexpansion(key);
		
		if(IV.length() != 32) {
			System.out.println("please input correct IV!");
			return "";
		}
		start = 0;
		end = 2;
		byte[][] ivbytes = new byte[cipher.Nb][cipher.Nb];
		//iterate over line two chars at a time, add their hex value to matrix
		for (int i = 0; i < ivbytes.length; i++) {
			for (int j = 0; j < ivbytes[0].length; j++) {
				sb = IV.substring(start, end);
				ivbytes[j][i] = (byte)(Integer.parseInt(sb.toString(), 16));
				start += 2;
				end += 2;
			}
		}
		
		List<byte[][]> states = new ArrayList<byte[][]>();
		List<byte[][]> outputs = new ArrayList<byte[][]>();
		
		byte[] plaintextbytes = plaintext.getBytes();
		byte[][] state = new byte[cipher.Nb][cipher.Nb];
		
		for (int i = 0; i < plaintextbytes.length; i++) {
			if(i%16 == 0 && i != 0) {
				states.add(state);
				state = new byte[cipher.Nb][cipher.Nb];
				initializestate(state);
			}
			state[i%4][(i/4)%4] = plaintextbytes[i];	
		}
		
		byte[][] Input = ivbytes;
		
		
		for (int i = 0 ; i < states.size(); i++) {
			byte[][] ptext = states.get(i);
			byte[][] afterencryption = cipher.encryptstate(Input);
			outputs.add(xorbytes(afterencryption, ptext));
			Input = afterencryption;
		}
		
		for (int i = 0; i < states.size(); i++) {
			stringBuilder.append(statetostring(states.get(i)));
		}
		
		return stringBuilder.toString(); 
	}

	public String decryptparseString(String encryption, String inputkey, String IV) {
		StringBuilder stringBuilder = new StringBuilder();
		String sb;
		int start = 0;
		int end = 2;
		byte[] key = new byte[cipher.Nk*4];
		if(inputkey.length() != 32) {
			System.out.println("please input correct key!");
			return "";
		}
		//iterate over key two chars a time, add their hex value to matrix
		for (int i = 0; i < key.length; i++) {
				sb = inputkey.substring(start, end);
				key[i] = (byte)(Integer.parseInt(sb.toString(), 16));
				start += 2;
				end += 2;
		}
		
		cipher.Keyexpansion(key);
		
		byte[] iv = new byte[cipher.Nb*4];
		if(IV.length() != 32) {
			System.out.println("please input correct IV!");
			return "";
		}
		start = 0;
		end = 2;
		byte[][] ivbytes = new byte[cipher.Nb][cipher.Nb];
		//iterate over line two chars at a time, add their hex value to matrix
		for (int i = 0; i < ivbytes.length; i++) {
			for (int j = 0; j < ivbytes[0].length; j++) {
				sb = IV.substring(start, end);
				ivbytes[j][i] = (byte)(Integer.parseInt(sb.toString(), 16));
				start += 2;
				end += 2;
			}
		}
		
		List<byte[][]> states = new ArrayList<byte[][]>();
		List<byte[][]> outputs = new ArrayList<byte[][]>();
		
		byte[][] state = new byte[cipher.Nb][cipher.Nb];
		
		List<String> encryptions = new ArrayList<String>();
		for(int i = 0 ; i + 1 < encryption.length() ; ) {
			encryptions.add(encryption.substring(i*32,(i+1)*32));
		}
		for (int i = 0; i < encryptions.size(); i++) {
			start = 0;
			end = 2;
			String oneline = encryptions.get(i);
			for (int h = 0; h < state.length; h++) {
				for (int j = 0; j < state[0].length; j++) {
					sb = oneline.substring(start, end);
					state[j][h] = (byte)(Integer.parseInt(sb.toString(), 16));
					start += 2;
					end += 2;
				}
			}
			states.add(state);
			state = new byte[cipher.Nb][cipher.Nb];
			initializestate(state);
		}
		
		
		byte[][] Input = ivbytes;
		
		
		for (int i = 0 ; i < states.size(); i++) {
			byte[][] ptext = states.get(i);
			byte[][] afterencryption = cipher.encryptstate(Input);
			outputs.add(xorbytes(afterencryption, ptext));
			Input = afterencryption;
		}
		
		byte[] stringbytes = statetobyte(states.get(0));
		for (int i = 1; i < states.size(); i++) {
			stringbytes = combinebytegroup(stringbytes, statetobyte(states.get(i)));
		}
		
		return new String(stringbytes); 
	}
	
	private void initializestate(byte[][] state) {
		for (byte[] fourelement : state) {
			for(byte element : fourelement ) {
				element = (byte)(0x32);
			}
		}
	}
	
	private byte[][] xorbytes(byte[][] A, byte[][] B) {
		byte[][] result = new byte[A.length][A[0].length];
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				result[i][j] = (byte)(A[i][j]^B[i][j]);
			}
		}
		return result;
	}
	
	private String statetostring(byte[][] state) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				stringBuilder.append(String.format("%02X", state[j][i]));
			}
		}
		String string = stringBuilder.toString();
		return string;
	}
	
	private byte[] statetobyte(byte[][] state){
		byte[] result = new byte[state.length*state[0].length];
		for (int i = 0; i < result.length; i++) {
			result[i] = state[(i/4)%4][i%4];
		}
		return result;
	}
	
	private byte[] combinebytegroup(byte[] A, byte[] B) {
		byte[] result = new byte[A.length+B.length];
		for (int i = 0; i < A.length; i++) {
			result[i] = A[i];
		}
		for (int i = A.length; i < A.length+B.length; i++) {
			result[i] = B[i];
		}
		return result;
	}
	
}
