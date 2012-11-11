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
			if ( tts.length() > 1 && tts.charAt(0)==',' ) {
				tts = tts.substring(1);
			}
			System.out.printf("%3s %3s %8s %16s %20s %8s \n", line, letter, context, tts, pts, ptas );
		}
	}
	
	static void printPTS( MyOutput to, int Ts ) {
		if ( Ts > 0 ) { 
			to.pts = to.pts.concat( ",1/" + Integer.toString(Ts+1) );
		}else {
			to.pts = to.pts.concat( ",1" );
		}
	}
	
	static int exclusion( Context currentContext, Set<Character> cantSet, Map<String, Context> contextMap, String msg ) {
		int Ts = findTsC(currentContext, msg);
		for (char c : cantSet) {
			if (c != '#') {
				if (!currentContext.name.equals("#")) {
					Ts -= findTs( currentContext.name + c, msg);
				}else {
					Ts -= findTs( Character.toString(c), msg);
				}
			}
		}
		return Ts;
	}
	
	static int findTs( String s, String msg ) {
		String m_msg = msg;
		int k = m_msg.indexOf(s);
		int i = 0;
		while (k>-1) {
			++i;
			m_msg = m_msg.substring(k+1);
			k = m_msg.indexOf(s);
		}
		return i;
	}
	
	static int findTsC( Context context, String msg ) {
		String s = context.name;
		if (s.equals("#")) {
			return context.counter;
		}
		String nMsg = msg.substring(0, msg.length() - s.length());
		return findTs(s, nMsg);
	}
	
	static void fillCantSet( Set<Character> set, Context context, String msg ) {
		String m_msg = msg;
		int k = m_msg.indexOf(context.name);
		while (k>-1) {
			int t = k + context.name.length();
			if (m_msg.length() <= t)
				break;
			set.add(m_msg.charAt(t));
			m_msg = m_msg.substring(k+1);
			k = m_msg.indexOf(context.name);
			
		}
	}
	
	static void fillNewContext( Context context, String msg) {
		Set<Character> set = new HashSet<Character>();
		fillCantSet(set, context, msg);
		for (char c : set) {
			context.incCounter(c);
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
		nullContext.counter = 0;
		nullContext.lettersCnt = 0;
		contextMap.put("", nullContext );
		int chCnt = 256;
		
		for (int i=0; i<n; ++i) {
			MyOutput output = new MyOutput();
			output.line = Integer.toString(i+1);
			String currentMsg = msg.substring(0, i);
			
			String context = findContext( msg.substring(0, i), i, D );
			contexts.add(context);
			
			Context tmp = new Context(context);
			if ( !contextMap.containsKey(context) ) {
				contextMap.put(context, tmp);
			}else {
				tmp = contextMap.get(context);
			}
			
			fillNewContext(tmp, currentMsg);
			char curChar = msg.charAt(i);
			output.letter = Character.toString(curChar);
			output.context = tmp.name;
			
			output.tts = "";
			output.pts = "";
			output.ptas = "";
			int Ts = 0;
			
			if ( tmp.name.equals("#") ) {
				tmp.counter = i;
			}

			if ( tmp.entries.containsKey(curChar) ) {
				//ok
				
				Ts = findTsC( tmp, currentMsg );
				output.tts = Integer.toString(tmp.counter);
				int Tsa = findTs( context + curChar, currentMsg);
				output.ptas = Integer.toString(Tsa)+"/"+Integer.toString(Ts+1);
				tmp.counter++;
			}else {
				Set<Character> cantBe = new HashSet<Character>();
				while ( !tmp.entries.containsKey(curChar) ) {
					if ( context.equals("") ) {
						//esc + first time
						tmp.counter = i;
						int Ts_s = exclusion(tmp, cantBe, contextMap, currentMsg);
						printPTS(output, Ts_s);
						output.ptas = "1/" + Integer.toString(chCnt--);
						break;
					}else {
						//esc + smaller context
						Ts = findTsC( tmp, currentMsg );
						output.tts = output.tts.concat("," + Integer.toString(Ts));
						tmp.counter++;
						int Ts_s = exclusion(tmp, cantBe, contextMap, currentMsg);
						fillCantSet(cantBe, tmp, currentMsg);
						printPTS(output, Ts_s);
						if ( context.length() > 1 )
							context = context.substring(1);
						else
							context = "";
						tmp = contextMap.get(context);
						if ( tmp == null ) {
							tmp = new Context(context);
							contextMap.put(context, tmp);
							fillNewContext(tmp, currentMsg);
							//System.err.println("No smaller context: \"" + context + '"');
						}				
					}
				}
				if ( tmp.name.equals("#") ) {
					tmp.counter = i;
				}
				Ts = findTsC(tmp, currentMsg);
				output.tts = output.tts.concat("," + Integer.toString(tmp.counter));
				if ( output.ptas.equals("") ) {
					//fillCantSet(cantBe, tmp, currentMsg);
					int Ts_s = exclusion(tmp, cantBe, contextMap, currentMsg);
					int Tsa = findTs( context + curChar, currentMsg);
					output.ptas = Integer.toString(Tsa)+"/"+Integer.toString(Ts_s+1);
				}
				if ( Ts != tmp.counter ) {
					//check!
					System.err.println(i+1);
					System.err.println(tmp.counter + " != " + Ts);
				}
				tmp.counter++;
				
			}
			tmp.incCounter(curChar);
			output.out();
		}
	}

}
