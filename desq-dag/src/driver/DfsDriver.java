package driver;

import java.io.IOException;

import baseline.Dfs;
import hierarchy.SimpleHierarchy;
import utils.Dictionary;
import writer.LogWriter;
import writer.SequentialWriter;

public class DfsDriver {

	/** <input> <output> <sigma> <gamma> <lambda> <0/1?> <logfile> <0?> 
	 * @throws Exception */
	public static void main(String[] args) throws Exception {
		String input = args[0];
		String output = args[1];
		
		int sigma = Integer.parseInt(args[2]);
		int gamma = Integer.parseInt(args[3]);
		int lambda = Integer.parseInt(args[4]);
		
		boolean generalize = (args[5].equals("0")) ? false : true;
		
		String logfile = args[6];
		
		boolean writeOutput = (args[7].equals("0")) ? false : true;
		
		String sequenceFile = input.concat("/raw/part-r-00000");
		String dictionary = input.concat("/wc/part-r-00000");
		
		/** load dictionary */
		Dictionary dict = Dictionary.getInstance();
		dict.load(dictionary, sigma);
		
		
		/** initialize hierarchy */
		int[] itemToParent = dict.getItemToParent();
		SimpleHierarchy.getInstance().initialize(itemToParent);
		
		/** initialize writer */
		
		if(writeOutput) {
		SequentialWriter writer = SequentialWriter.getInstance();
			writer.setItemIdToItemMap(dict.getItemIdToName());
			writer.setOutputPath(output);
		}
		
		long tS = System.currentTimeMillis();
		
		Dfs dfs = new Dfs(sigma, gamma,lambda, generalize, writeOutput);
		dfs.scanDatabase(sequenceFile);
		dfs.mine();
		
		long tE = System.currentTimeMillis();
		
		long totaltime = (long) ((tE-tS)/1000.0);
		
		/** Write stats to log*/
		LogWriter lwriter = LogWriter.getInstance();
		lwriter.setOutputPath(logfile);
		String s = null;
		s = "DFS" 
		+ "\t" + sigma 
		+ "\t" + gamma 
		+ "\t" + lambda
		+ "\t" + generalize
		+ "\t" + totaltime
		+ "\t" + dfs.noOutputPatterns();
		lwriter.write(s);
	}

}
