package SimonC_MainPackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileMonster {
	
	public static void initializeAudio(String fileName, List<String> audioPaks){
		File f = new File(fileName);
		Scanner in;
		try {
			in = new Scanner(f);
			while(in.hasNextLine()){
				audioPaks.add(in.nextLine());
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
	}

}
