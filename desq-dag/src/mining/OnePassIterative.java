/**
 * 
 */
package mining;

import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;

import fst.OutputLabel;
import fst.XFst;

/**
 * OnePassIterative.java
 * 
 * @author Kaustubh Beedkar {kbeedkar@uni-mannheim.de}
 */
public class OnePassIterative extends DesqCount {

	// Parallel arrays for outputLabel-toState pairs
	OutputLabel[] oLabelArray;

	int[] toStateArray;

	// Parallel arrays for buffer, state, pos for the stack
	ArrayList<int[]> buffers = new ArrayList<int[]>();

	IntArrayList states = new IntArrayList();

	IntArrayList positions = new IntArrayList();

	public OnePassIterative(int sigma, XFst xPFst, boolean writeOutput, boolean useFlist) {
		super(sigma, xPFst, writeOutput, useFlist);

		this.oLabelArray = new OutputLabel[xfst.oLabelList.size()];
		this.xfst.states.add(XFst.DELIM);
		this.toStateArray = new int[this.xfst.states.size()];

		this.xfst.oLabelList.toArray(oLabelArray);
		this.xfst.states.toArray(toStateArray);
	}

	@Override
	protected void computeMatch() {
		// Initialize the stack
		buffers.add(new int[0]);
		states.add(xfst.getInitialState());
		positions.add(0);

		int stackPos = 0;
		while (stackPos < positions.size()) {

			int state = states.getInt(stackPos);
			int position = positions.getInt(stackPos);
			int[] buffer = buffers.get(stackPos);

			if (xfst.isAccept(state)) {
				if (!(buffer.length == 0)) {
					countSequence(buffer);
					gpt++;
				}
			}

			if (position == sequence.length) {
				stackPos++;
				continue;
			}

			int inputItem = sequence[position];
			int offset = xfst.getOffset(state, inputItem);

			if (offset >= 0) {
				for (; toStateArray[offset] != XFst.DELIM; offset++) {
					int nextState = toStateArray[offset];
					int yieldItem = oLabelArray[offset].item;
					int currItem = inputItem;

					switch (oLabelArray[offset].type) {
					case EPSILON:
						addToStack(buffer, nextState, position + 1);
						break;

					case CONSTANT:
						if (!useFlist || flist[yieldItem] >= sigma) {
							int[] newBuffer = new int[buffer.length + 1];
							System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
							newBuffer[buffer.length] = yieldItem;

							addToStack(newBuffer, nextState, position + 1);
						}
						break;
					case SELF:
						if (!useFlist || flist[inputItem] >= sigma) {
							int[] newBuffer = new int[buffer.length + 1];
							System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
							newBuffer[buffer.length] = inputItem;

							addToStack(newBuffer, nextState, position + 1);
						} 
						break;
					case SELFGENERALIZE:
						// add parents
						while (hierarchy.hasParent(currItem)) {
							if (currItem == yieldItem)
								break;
							currItem = hierarchy.getParent(currItem);

							if (!useFlist || flist[currItem] >= sigma) {
								int[] newBuffer = new int[buffer.length + 1];
								System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
								newBuffer[buffer.length] = currItem;

								addToStack(newBuffer, nextState, position + 1);
							} 
						}

						// add item
						if (!useFlist || flist[inputItem] >= sigma) {
							int[] newBuffer = new int[buffer.length + 1];
							System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
							newBuffer[buffer.length] = inputItem;

							addToStack(newBuffer, nextState, position + 1);
						}
						break;
					default:
						break;
					}
				}
			}
			stackPos++;

		}
		clearStack();
	}

	private void addToStack(int[] buffer, int state, int position) {

		buffers.add(buffer);
		states.add(state);
		positions.add(position);

	}

	private void clearStack() {
		buffers.clear();
		states.clear();
		positions.clear();
	}

}
