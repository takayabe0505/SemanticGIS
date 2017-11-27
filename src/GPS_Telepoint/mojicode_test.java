package GPS_Telepoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class mojicode_test {

	public static void main(String[] args) throws NumberFormatException, IOException {
		File in = new File("C:/Users/yabec/Desktop/SemanticGIS/TELEPOINT_data/P1B72/01/01101.csv");
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		int count = 0;
		while((line=br.readLine())!=null) {
			count++;
			String tokens[] = line.split(",");
			String name = tokens[0];
			System.out.println(name);
			if(count==10) {
				break;
			}
		}
		br.close();
	}

}
