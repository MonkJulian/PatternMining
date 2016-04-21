package misc;


import patex.PatEx;
import fst.Fst;

public class CFstWriter {

	public static void main(String[] args) {
		String patEx = "3 (3) (3^) (3=^)"; // args[0];
		if(args.length > 0) {
			patEx = args[0].trim();
		}
		
		// Modify patter expression for partial match
		patEx = ".* " + patEx + " .*";
		
		PatEx regex = new PatEx(patEx);
		Fst cFst = regex.translateToFst();
		
		// Write compressed FST to file
		cFst.writeToStdout();
	}

}

