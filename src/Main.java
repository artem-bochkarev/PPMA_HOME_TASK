import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Main {

	/**
	 * @param args
	 */
	
	class Entries implements Comparable<Entries>{
		public char letter;
		public int counter;
		
		Entries( char letter ) {
			this.letter = letter;
			counter = 1;
		}
		@Override
		public int compareTo(Entries o) {
			return Character.compare(letter, o.letter);
		}
	}
	
	class Context {
		public String name;
		public Set<Entries> entries;
		Context( String name ) {
			this.name = name;
			entries = new HashSet<Entries>();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String alphabet = args[0];
		int D = Integer.parseInt(args[1]);
		String msg = args[2];
		System.out.println( alphabet );
		System.out.println( D );
		
		int n = msg.length();
		ArrayList<String> contexts = new ArrayList<String> (n);
		
		
		for (int i=0; i<n; ++i) {
			
		}
	}

}
