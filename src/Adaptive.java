import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Adaptive {

	/**
	 * @param args
	 */
	
	static class MyOutput {
		public String line, letter, binary;
		public void out() {
			if ( binary.length() > 1 && binary.charAt(0)=='+' ) {
				binary = binary.substring(1);
			}
			System.out.printf("%s\t%s\t%s\n", line, letter, binary );
		}
	}
	
	static class ArithmeticCode {
		private static final long topValue = (((long) 1 << 16) - 1);
		private static final long firstQtr = (topValue/4+1);
		private static final long Half = 2*firstQtr;
		private static final long thirdQtr = 3*firstQtr;
		private long high, low;
		private int bitsToFollow;
		private StringBuilder currentStr;
		private StringBuilder str;
		
		public ArithmeticCode() {
			low = 0;
			high = topValue;
			bitsToFollow = 0;
			str = new StringBuilder();
		}
		
		public void bitsPlusFollow(char c) {
			currentStr.append(c);
			for (int i=bitsToFollow; i>0; i--) {
				if (c == '1')
					currentStr.append('0');
				else
					currentStr.append('1');
			}
			bitsToFollow = 0;
		}
		
		public void addIncl(String a) {
			str.append(a);
		}
		
		public String encodeSymbol(int symbol, long cum_freq[] ) {
			if ( cum_freq.length == 1 )
				return "";
			currentStr = new StringBuilder();
			long range;
			range = (long) (high - low) + 1;
			high = low + (range * cum_freq[symbol - 1]) / cum_freq[0] - 1;
			low  = low + (range * cum_freq[symbol]) / cum_freq[0];
			for (;;) { 
				if (high < Half) { 
					bitsPlusFollow('0');
				}
				else if (low >= Half) {
					bitsPlusFollow('1');
					low -= Half;
					high -= Half;
				} else if (low >= firstQtr
						&& high < thirdQtr) {
					bitsToFollow += 1;
					low -= firstQtr;
					high -= firstQtr;
				}
				else
					break;
				low = 2 * low;
				high = 2 * high + 1;
			}
			str.append(currentStr);
			return currentStr.toString();
		}
	}
	
	static class Statistic {
		private Map<Character, Integer> map;
		Statistic() {
			map = new HashMap<Character, Integer>();
			map.put('#', 1);
		}
		
		public long[] cumFreq() {
			//pos = new HashMap<Character, Integer>();
			long[] arr = new long[map.size()+1];
			int k = map.size();
			arr[k] = 0;
			for (char c:map.keySet()) {
				arr[k-1] = arr[k]+map.get(c);
				//pos.put(c, k);
				--k;
			}
			return arr;
		}
		
		public int getCharPos(char c) {
			if ( !map.containsKey(c) )
				return -1;
			int k = 1;
			for (char d:map.keySet()) {
				if ( d == c ) {
					return k;
				}
				++k;
			}
			return -1;
		}
		
		public void add(char c) {
			if ( map.containsKey(c) ) {
				map.put(c, map.get(c) + 1);
			}else {
				map.put(c, 1);
			}
		}
	}
	
	public static String charToBinary(char c) {
		StringBuilder binary = new StringBuilder();
		//if ()
		//if ( c != ' ' && c != '.' && c != '_' ) {
			c <<= 8;
			for (int i = 0; i < 8; i++) {
				binary.append((c & 32768) == 0 ? 0 : 1);
				c <<= 1;
			}
		/*}else {
			for (int i = 0; i < 8; i++) {
				binary.append((c & 32768) == 0 ? 0 : 1);
				c <<= 1;
			}
		}*/
		binary.append(' ');
		return binary.toString();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String alphabet = args[0];
		int D = Integer.parseInt(args[0]);
		//System.out.println( alphabet );
		System.out.println( D );
		String msg = args[1];
		int ii = 2;
		while ( ii < args.length )
		{
			msg += " " + args[ii];
			++ii;
		}
		
		int n = msg.length();
		ArithmeticCode arithm = new ArithmeticCode();
		Statistic st = new Statistic();
		
		for (int i=0; i<n; ++i) {
			MyOutput output = new MyOutput();
			output.line = Integer.toString(i+1);
			output.letter = "";
			
			char curChar = msg.charAt(i);
			int pos = st.getCharPos(curChar);
			if ( pos != -1 ) {
				output.binary = arithm.encodeSymbol(pos, st.cumFreq());
			}else {
				pos = st.getCharPos('#');
				output.binary = arithm.encodeSymbol(pos, st.cumFreq());
				arithm.addIncl(charToBinary(curChar));
				output.binary += "+" + charToBinary(curChar);
				output.letter = "#";
			}
			
			st.add(curChar);
			output.letter += Character.toString(curChar);

			output.out();
		}
		
		System.out.println("Source size = " + msg.length() + " bytes");
		int arcSize = (arithm.str.toString().length() + 7)/8;
		System.out.println("Archived size = " + arcSize + " bytes");
	}

}
