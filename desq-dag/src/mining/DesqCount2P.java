package mining;

import org.apache.lucene.util.FixedBitSet;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import fst.XFst;

public class DesqCount2P extends DesqCount {
	
	XFst reverseXfst;
	
	int numStates;
	
	IntArrayList buffer = new IntArrayList();

	public DesqCount2P(int sigma, XFst xfst, boolean writeOutput, boolean useFlist) {
		super(sigma, xfst, writeOutput, useFlist);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void computeMatch() {
		
		FixedBitSet reachableStates = new FixedBitSet(numStates * (sequence.length));
		FixedBitSet hasFinalState = new FixedBitSet(sequence.length);
		
		int currOffset, prevOffset, nextOffset;
		boolean isFrequent;
		for (int pos = 0; pos < sequence.length; ++pos) {
			isFrequent = (flist[sequence[pos]] >= sigma) ? true : false;
			
		}
		
	}

}
