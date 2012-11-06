import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Main {

	/**
	 * @param args
	 */
	
	static class Context implements Comparable<Context>{
		public String name;
		public Map<Character, Integer> entries;
		Context( String name ) {
			this.name = name;
			entries = new HashMap<Character, Integer>();
		}
		public int getCounter( char c ) {
			if ( entries.containsKey(c) ) {
				return entries.get(c);
			}
			return 0;
		}
		public void incCounter( char c ) {
			if ( entries.containsKey(c) ) {
				entries.put(c, entries.get(c) + 1);
			}else {
				entries.put(c, 1);
			}
		}
		@Override
		public int compareTo(Context o) {
			return name.compareTo(o.name);
		}
	}
	
	static String findContext( String msg, int curPos, int d ) {
		String context = "";
		
		for (int j=d; j>0; --j) {
			String toTry = msg.substring(msg.length() - j);
			int k = msg.indexOf(toTry);
			if ( k < msg.length() - j ) {
				return toTry;
			}
		}
		
		return context;
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
		Map<String, Context> contextMap = new HashMap<String, Context>();
		
		for (int i=0; i<n; ++i) {
			String context = findContext( msg.substring(0, i+1), i, D );
			contexts.add(context);
			Context tmp = new Context(context);
			if ( !contextMap.containsKey(context) ) {
				contextMap.put(context, tmp);
			}else {
				tmp = contextMap.get(context);
			}
			
			char curChar = msg.charAt(i);
			if ( tmp.entries.contains(o) )
		}
	}

}
