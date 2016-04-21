package driver;

import java.io.IOException;

import patex.PatEx;
import baseline.Dfs;
import fst.XFst;
import fst.Fst;
import hierarchy.SimpleHierarchy;
import mining.DfsMiner;
import utils.Dictionary;
import writer.SequentialWriter;



public class DfsMode {
	
	public static final int INF = Integer.MAX_VALUE;
	
	int sigma, gamma, lambda;
	boolean generalize;
	
	DfsMode(int sigma, int gamma, int lambda, boolean generalize) {
		this.sigma = sigma;
		this.gamma = gamma;
		this.lambda = lambda;
		this.generalize = generalize;
	}

	public void run() throws Exception {
		
		// i/o paths
		String input = "/home/kbeedkar/svn/test-data/nyt-1991/raw/part-r-00000";
		String dictionary = "/home/kbeedkar/svn/test-data/nyt-1991/wc/part-r-00000";
		String output = "/home/kbeedkar/svn/cbm/test/dfs-nyt-test-col-ngrams/";

		// load dictionary
		//Dictionary dict = new Dictionary();
		Dictionary dict = Dictionary.getInstance();
		dict.load(dictionary, sigma);

		// initialize hierarchy
		int[] itemToParent = dict.getItemToParent();
		SimpleHierarchy.getInstance().initialize(itemToParent);

		// initialize writer
		SequentialWriter writer = SequentialWriter.getInstance();
		writer.setItemIdToItemMap(dict.getItemIdToName());
		writer.setOutputPath(output);

		// mining
		Dfs dfs = new Dfs(sigma, gamma,lambda, generalize);

		//dfs.setLeastFrequentItem(dict.leastFrequentItem());
		
		long sS = System.currentTimeMillis();
		dfs.scanDatabase(input);

		long sE = System.currentTimeMillis();

		long mS = System.currentTimeMillis();
		dfs.mine();
		long mE = System.currentTimeMillis();

		long sTime = (long) ((sE - sS) / 1000.0);
		long mTime = (long) ((mE - mS) / 1000.0);

		long tTime = sTime + mTime;

		System.out.println("Total time = " + tTime);
		
		System.out.println("Scan time = " + sTime);
		System.out.println("Mining time = " + mTime);
		
		System.out.println("items counted = " + dfs.itemsCounted);
		System.out.println("Total patterns = " + dfs.noOutputPatterns());
	}

	public static void main(String[] args) throws Exception {
		
		//DfsMode dfsm = new DfsMode(100,Integer.MAX_VALUE,2);
		
		boolean generalize = false;
		
		DfsMode dfsm = new DfsMode(100,2,5,generalize);
		dfsm.run();

		
	}

}
