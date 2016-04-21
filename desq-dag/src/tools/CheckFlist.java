package tools;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CheckFlist {

	public static void main(String[] args) throws NumberFormatException, IOException {
		String input = "/home/kbeedkar/Data/amzn-encoded/";
		
		Map<Integer, Integer> flist = new HashMap<Integer, Integer>();
		
		String dictPath = input.concat("/wc/part-r-00000");
		//Load old dictionary
		FileInputStream fstream1 = new FileInputStream(dictPath);
		DataInputStream in1 = new DataInputStream(fstream1);
		BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));

		String line1;
		while ((line1 = br1.readLine()) != null) {
			String[] tokens = line1.split("\t");
			int itemId = Integer.parseInt(tokens[3].trim());
			int support = Integer.parseInt(tokens[2].trim());
			flist.put(itemId, support);
		}
		br1.close();
		
		Map<Integer, Integer> flist2 = new HashMap<Integer, Integer>();
	
		String inputPath = input.concat("/raw/part-r-00000");
		// Read old sequences
		FileInputStream fstream = new FileInputStream(inputPath);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;
		while ((line = br.readLine()) != null) {
			if (!line.isEmpty()) {
			Set<Integer> items = new HashSet<Integer>();
			
			String[] tokens = line.split("\\s* \\s*");
			if(tokens.length > 1) {
			for(String token : tokens) {
				int itemId = Integer.parseInt(token);
				items.add(itemId);
				
			}
			}
			for (int item : items) {
				if(flist2.containsKey(item)) {
					flist2.put(item, flist.get(item)+ 1);
				}
				else{
					flist2.put(item, 1);
				}
			}
		}
		}
		br.close();

		
		for(int item : flist2.keySet()) {
			int value1 = flist.get(item);
			int value2 = flist2.get(item);
			
			if(value1 > value2){
				System.err.println("values not equal for item : " + item);
			}
			else if (value1 < value2) {
				System.err.println("values not equal for item : " + item);
			}
		}
		System.out.println("end");
		
	}

}
