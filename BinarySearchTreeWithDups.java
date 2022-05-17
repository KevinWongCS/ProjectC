import java.util.*;

public class BinarySearchTreeWithDups<T extends Comparable<? super T>> extends BinarySearchTree<T>
		implements SearchTreeInterface<T>, java.io.Serializable {
	
	// EFFICIENCY VARIABLE
	private static int recursionTimes = 0;
	
	public BinarySearchTreeWithDups() {
		super();
	}

	public BinarySearchTreeWithDups(T rootEntry) {
		super(rootEntry);
		setRootNode(new BinaryNode<T>(rootEntry));
	}

	/** Description for add() method
	 * Adds a new entry to this tree, if it does not match an existing object in the
	 * tree. Otherwise, replaces the existing object with the new entry.
	 * 
	 * @param newEntry An object to be added to the tree.
	 * @return Either null if anEntry was not in the tree but has been added, or the
	 *         existing entry that matched the parameter anEntry and has been
	 *         replaced in the tree.
	 */
	@Override
	public T add(T newEntry) {
		T result = null;
		if (isEmpty())
			setRootNode(new BinaryNode<T>(newEntry));
		else
			result = addEntryHelperNonRecursive(newEntry);
		return result;
	}

	// THIS METHOD CANNOT BE RECURSIVE.
	private T addEntryHelperNonRecursive(T newEntry) {
		T result = null;
		int comparison = 0;
		BinaryNode<T> newNode = new BinaryNode<>(newEntry); // create new leaf
		BinaryNode<T> rootNode = this.getRootNode(); // set rootNode(aka "currentNode") to rootNode
		
		// transversing the BST
		while (rootNode != null) {
			comparison = newNode.getData().compareTo(rootNode.getData()); // 1. Compare the new element to the current element.
			
			if (comparison < 0) { // 2. If the new element is smaller, go into the left subtree. Return to step 1.
				if (rootNode.hasLeftChild()) { // this logic ensures rootNode stops at leaf 
					rootNode = rootNode.getLeftChild();;
				} else {
					break;
				}
			} else if (comparison > 0) { // 3. If the new element is larger, go into the right subtree. Return to step 1.
				if (rootNode.hasRightChild()) { // this logic ensures rootNode stops at leaf 
					rootNode = rootNode.getRightChild();;
				} else {
					break;
				}
			} else if (comparison == 0) { // 4. If the new element is equal, go into the left subtree. Return to step 1.
				result = newEntry; // returns the data value if repeated (optional)
				if (rootNode.hasLeftChild()) { // this logic ensures rootNode stops at leaf 
					rootNode = rootNode.getLeftChild();;
				} else {
					break;
				}
			}
		} // end of outer while loop this loop breaks when rootNode == null	
		
		// appending the new leaf
		if (comparison <= 0) { // If the new element is smaller or equal, go into the left subtree
			rootNode.setLeftChild(newNode);
		} else { // If the new element is larger, go into the right subtree
			rootNode.setRightChild(newNode);
		}
		return result;
	}
	// THIS METHOD CANNOT BE RECURSIVE.
	// Make sure to take advantage of the sorted nature of the BST!
	public int countEntriesNonRecursive(T target) {
		
		// EFFICIENCY VARIABLE
		int loopTimes=0;
		
		int count = 0;
		Stack<BinaryNode<T>> nodeStack = new Stack<>();
		BinaryNode<T> currentNode = getRootNode();

		// empty: return 0
		if (this.getRootNode() == null) {
			return count;
		}

		// loop using a in-order traversal of the BST
		while (!nodeStack.isEmpty() || currentNode != null) {
			
			// EFFICIENCY VARIABLE
			loopTimes++;
			
			// This one conditional takes advantage of order in BST
			while (currentNode!= null && target.compareTo(currentNode.getData()) > 0) {
				currentNode = currentNode.getRightChild();
			}
			
			while (currentNode != null) {
				nodeStack.push(currentNode);
				currentNode = currentNode.getLeftChild();
			}

			if (!nodeStack.isEmpty()) {
				BinaryNode<T> nextNode = nodeStack.pop();

				if (nextNode.getData().equals(target)) {
					count++;
				}
				currentNode = nextNode.getRightChild();
			}
		} // end of outer while loop
		
		// EFFICIENCY VARIABLE
		System.out.println(loopTimes);
		
		return count;
	}
	
	
	// THIS METHOD MUST BE RECURSIVE! 
	// You are allowed to create a private helper.
	// Make sure to take advantage of the sorted nature of the BST!
	public int countGreaterRecursive(T target) {
		
		// EFFICIENCY VARIABLE
		recursionTimes = 0;
		
		int count = 0;
		BinaryNode<T> rootNode = getRootNode();
		
		// empty: return 0
		if (this.getRootNode() == null) {
			return count;
		} else { // not empty call recursive method
			count = inorderTraverse(rootNode, target);
		}
		
		// EFFICIENCY VARIABLE
		System.out.println(recursionTimes);
		
		return count;
	}
	
	private int inorderTraverse(BinaryNode<T> node, T target) { // this private method is used by countGreaterRecursive
		
		// EFFICIENCY VARIABLE
		recursionTimes++;
		
		int count = 0;

		if (node != null) {
			if (target.compareTo(node.getData()) < 0) {
				count += inorderTraverse(node.getLeftChild(), target);
			} 
			
			if (node.getData().compareTo(target) > 0) {
				count += 1;
			}
			
			count += inorderTraverse(node.getRightChild(), target);
		}
		return count;
	}
		
	
	// THIS METHOD CANNOT BE RECURSIVE.
	// Hint: use a stack!
	// Make sure to take advantage of the sorted nature of the BST!
	public int countGreaterIterative(T target) {
		
		// EFFICIENCY VARIABLE
		int loopTimes=0;
		
		int count = 0;
		Stack<BinaryNode<T>> nodeStack = new Stack<>();
		BinaryNode<T> currentNode = getRootNode();

		// empty
		if (this.getRootNode() == null) {
			return count;
		}

		// loop using a post-order traversal of the BST
		while (!nodeStack.isEmpty() || currentNode != null) {
			
			// EFFICIENCY VARIABLE
			loopTimes++;
			
			// This one conditional takes advantage of order in BST
			while (currentNode!= null && target.compareTo(currentNode.getData()) > 0) {
				currentNode = currentNode.getRightChild();
			}
			
			while (currentNode != null) {
				nodeStack.push(currentNode);
				currentNode = currentNode.getLeftChild();
			}

			if (!nodeStack.isEmpty()) {
				BinaryNode<T> nextNode = nodeStack.pop();

				if (nextNode.getData().compareTo(target) > 0) {
					count++;
				}
				currentNode = nextNode.getRightChild();
			}
		} // end of outer while loop
		
		// EFFICIENCY VARIABLE
		System.out.println(loopTimes);
		
		return count;
	}
		
	
	// For full credit, the method should be O(n).
	// You are allowed to use a helper method.
	// The method can be iterative or recursive.
	// If you make the method recursive, you might need to comment out the call to the method in Part B.
	public int countUniqueValues() {
		BinaryNode<T> currentNode = this.getRootNode();
		return this.countUniqueHelper(currentNode, (Comparable)null);
	}

	private int countUniqueHelper(BinaryNode<T> currentNode, Comparable prevTarget) {
		int count = 0;
		if (currentNode != null) {
			if (((Comparable)currentNode.getData()).equals(prevTarget)) {
				--count;
			}

			count += this.countUniqueHelper(currentNode.getLeftChild(), (Comparable)currentNode.getData());
			++count;
			count += this.countUniqueHelper(currentNode.getRightChild(), prevTarget);
		}

		return count;
	}
		
	
	public int getLeftHeight() {
		BinaryNode<T> rootNode = getRootNode();
		if(rootNode==null) {
			return 0;
		} else if(!rootNode.hasLeftChild()) {
			return 0;
		} else {
			return rootNode.getLeftChild().getHeight();
		}
	}

	public int getRightHeight() {
		BinaryNode<T> rootNode = getRootNode();
		if(rootNode==null) {
			return 0;
		} else if(!rootNode.hasRightChild()) {
			return 0;
		} else {
			return rootNode.getRightChild().getHeight();
		}
	}
	

}
