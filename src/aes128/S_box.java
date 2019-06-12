package aes128;

public class S_box {
	
	public int[] c = {1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0} ;
	public int[] irreducible = {1,1,0,1,1,0,0,0,1,0,0,0,0,0,0,0};
	private int[] quotient = new int[16];
	private int[] final_quotient = new int[16];
	private int[] remainder = new int[16];
	private int[][] s_box = new int[16][16];
	
	private int get_bit(byte b,int i) {
	    int bit = (int)((b>>i) & 0x1);
	    return bit;
	}
	
	private int[] byte_to_intarry(byte data) {
		int[] bits = new int[16];
		for (int i = 0; i < 8; i++) {
			bits[i] = get_bit(data, i);
		}
		return bits;
	}
	
	private int get_exponent(int[] num) {
		int exponent = 0;
		for (int i = 1; i <= num.length; i++) {
			if(num[i-1] == 1)
				exponent = i;
		}
		return exponent;
	}
	
	private void calc_quotient(int diff_exponent) {
		quotient[diff_exponent] = 1;
	}
	
	private void division(int[] A, int[] B){
		int a_exponent = get_exponent(A);
		int b_exponent = get_exponent(B);
		int[] one_quotient = new int[16];
		int diff_exponent = 0;
		
		if(a_exponent >= b_exponent) {
			diff_exponent = a_exponent - b_exponent;
			for (int i = 0; i < B.length; i++) {
				if(i+diff_exponent < B.length)
					one_quotient[i+diff_exponent] = B[i];
				if(i < diff_exponent)
					one_quotient[i] = 0;
			}
			
			for (int i = 0; i < A.length; i++) {
				A[i] = A[i]^one_quotient[i];
			}
			
			calc_quotient(diff_exponent);
			division(A, B);
		}
		else {
			final_quotient = quotient;
			remainder = A;
			quotient = new int[16];
		}
	}
	
	private int[] multiplication(int[] A, int[] B) {
		int[] product = new int[16];
		int[] modeled_product = new int[16];
		for(int i = 0 ; i < 8 ; i++) {
			if(A[i] == 1) {
				for (int j = 0; j < 8; j++) {
					product[i+j] = product[i+j]^B[j];
				}
			}
		}
		modeled_product = product;
		if(get_exponent(product) > 8 ) {
			division(product, irreducible.clone());
			modeled_product = remainder;
		}
		return modeled_product;
	}
	
	private int[] subtract(int[] A, int[] B) {
		int[] result = new int[A.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = A[i]^B[i];
		}
		return result;
	}
	
	// m(x) = x^8 + x^4 + x^3 + x + 1   {01}{1b}  0000 0001 0001 1011  irreducible polynomial
	// gcd(bits(x),m(x) = 1 using extended Euclidean algorithm
	private int[] get_inverse(int[] bits) {
		int[] inverse = new int[16];
		int[][] r = new int[3][16];
		int[] q = new int[16];
		int[][] v = new int[3][16];
		int[][] w = new int[3][16];
		
		int[] terminator = new int[16];
		
		r[0] = irreducible.clone();
		r[1] = bits;
		
		v[0][0] = 1;
		w[1][0] = 1;
		
		division(r[0], r[1]);
		q = final_quotient;
		r[2] = remainder;
		while(!equals(remainder, terminator)) {
			v[2] = subtract(v[0], multiplication(q, v[1]));
			w[2] = subtract(w[0], multiplication(q, w[1]));
			v[0] = v[1];
			v[1] = v[2];
			w[0] = w[1];
			w[1] = w[2];
			r[0] = r[1];
			r[1] = r[2];
			division(r[0], r[1]);
			q = final_quotient;
			r[2] = remainder;
		}
		inverse = w[1];
		
		return inverse;
	}
	
	private boolean equals(int[] A, int[] B) {
		for (int i = 0; i < B.length; i++) {
			if(A[i] != B[i])
				return false;
		}
		return true;
	}
	
	private int convert(int[] bits) {
		int[] converted = new int[16];
		int result = 0;
		int exp = 1;
		for(int i = 0 ; i < 8 ; i++) {
			converted[i] = bits[i]^bits[(i+4)%8]^bits[(i+5)%8]^bits[(i+6)%8]^bits[(i+7)%8]^c[i];
			result = result + converted[i]*exp;
			exp = exp * 2;
		}
		return result;
	}
	
	public int[][] generate() {
		int i = 0;
		int j = 0;
		System.out.println("begin to generate s box");
		for(; i < 16 ; i++) {
			for(j = 0; j < 16 ; j++) {
				int[] bits = new int[16];
				if(i == 0 && j == 0) {
					
				}
				else {
					byte data = (byte) ((i<<4) + j);
					bits = get_inverse(byte_to_intarry(data));
				}
				s_box[i][j] = convert(bits);
				String string = String.format("%02x", s_box[i][j]);
				System.out.print(string+" ");
			}
			System.out.println();
		}
		System.out.println("s box has been generated");
		return s_box;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		S_box s_box = new S_box();
		s_box.generate();
		
	}
	
	
	
}
