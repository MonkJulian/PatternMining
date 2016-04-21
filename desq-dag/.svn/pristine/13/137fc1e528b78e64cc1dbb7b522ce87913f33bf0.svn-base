package mining;

import fst.XFst;
import fst.OutputLabel;
import hierarchy.SimpleHierarchy;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;

import utils.Dictionary;
import utils.PrimitiveUtils;
import writer.SequentialWriter;


//TODO: separator changed to -1!!!

/**
 * @author kbeedkar
 *
 */
public class DfsMiner {

	// minimum support
	int sigma;

	// input transactions
	ArrayList<int[]> inputTransactions = new ArrayList<int[]>();

	// transaction supports -- not required as no aggregation
	// IntArrayList transactionSupports = new IntArrayList();
	static int tId;

	// Automata
	XFst dfa;

	// Buffer for processing transaction
	int[] transaction;

	// single (length-1) capture items globally across the database
	Items captureItems = new Items();

	// writer
	SequentialWriter writer = SequentialWriter.getInstance();

	/** hierarchy */
	SimpleHierarchy hierarchy = SimpleHierarchy.getInstance();

	// Parallel arrays for yeilds and state
	OutputLabel[] yieldArray;
	int[] stateArray;

	// initial dfa state
	int initialDfaState;

	int[] flist = Dictionary.getInstance().getFlist();
	//int[] flist = new int[]{0,2,1,1,1,1};
	
	//public int itemsCounted = 0;
	
	int noOfPatterns = 0;

	private boolean writeOutput = true;;

	// -- Methods

	public DfsMiner() {
	};

	public DfsMiner(int sigma, XFst dfa, boolean writeOutput) {
		this.sigma = sigma;
		this.dfa = dfa;

		yieldArray = new OutputLabel[dfa.oLabelList.size()];
		dfa.states.add(0);
		stateArray = new int[dfa.states.size()];

		dfa.oLabelList.toArray(yieldArray);
		dfa.states.toArray(stateArray);

		initialDfaState = dfa.getInitialState();
		
		this.writeOutput  = writeOutput;
	}
	
	
	public void clear() {
		inputTransactions.clear();
	}

	public void scanDatabase(String dbFile) throws Exception {
		FileInputStream fstream;
		fstream = new FileInputStream(dbFile);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		while ((strLine = br.readLine()) != null) {
			if (!strLine.isEmpty()) {
				// TODO: take item separator as parameter
				String[] sequence = strLine.split("\\s* \\s*");
				int[] sequenceAsInts = new int[sequence.length];
				int i = 0;
				for (String term : sequence) {
					sequenceAsInts[i] = Integer.parseInt(term);
					i++;
				}
				addTransaction(sequenceAsInts, 0, sequenceAsInts.length);
			}
		}
		br.close();
	}

	public void addTransaction(int[] transaction, int fromIndex, int toIndex) {
		int transactionId = tId++;

		int length = toIndex - fromIndex;
		int[] inputTransaction = new int[length];
		System.arraycopy(transaction, fromIndex, inputTransaction, 0, length);
		// TODO: do not add all transitions
		inputTransactions.add(inputTransaction);

		int position = fromIndex;
		//for (; position < toIndex; ++position) {
			// initial DFA state
			int state = initialDfaState;

			//int i = 0;
			while (position < toIndex) {
				int itemId = transaction[position];

				int offset = dfa.getOffset(state, itemId);
				boolean epsilonYield = false;
				int tmp = -1;

				if (offset >= 0) {
					for (; stateArray[offset] != 0; offset++) {
						int nextState = stateArray[offset];
						int yieldItem = yieldArray[offset].item;
						int currItem = itemId;

						
						switch (yieldArray[offset].type) {
						case EPSILON:
							tmp = offset;
							epsilonYield = true;
							break;
						case CONSTANT:
							if(flist[yieldItem] >= sigma) {
								captureItems.addItem(yieldItem, transactionId, (position), nextState);
								//itemsCounted++;
							}
							break;
						case SELF:
							if(flist[itemId] >= sigma) {
								captureItems.addItem(itemId, transactionId, (position), nextState);
								//itemsCounted++;
							}
							break;
						case SELFGENERALIZE:
							if(flist[itemId] >= sigma) {
								captureItems.addItem(itemId, transactionId, (position), nextState);
								//itemsCounted++;
							}
							// add parents;
							while (hierarchy.hasParent(currItem)) {
								if (currItem == yieldItem) {
									break;
								}
								currItem = hierarchy.getParent(currItem);
								if(flist[currItem] >= sigma) {
									captureItems.addItem(currItem, transactionId, (position), nextState);
									//itemsCounted++;
								}
							}
							break;
						default:
							break;
						}
						// offset++;
					}
				}

				if (epsilonYield) { // non capture transition
					//int nextState = stateArray[tmp];
					position++;
					state = stateArray[tmp];
				} /*else {
					//i++;
					//state = initialDfaState;
					break;
				}*/
			//}
		}
	}

	public void mine() throws IOException, InterruptedException {
		//System.out.println("Root items counted =" + itemsCounted);

		int[] prefix = new int[1];

		// TODO: use iterator
		for (Map.Entry<Integer, Item> entry : captureItems.itemIndex.entrySet()) {
			Item item = entry.getValue();
			if (item.support >= sigma) {
				prefix[0] = entry.getKey();
				// if we reached a final state with this item

				/*if (item.isAccept && writeOutput) {
					//System.out.println(Arrays.toString(prefix) + " : " + item.support);
					writer.write(prefix, item.support);
					noOfPatterns++;
				}*/

				//rightExpansion(prefix, item.transactionIds, item.isAccept);
				//item.flush();
				rightExpansion(prefix, item.transactionIds);
			}
		}
		clear();
	}

	private void rightExpansion(int[] prefix, ByteArrayList transactionIds) throws IOException,
			InterruptedException {

		// posting list for prefix
		PostingList.Decompressor transactions = new PostingList.Decompressor(transactionIds);

		Items localCaptureItems = new Items();

		boolean reachedFinalState;

		int xCount = 0;
		int tmp = -1;
		boolean epsilonYield;

		// for all transations
		do {
			reachedFinalState = false;
			int transactionId = transactions.nextValue();
			transaction = inputTransactions.get(transactionId);

			// for all positions
			do {
				int state = transactions.nextValue(); //current state in DFA
				reachedFinalState |= dfa.isAccept(state);
				int position = transactions.nextValue() + 1; // position of the next item input transaction

				while (position < transaction.length) {
					int itemId = transaction[position];

					int offset = dfa.getOffset(state, itemId);
					epsilonYield = false;

					if (offset >= 0) {
						// for all yield/state pairs
						for (; stateArray[offset] != 0; offset++) {
							int nextState = stateArray[offset];
							int yieldItem = yieldArray[offset].item;
							int currItem = itemId;

							switch (yieldArray[offset].type) {
							case EPSILON:
								tmp = offset;
								epsilonYield = true;
								break;
							case CONSTANT:
								if (flist[yieldItem] >= sigma) {	
									localCaptureItems.addItem(yieldItem, transactionId, (position), nextState);
									//itemsCounted++;
								}
								break;
							case SELF:
								if (flist[itemId] >= sigma) {
									localCaptureItems.addItem(itemId, transactionId, (position), nextState);
									//itemsCounted++;
								}
								break;
							case SELFGENERALIZE:
								if (flist[itemId] >= sigma) {
									localCaptureItems.addItem(itemId, transactionId, (position), nextState);
									//itemsCounted++;
								}
								// add parents;
								while (hierarchy.hasParent(currItem)) {
									if (currItem == yieldItem) {
										break;
									}
									currItem = hierarchy.getParent(currItem);
									if (flist[currItem] >= sigma) {
										localCaptureItems.addItem(currItem, transactionId, (position), nextState);
										//itemsCounted++;
									}
								}
								break;
							default:
								break;
							}
						}
					}
					if (epsilonYield) { // non capture transition
						int nextState = stateArray[tmp];
						position++;
						state = nextState;
						reachedFinalState |= dfa.isAccept(state);
					} else
						break;
				}

			} while (transactions.hasNextValue());

			// increment the count, if we reached a final state with a
			// non-capture transition
			if (reachedFinalState)
				xCount++;

		} while (transactions.nextPosting());

		// We reached a final state with non captured transition then output
		// what we captured until now with updated support
		//if (reachedFinalState && xCount >= sigma && !accepted) {
		//if (xCount >= sigma && !accepted) {
		if (xCount >= sigma) {
			//System.out.println(Arrays.toString(prefix) + " : " + xCount);
			noOfPatterns++;
			if(writeOutput)
				writer.write(prefix, xCount);
		}

		int[] newPrefix = new int[prefix.length + 1];

		final IntIterator it = localCaptureItems.itemIndex.keySet().iterator();
		while (it.hasNext()) {
			int itemId = it.nextInt();
			Item item = localCaptureItems.itemIndex.get(itemId);
			if (item.support >= sigma) {
				System.arraycopy(prefix, 0, newPrefix, 0, prefix.length);
				newPrefix[prefix.length] = itemId;

				// if we reached a final state with this item
				/*if (item.isAccept) {
					//System.out.println(Arrays.toString(newPrefix) + " : " +	item.support);
					if(writeOutput)
						writer.write(newPrefix, item.support);
					noOfPatterns++;
				}*/
				//rightExpansion(newPrefix, item.transactionIds, item.isAccept);
				//item.flush();
				rightExpansion(newPrefix, item.transactionIds);
			}
			it.remove();
		}

		localCaptureItems.clear();
	}
	
	
	public int noOutputPatterns() {
		return noOfPatterns;
	}

	// -- Helper classes

	private final class Item {
		int support = 0;
		int lastTransactionId = -1;
		//long lastStatePosition = -1;
		//boolean isAccept = false;
		ByteArrayList transactionIds = new ByteArrayList();;
		//IntOpenHashSet[] statePosSet = new IntOpenHashSet[dfa.getNumStates()];
		BitSet[] statePosSet = new BitSet[dfa.getNumStates()]; 
		
		Item() {
			for(int i = 0; i < dfa.getNumStates(); i++) {
				//statePosSet[i] = new IntOpenHashSet();
				statePosSet[i] = new BitSet();
			}
		}
		void flush() {
			for(int state = 0; state < dfa.getNumStates(); state++) {
				/*if(!statePosSet[state].isEmpty()) {
					//final IntIterator it = statePosSet[state].iterator();
					while(it.hasNext()) {
						int pos = it.nextInt();
						PostingList.addCompressed(state + 1, transactionIds);
						PostingList.addCompressed(pos + 1, transactionIds);
					}
					for(int i = statePosSet[state].nextSetBit(0); i >=0; i = statePosSet[state].nextSetBit(i+1)) {
						PostingList.addCompressed(state + 1, transactionIds);
						PostingList.addCompressed(i + 1, transactionIds);
					}
				}*/
				statePosSet[state].clear();
			}
		}
	}

	private final class Items {
		Int2ObjectOpenHashMap<Item> itemIndex = new Int2ObjectOpenHashMap<Item>();

		public void addItem(int itemId, int transactionId, int position, int dfaState) {
			Item item = itemIndex.get(itemId);
		
			if (item == null) {
				item = new Item();
				itemIndex.put(itemId, item);
			}

			//long sp = PrimitiveUtils.combine(dfaState, position);
			if (item.lastTransactionId != transactionId) {
			
				if(item.lastTransactionId != -1)
					item.flush();
				
				/** Add transaction separator */
				if (item.transactionIds.size() > 0) {
					PostingList.addCompressed(0, item.transactionIds);
				}

				item.lastTransactionId = transactionId;
				item.support++;
				
				//item.isAccept = (dfa.isAccept(dfaState) || item.isAccept);
				//item.lastStatePosition = sp;
				
				PostingList.addCompressed(transactionId + 1, item.transactionIds);
				PostingList.addCompressed(dfaState + 1, item.transactionIds);
				PostingList.addCompressed(position + 1, item.transactionIds);
				
				item.statePosSet[dfaState].set(position);
			}
			else if (!item.statePosSet[dfaState].get(position)) {
				item.statePosSet[dfaState].set(position);
				PostingList.addCompressed(dfaState + 1, item.transactionIds);
				PostingList.addCompressed(position + 1, item.transactionIds);
			}
			//item.statePosSet[dfaState].add(position);
			
		}

		public void clear() {
			itemIndex.clear();
		}
	}
}
