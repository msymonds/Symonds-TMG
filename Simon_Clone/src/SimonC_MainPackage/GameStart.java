/**
 *  Michael Symonds
 *  The Game of Symonds
 */

package SimonC_MainPackage;

public class GameStart {
	private static final int EASY_MAX = 10;
	private static int sequenceCounter = 0;
	private static int checkCounter = 0;
	int [] sequence;

	
	public GameStart(){
		sequence = new int[EASY_MAX];
		createSequence();
	}
	
	public GameStart(int n){
		sequence = new int[n];
		createSequence();
	}
	
	private void createSequence(){
		for(int i = 0; i < sequence.length; i++){
			sequence[i] = ((int)(Math.random() * 4 ));
		}
	}
	
	public int getCheckCounter(){
		return checkCounter;
	}
	
	public int getSequenceCounter(){
		return sequenceCounter;
	}
	
	public void incrementCheckCounter(){
		checkCounter++;
	}
	
	public void incrementSequenceCounter(){
		sequenceCounter++;
	}
	
	public void resetCheckCounter(){
		checkCounter = 0;
	}
	
	public void resetSequenceCounter(){
		sequenceCounter = 0;
	}
	
	public String toString(){
		String s = "";
		for (int i = 0; i < sequence.length; i++){
			s += "index: " + i + "  value: " + sequence[i] + "\n";
		}
		s += "\nSequenceCounter: " + sequenceCounter;
		s += "\nCheckCounter: " + checkCounter;
		return s;
	}
} // GameStart
