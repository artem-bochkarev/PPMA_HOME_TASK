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
		public int counter;
		public int lettersCnt;
		Context( String name ) {
			this.name = name;
			counter = 1;
			entries = new HashMap<Character, Integer>();
			entries.put('#', 1);
			lettersCnt = 1;
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
			lettersCnt++;
		}
		@Override
		public int compareTo(Context o) {
			return name.compareTo(o.name);
		}
	}
	
	static String findContext( String msg, int curPos, int d ) {
		String context = "";
		
		for (int j=d; j>0; --j) {
			if ( j > msg.length() )
				continue;
			String toTry = msg.substring(msg.length() - j);
			int k = msg.indexOf(toTry);
			if ( k < msg.length() - j ) {
				return toTry;
			}
		}
		
		return context;
	}
	
	static class MyOutput {
		public String line, letter, context, tts, pts, ptas;
		public void out() {
			if ( pts.length() > 1 && pts.charAt(0)==',' ) {
				pts = pts.substring(1);
			}
			System.out.printf("%3s %3s %8s %16s %20s %8s \n", line, letter, context, tts, pts, ptas );
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
		Map<String, Context> contextMap = new HashMap<String, Context>();
		Context nullContext = new Context("#");
		nullContext.counter = -1;
		contextMap.put("", nullContext );
		
		for (int i=0; i<n; ++i) {
			MyOutput output = new MyOutput();
			output.line = Integer.toString(i+1);
			
			String context = findContext( msg.substring(0, i), i, D );
			contexts.add(context);
			
			Context tmp = new Context(context);
			if ( !contextMap.containsKey(context) ) {
				contextMap.put(context, tmp);
			}else {
				tmp = contextMap.get(context);
				tmp.counter++;
			}
			
			char curChar = msg.charAt(i);
			output.letter = Character.toString(curChar);
			output.context = tmp.name;
			
			int Ts = tmp.counter;
			output.tts = Integer.toString(Ts);
			output.pts = "";
			
			int Ptsa_ch = 0;
			int Ptsa_zn = 0;
			if ( tmp.entries.containsKey(curChar) ) {
				//ok
				
			}else {
				while ( !tmp.entries.containsKey(curChar) ) {
					if ( tmp.lettersCnt > 1 ) { 
						output.pts = output.pts.concat( ",1/" + Integer.toString(tmp.lettersCnt) );
					}else {
						output.pts = output.pts.concat( ",1" );
					}
					boolean b = (context.length() > 0);
					if ( context.length() > 1 )
						context = context.substring(1);
					else
						context = "";
					//esc
					tmp = contextMap.get(context);
					if ( tmp == null ) {
						tmp = new Context(context);
						contextMap.put(context, tmp);
						//System.err.println("No smaller context: \"" + context + '"');
					}
					
					
					if (b) {
						tmp.counter++;
					}

					if ( tmp.entries.containsKey(curChar) ) {
						//ok
						break;
					}else if ( context.equals("") ) {
						//first time
						Ptsa_ch = 1;
						if (b) {
							output.tts = output.tts.concat("," + Integer.toString(tmp.counter));
							if ( tmp.lettersCnt > 1 ) { 
								output.pts = output.pts.concat( ",1/" + Integer.toString(tmp.lettersCnt) );
							}else {
								output.pts = output.pts.concat( ",1" );
							}
						}
						break;
					}
				}
			}
			if ( Ptsa_ch == 0 ) {
				Ptsa_ch = tmp.entries.get(curChar);
				Ptsa_zn = tmp.entries.size();
			}
			tmp.incCounter(curChar);
			output.out();
		}
	}

}
