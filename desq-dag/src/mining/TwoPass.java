package mining;

import fst.XFst;
import fst.OutputLabel;
import fst.OutputLabel.Type;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;





import org.apache.lucene.util.OpenBitSet;

import driver.DesqCountDriver;


/**
 * TwoPass.java
 * @author Kaustubh Beedkar {kbeedkar@uni-mannheim.de}
 */
public class TwoPass extends DesqCount {

	// Members

	// pFst with reverse edges
	XFst rXPFst;

	// Parallel toState, outputType arrays
	int[] toStateArray;
	
	Type[] outputTypeArray;

	// parallel fromState,output arrays
	int[] fromStateArray;

	OutputLabel[] yieldArray;
	
	// Buffer to store generated output
	IntArrayList buffer = new IntArrayList();
	
	// Helpers
	final int EPS = -1; final int END = -1;
	
	// Total states in pFST
	int numPFstStates;
	
	// Initital pFst state
	int initialPFstState;
	
	
	// Begin and end positions of the subsequence to scan during backward pass
	//int beginPos = 0;
	int beginPos = Integer.MAX_VALUE;
	
	// int endPos = sequence.length - 1;
	int endPos = -1;
	
	boolean foundBeginPos = false;		
	
	
	// Parallel arrays for state,pointer-to-sequence
	//IntArrayList stateList = new IntArrayList();
	
	//ObjectArrayList<Node> pointerList = new ObjectArrayList<Node>();
	
	
	class Node {
		int item;
		
		ObjectArrayList<Node> suffixes;
		
		Node(int item, ObjectArrayList<Node> suffixes) {
			this.item = item;
			this.suffixes = suffixes;
		}
	}
	
	ObjectArrayList<Node>[] stateSuffix; 
	int[] stateList;
	int stateListSize = 0;
	

	// Methods

	public TwoPass(int sigma, XFst xPFst, boolean writeOutput, boolean useFlist, XFst rXPFst) {
		super(sigma, xPFst, writeOutput, useFlist);

		this.rXPFst = rXPFst;

		// Initialize toStateArray
		xfst.states.add(-1); // TODO should be handled by DFA
		toStateArray = new int[xfst.states.size()];
		xfst.states.toArray(toStateArray);

		
		// Initialize outputTypeArray
		outputTypeArray = new Type[xfst.oLabelList.size()];
		for(int i = 0; i < xfst.oLabelList.size(); ++i){
			outputTypeArray[i] = xfst.oLabelList.get(i).type;
		}
		
		// Initialize fromState, output arrays
		this.rXPFst.states.add(-1);
		fromStateArray = new int[this.rXPFst.states.size()];
		yieldArray = new OutputLabel[this.rXPFst.oLabelList.size()];
		this.rXPFst.states.toArray(fromStateArray);
		this.rXPFst.oLabelList.toArray(yieldArray);
		
		// Initialize misc members
		this.numPFstStates = xfst.getNumStates();
		this.initialPFstState = xfst.getInitialState();

		stateSuffix = (ObjectArrayList<Node>[]) new ObjectArrayList[numPFstStates];
		//stateList = new IntArrayList(numPFstStates);
		stateList = new int[numPFstStates];
	}

	
	
	private void forwardPass(int state, int pos, int nextOffset, OpenBitSet reachableStates, OpenBitSet hasFinalState) {
		if (pos == sequence.length)
			return;
		int item = sequence[pos];

		DesqCountDriver.fstTime.start();
		
		int offset = xfst.getOffset(state, item);
		
		DesqCountDriver.fstTime.stop();
		
		if (offset >= 0) {
			boolean frequent = (flist[item] >= sigma) ? true : false;
			
			for (; toStateArray[offset] != XFst.DELIM; ++offset) {
				
				int nextState = toStateArray[offset];
				
				if ( !reachableStates.fastGet(nextOffset + nextState) && (frequent || outputTypeArray[offset] != Type.SELF)) {
					
				
					reachableStates.fastSet(nextOffset + nextState);
				
					// Mark pos if we reached a final state consuming
					// seq[pos]
					// also track the last pos with final state
					if (xfst.isAccept(nextState)) {
						hasFinalState.fastSet(pos);
						endPos = Math.max(endPos, pos);
					}

					// Mark beginPos as position at which first output
					// 	is generated
					if (pos < beginPos && outputTypeArray[offset] != Type.EPSILON) {
						beginPos = pos;
					}
				
					forwardPass(nextState, pos + 1, nextOffset + numPFstStates, reachableStates, hasFinalState);
				}
				
			}
		}
	}
	
	
	
	
	@Override
	protected void computeMatch() {

		// Bits to store reachable states after consuming sequence[pos]
		OpenBitSet reachableStates = new OpenBitSet(numPFstStates * (sequence.length + 1));

		// Bits indicating if there was a final state reached after consuming
		// sequence[pos]
		OpenBitSet hasFinalState = new OpenBitSet(sequence.length);

		// offset of bits for next pos
		int currOffset, nextOffset, prevOffset;

		//
		int state, offset;
		
		//
		boolean frequent;
		
		//////
		/////
		//// Forward pass over the sequence
		///
		//


		DesqCountDriver.forwardPassTime.start();
		
		reachableStates.fastSet(initialPFstState);

		forwardPass(initialPFstState, 0, numPFstStates, reachableStates, hasFinalState);
		
		/*
		// loop over all positions
		for (int pos = 0; pos < sequence.length; pos++) {

			
			frequent = (flist[sequence[pos]] >= sigma) ? true : false;
			
			currOffset = pos * numPFstStates;
			nextOffset = currOffset + numPFstStates;
			
			//reachableStates.fastSet(currOffset); //TODO: hacky: ideally FST should be minimized to handle .* in the front
			
			// loop over all set bits
			for (int i = reachableStates.nextSetBit(currOffset);  i < nextOffset; i = reachableStates.nextSetBit(i + 1)) {

				// Set reachable bits from state at bit i
				//state = i - currOffset;
				offset = pFst.getOffset(i - currOffset, sequence[pos]);
				
				//TODO: Keep track of the first item that matches the input label
				
				
				if (offset >= 0) {
					for (; toStateArray[offset] != XFst.DELIM; ++offset) {
						
						if (frequent || outputTypeArray[offset] != Type.SELF) {
						
							reachableStates.fastSet(nextOffset + toStateArray[offset]);

							// Mark pos if we reached a final state consuming
							// seq[pos]
							// also track the last pos with final state
							if (pFst.isAccept(toStateArray[offset])) {
								hasFinalState.fastSet(pos);
								endPos = pos;
							}

							// Mark beginPos as position at which first output
							// is generated
							if (!foundBeginPos && outputTypeArray[offset] != Type.EPSILON) {
								beginPos = pos;
								foundBeginPos = true;
							}
						}
					}
				}
			}
			
		}*/


		DesqCountDriver.forwardPassTime.stop();
		
		
		//////
		/////
		//// Backward pass over the sequence
		///
		//
		
		
		
		DesqCountDriver.backwardPassTime.start();
		
		
		// begin and end pointers in stateList
		//int b = 0, e = 0;
	
		//
		int fromState, toState;
		int item;

		
		
		// loop over sequence[endPos,beginPos]
		for (int pos = endPos; pos >= beginPos; pos--) {

			item = sequence[pos];
			
			//System.out.println("item = " + item);
			
			// Update offsets for backward pass
			currOffset = (pos + 1) * numPFstStates;
			nextOffset = currOffset + numPFstStates;
			prevOffset = currOffset - numPFstStates;
			
			// If we reached a final state consuming seq[pos]
			if (hasFinalState.fastGet(pos)) {
				// Compute and add final states to working list
				for(int i = reachableStates.nextSetBit(currOffset); 
						i >= 0 && i < nextOffset ; i = reachableStates.nextSetBit(i+1) ) {
					// Add state at i and epsilon to working list
					if(xfst.isAccept(state = i - currOffset)) {
						if(stateSuffix[state] == null) {
							stateList[stateListSize++] = state;
							stateSuffix[state] = new ObjectArrayList<Node>();
						}
						stateSuffix[state].add(null);
					}
				}
			}
			
			// set of suffixes and states for the reachable states
			ObjectArrayList<Node>[] nextStateSuffix = (ObjectArrayList<Node>[]) new ObjectArrayList[numPFstStates];
			int[] nextStateList = new int[numPFstStates];
			int nextStateListSize = 0;
			
			for(int i = 0; i < stateListSize; i++) {
				
			
				fromState = stateList[i];
				//System.out.println("fromState = " + currentState);
				
				DesqCountDriver.fstTime.start();
				
				// compute reachable states from current state
				offset = rXPFst.getOffset(fromState, item);
				
				DesqCountDriver.fstTime.stop();
				
				if(offset >= 0) {
					for(; fromStateArray[offset] != XFst.DELIM; offset++) {
						
						toState = fromStateArray[offset];
						
						// If this state was marked reachable during the forward pass
						if(reachableStates.fastGet(prevOffset + toState)){
						
							//System.out.println("toState = " + fromState);
							boolean output = (toState == initialPFstState) ? true: false;
							
							if(!output && nextStateSuffix[toState] == null) {
								// add fromState to working list
								nextStateList[nextStateListSize++] = toState;
								nextStateSuffix[toState] = new ObjectArrayList<Node>();
							}
							this.add(fromState, item, yieldArray[offset], nextStateSuffix[toState], output);
						}
					}
				}
			}
			// Update 
			this.stateList = nextStateList;
			this.stateListSize = nextStateListSize;
			this.stateSuffix = nextStateSuffix;
		}
		
		// output sequence after consuming seq[pos]
		for(int i = 0; i < stateListSize; ++i) {
			computeOutput(stateSuffix[stateList[i]]);
		}
		
		DesqCountDriver.backwardPassTime.stop();
		
		this.clear();
		
		
	}
	
	private void clear() {
		for (int i = 0; i < numPFstStates; ++i){
			stateSuffix[i] = null;
		}
		stateListSize = 0;
		beginPos = Integer.MAX_VALUE;
		endPos = -1;
		foundBeginPos = false;		
	}

	private void add(int state, int inputItem, OutputLabel olabel, ObjectArrayList<Node> suffixes, boolean output) {
		Node node;
		switch (olabel.type) {
		case EPSILON:
			if(output) {
				computeOutput(stateSuffix[state]);
			} else {
				for(Node n : stateSuffix[state])
					suffixes.add(n);
			}
			
			break;
		case CONSTANT:
			if (!useFlist || flist[olabel.item] >= sigma) {
				node = new Node(olabel.item, stateSuffix[state]);

				if(output) {
					computeOutput(node);
					break;
				}
				suffixes.add(node);
			}
			break;
		case SELF:
			if (!useFlist || flist[inputItem] >= sigma) {
				node = new Node(inputItem, stateSuffix[state]);
				
				if(output) {
					computeOutput(node);
					break;
				}
				
				suffixes.add(node);
			}
			break;
		case SELFGENERALIZE:
			// add input item
			
			if(!useFlist || flist[inputItem] >= sigma) {
				node = new Node(inputItem, stateSuffix[state]);
				
				if(output) {
					computeOutput(node);
				} else {
					suffixes.add(node);
				}
			}
			
			//add ancestors
			int currItem = inputItem;
			while (hierarchy.hasParent(currItem)) {
				if(currItem == olabel.item)
					break;
				currItem = hierarchy.getParent(currItem);
				if(!useFlist || flist[currItem] >= sigma) {
					node = new Node(currItem, stateSuffix[state]);
					if (output) {
						computeOutput(node);
					} else {
						suffixes.add(node);
					}
				}
			}
			break;
		default:
			break;
		}
	}
	
	
	private void computeOutput(ObjectArrayList<Node> suffixes) {
		for (Node node : suffixes)
			computeOutput(node);
	}
	
	
	private void outputBuffer() {
		
		if(!buffer.isEmpty()) {
			countSequence(buffer.toIntArray());
			//System.out.println(buffer);
		}
	}
	
	private void computeOutput(Node node) {
		if(node == null) {
			outputBuffer();
			return;
		}
		
		buffer.add(node.item);
		for(Node n : node.suffixes) {
			computeOutput(n);
		}
		buffer.remove(buffer.size() - 1);
	}

}
