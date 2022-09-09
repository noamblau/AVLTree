//Noam Blau
//Ido Schwartz

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {
	private IAVLNode root;
	private IAVLNode min;
	private IAVLNode max;
	
	/**
	   * public AVLTree()
	   *
	   * constructor
	   * 
	   * time complexity: O(1)
	   *
	   */
	public AVLTree() {
		this.root = null;
		this.min = null;
		this.max = null;
	}

	/**
	   * public boolean empty()
	   *
	   * returns true if and only if the tree is empty
	   * 
	   *time complexity: O(1)
	   *
	   */
   public boolean empty() {
	  return this.getRoot() == null || !this.getRoot().isRealNode();
  }
  
  /**
   * private static IAVLNode treePosition(int k, IAVLNode root)
   *
   * returns the node with key k if it exists in the tree
   * otherwise, returns its node predecessor if it was in the tree
   * 
   * time complexity: O(log(n))
   */
  private static IAVLNode treePosition(int k, IAVLNode root) {
	  IAVLNode node = root;
	  IAVLNode position = node;
	  
	//searching the given key, from the root down
	  while (node.isRealNode()) { 
		  position = node;
		  if (k == node.getKey()) //if true we found the node with key k 
			  return node;
		  else if (k < node.getKey()) //if true the possible location for the node is in the left subtree
			  node = node.getLeft();
		  else
			  node = node.getRight();
	  }
	  return position;
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   * 
   * time complexity: O(log(n))
   */
	public String search(int k)
	{
		IAVLNode searched = searchNode(k);
		if (searched == null) 
			return null;//k isn't a key in the tree
		else
			return searched.getValue();
	}
  
	/**
	   * public IAVLNode searchNode(int k)
	   *
	   * returns the node with key k if it exists in the tree
	   * otherwise, returns null
	   * 
	   * time complexity: O(log(n))
	   */
  public IAVLNode searchNode(int k) {
	  if (this.empty()) return null;
	  IAVLNode node = treePosition(k, this.getRoot());
	  if(node.getKey() == k) //if true k is a key in the tree
		  return node;
	  return null;//k isn't a key in the tree
  }
  
  /**
   *  private void insertUpdateMinMax(IAVLNode node)
   *
   * updating the min and max fields, for the insertion method, 
   * if the new node's key is smaller then the min's key, or bigger then the max's key
   * 
   * time complexity: O(1)
   */
  private void insertUpdateMinMax(IAVLNode insertNode) {
	//the tree is empty 
	  if (this.empty()) {
		   this.min = insertNode;
		   this.max = insertNode;
	   }
	   else {
		   int key = insertNode.getKey();
		   //the new node's key is smaller then the min's key
		   if (key < this.min.getKey()) this.min = insertNode;
		   //the new node's key is bigger then the max's key
		   if (key > this.max.getKey()) this.max = insertNode;
	   }
  }
  
  /**
   *  private void deleteUpdateMinMax(int k)
   *
   * updating the min and max fields, for the deletion method,
   * if the given k (which is the key of the node that is supposed to be deleted) is the min's or max's key
   * 
   * time complexity: O(log(n))
   */
  private void deleteUpdateMinMax(int deletetKey) {
	  //the min supposed to be deleted
	  if (deletetKey == this.min.getKey()) {
		  //the min is a leaf so its successor is its parent
		  if (this.min.getHeight() == 0) this.min = this.min.getParent();
		  else this.min = successor(this.min);
	  }
	//the max is supposed to be deleted
	  if (deletetKey == this.max.getKey()) {
		  //the max is a leaf so its successor is its parent
		  if (this.max.getHeight() == 0) this.max = this.max.getParent();
		  else this.max = predecessor(this.max);
	  }
 }
  
  /**
   * private IAVLNode successor(IAVLNode node)
   *
   * gets a node in the tree
   * returns the successor of the node
   * 
   * time complexity: O(log(n))
   */
  private IAVLNode successor(IAVLNode node) {
	  IAVLNode successor = node.getRight();
	  //if node has right child we go one time right and then all the way down left 
	  if (successor != null && successor.isRealNode()) {
		  while (successor.getLeft().isRealNode()) {
			  successor = successor.getLeft();
		  }
		  return successor;
	  }
	  //node has no right child so we go up to root from the node
	  //we returns the first node that we came from its left child
	  successor = node;
	  while (successor.isRightSon()) {
		  successor = successor.getParent();
	  }
	  return successor.getParent();  
  }
  
  /**
   * private IAVLNode predecessor(IAVLNode node)
   *
   * gets a node in the tree
   * returns the predecessor of the node
   * 
   * time complexity: O(log(n))
   */
  private IAVLNode predecessor(IAVLNode node) {
	  IAVLNode predecessor = node.getLeft();
	  //if node has left child we go one time left and then all the way down right 
	  if (predecessor != null && predecessor.isRealNode()) {
		  while (predecessor.getRight().isRealNode()) {
			  predecessor = predecessor.getRight();
		  }
		  return predecessor;
	  }
	  //node has no left child so we go up to root from the node
	  //we returns the first node that we came from its right child
	  predecessor = node;
	  while (!predecessor.isRightSon()) {
		  predecessor = predecessor.getParent();
	  }
	  return predecessor.getParent();
 }

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k already exists in the tree.
   * 
   *  time complexity: O(log(n))
   */
  public int insert(int k, String i) {
	  //This will be the number of balance operations after the insertion
	   int insertVal = 0;
	   AVLNode insertNode = new AVLNode(k, i);
	   insertUpdateMinMax(insertNode);
	   if (this.empty()) {
		   //If the tree is empty, 
		   //there is no need for balance operations, only to change the root
		   this.setRoot(insertNode);
	   }  
	   else
	   {
		   IAVLNode insertNodeParent = treePosition(k,this.getRoot());
		   // The key exists in the tree
		   if (k == insertNodeParent.getKey())
			   return -1;
		   else if (k < insertNodeParent.getKey())
			   insertNodeParent.connectSon(insertNode, 'L');
		   else
			   insertNodeParent.connectSon(insertNode, 'R');
		   insertVal = rebalanceAfterInsert(insertNode);
	   }
	   return insertVal;
  }
  
  /**
   *  public int rebalanceAfterInsert(IAVLNode insertNode)
   *
   * Receives a node which was inserted to the tree, 
   * balances the tree and updates all the fields 
   * of the tree and the nodes
   * returns the number of balancing operations(promotion/demotion/rotation)
   * it used.
   * 
   * time complexity: O(logn)
   */
   public int rebalanceAfterInsert(IAVLNode insertNode) {
	   int rebalanceCount = 0;
	   char dir;
	   IAVLNode parent  = insertNode.getParent();
	   IAVLNode son = insertNode;
	   while (parent != null) {
		   if(son.isRightSon())
			   dir = 'R';
		   else
			   dir = 'L';
		   if(isHeightDiff(parent, son,0)){
			   if(dir == 'R') {
				   // INSERT RIGHT CASE 1 -> Promote
				   if (isHeightDiff(parent, parent.getLeft(),1)) {
					  updateParent(parent); //promote
					  rebalanceCount++;
				   }
				   else if(isHeightDiff(parent, parent.getLeft(),2)) {
					   // INSERT RIGHT CASE 2 -> Single Rotate
					   if((isHeightDiff(son, son.getLeft(),2))&&
							   (isHeightDiff(son, son.getRight(),1))){
						   this.rotate(parent, 'L');
						   //The son and parent switched positions
						   //because of the rotate
						   IAVLNode tmp = son;
						   son = parent;
						   parent = tmp;
						   rebalanceCount+=2;
					   }
					   //INSERT RIGHT CASE 3 -> Double Rotate
					   else if((isHeightDiff(son, son.getLeft(),1))&&
							   (isHeightDiff(son, son.getRight(),2))){
						   this.doubleRotate(parent, "RL");
						   rebalanceCount+=5;
					   }
					   //JOIN RIGHT CASE -> Rotate and Promote
					   // This case can only be a result of join method use 
					   else if((isHeightDiff(son, son.getLeft(),1))&&
							   (isHeightDiff(son, son.getRight(),1))){
						   this.rotate(parent, 'L');
						   IAVLNode tmp = son;
						   son = parent;
						   parent = tmp;
						   updateParent(parent); //promote
						   rebalanceCount+=2;
					   }
				   }
			   }
			   if(dir =='L') {
				   //INSERT LEFT CASE 1 -> Promote
				   if(isHeightDiff(parent, parent.getRight(),1)){
						  updateParent(parent); //promote
						  rebalanceCount++;
				   }
				   else if(isHeightDiff(parent, parent.getRight(),2)) {
					   //INSERT LEFT CASE 2 -> Single Rotate
					   if((isHeightDiff(son, son.getRight(),2))&&
							   (isHeightDiff(son, son.getLeft(),1))) {
						   this.rotate(parent, 'R');
						   IAVLNode tmp = son;
						   son = parent;
						   parent = tmp;
						   rebalanceCount+=2;
					   }
					   //INSERT LEFT CASE 3 -> Double Rotate
					   else if((isHeightDiff(son, son.getRight(),1))&&
							   (isHeightDiff(son, son.getLeft(),2))) {
						   this.doubleRotate(parent, "LR");
						   rebalanceCount+=5;
					   }
					 //JOIN LEFT CASE -> Single Rotate and Promote
					   else if((isHeightDiff(son, son.getLeft(),1))&&
							   (isHeightDiff(son, son.getRight(),1))){
						   this.rotate(parent, 'R');
						   IAVLNode tmp = son;
						   son = parent;
						   parent = tmp;
						   updateParent(parent);
						   rebalanceCount+=2;
					   }
				   }
			   }
		   }
		   son = parent;
		   parent = parent.getParent();
	   }
	   // Update all size fields from the inserted node upto the root
	   updateSizesfromNode(insertNode);
	   return rebalanceCount;
   } 
   
   /**
    *  private int rebalanceAfterDelete(IAVLNode startDeleteNode)
    *
    * Receives a node (by delete function) to start 
    * the deletion balacing from,
    * balances the tree and updates all the fields 
    * of the tree and the nodes
    * returns the number of balancing operations(promotion/demotion/rotation)
    * it used.
    * 
    * time complexity: O(logn)
    */
   private int rebalanceAfterDelete(IAVLNode startDeleteNode) {
	   int rebalanceCount = 0;
	   char dir;
	   IAVLNode parent  = startDeleteNode.getParent();
	   IAVLNode son = startDeleteNode;
	   IAVLNode rightSon;
	   IAVLNode leftSon;
	   while (parent != null) {
		   if(son.isRightSon())
			   dir = 'R';
		   else
			   dir = 'L';
		 //DELETE CASE 1 -> Demote
		   if((isHeightDiff(parent, parent.getRight(),2))&&
				   (isHeightDiff(parent, parent.getLeft(),2))){
			   updateParent(parent); //demote
			   rebalanceCount++;
		   }
		   else if(dir == 'L') {
			   rightSon = parent.getRight();
			   if((isHeightDiff(parent, son, 3)) &&
					(isHeightDiff(parent, rightSon ,1))){
				   // DELETE RIGHT CASE 2-3 -> Single Rotate 
				   if((isHeightDiff(rightSon, rightSon.getRight(),1)) &&
						   ((isHeightDiff(rightSon, rightSon.getLeft(),1))||
							(isHeightDiff(rightSon, rightSon.getLeft(),2)))) {
					   this.rotate(parent, 'L');
					   rebalanceCount+=3;
				   }
				   // DELETE RIGHT CASE 4 -> Double Rotate
				   else if((isHeightDiff(rightSon, rightSon.getRight(),2)) &&
					(isHeightDiff(rightSon, rightSon.getLeft(),1))) {
					   this.doubleRotate(parent, "RL");
					   rebalanceCount+=6;
				   }
			   }
		   }
		   else {
			   leftSon = parent.getLeft();
			   if((isHeightDiff(parent, son ,3)) &&
					(isHeightDiff(parent, leftSon ,1))){
				   // DELETE LEFT CASE 2-3 -> Single Rotation
				   if((isHeightDiff(leftSon, leftSon.getLeft(),1)) &&
						   ((isHeightDiff(leftSon, leftSon.getRight(),1))||
							(isHeightDiff(leftSon, leftSon.getRight(),2)))) {
					   this.rotate(parent, 'R');
					   rebalanceCount+=3;
				   }
				   // DELETE LEFT CASE 4 -> Double Rotate
				   else if((isHeightDiff(leftSon, leftSon.getLeft(),2)) &&
					(isHeightDiff(leftSon, leftSon.getRight(),1))) {
					   this.doubleRotate(parent, "LR");
					   rebalanceCount+=6;
				   }
			   }  
		   }
		   son = parent;
		   parent = parent.getParent();
	   }
	// Update all size fields from the inserted node upto the root
	   updateSizesfromNode(startDeleteNode);
	   return rebalanceCount;
   }
   
   /**
    * private static void updateParent(IAVLNode parent)
    *
    * Receives a node and updates its height and size 
    * 
    * time complexity: O(1)
    */
	private static void updateParent(IAVLNode parent) {
		parent.updateHeight();
		parent.updateSize();
	}
	
	/**
	    * private void rotate(IAVLNode oldSubRoot, char dir)
	    *
	    * Receives a node which is the root of the sub-tree 
	    * before rotating and character for the rotation's direction
	    * and rotates the sub-tree to that direction. 
	    * 
	    * time complexity: O(1)
	    */
   private void rotate(IAVLNode oldSubRoot, char dir) {
	   IAVLNode tmp = null;
	   // newSubRoot will be the new root of the sub-tree after the rotation
	   IAVLNode newSubRoot;
	   if (dir == 'R') {
		   newSubRoot = oldSubRoot.getLeft();
		   tmp = newSubRoot.getRight();
		   //tmp is newSubRoot son in the direction of the rotation
		   if(oldSubRoot.getParent() != null) {
			   if(oldSubRoot.isRightSon())
				   oldSubRoot.getParent().connectSon(newSubRoot, 'R');
			   else
				   oldSubRoot.getParent().connectSon(newSubRoot, 'L');
		   }
		   else //oldSubRoot is the root of the tree
			   newSubRoot.setParent(oldSubRoot.getParent());
		   newSubRoot.connectSon(oldSubRoot, 'R');
		   oldSubRoot.connectSon(tmp, 'L');
		   updateHighetsAfterRotation(newSubRoot, 'R');
		   updateSizesAfterRotation( newSubRoot, 'R');
	   }
	   else{
		   newSubRoot = oldSubRoot.getRight();
		   tmp = newSubRoot.getLeft();
		   if(oldSubRoot.getParent() != null) {
			   if(oldSubRoot.isRightSon())
				   oldSubRoot.getParent().connectSon(newSubRoot, 'R');
			   else
				   oldSubRoot.getParent().connectSon(newSubRoot, 'L');
		   }
		   else
			   newSubRoot.setParent(oldSubRoot.getParent());
		   newSubRoot.connectSon(oldSubRoot, 'L');
		   oldSubRoot.connectSon(tmp, 'R');
		   updateHighetsAfterRotation(newSubRoot, 'L');
		   updateSizesAfterRotation( newSubRoot, 'L');
	   }
	   if(this.getRoot().getParent()!=null)
		   this.setRoot(this.getRoot().getParent());
   }
   
   /**
    * private void doubleRotate(IAVLNode oldSubRoot, String doubleDir) 
    *
    * Receives a node which is the root of the sub-tree 
    * before rotating and string of 2 characters
    * indicating the rotations direction
    * and rotates double rotation. 
    * 
    * time complexity: O(1)
    */
   private void doubleRotate(IAVLNode oldSubRoot, String doubleDir) {
	   if(doubleDir=="LR") {
		   rotate(oldSubRoot.getLeft(),'L');
		   rotate(oldSubRoot,'R');
	   }
	   if(doubleDir=="RL") {
		   rotate(oldSubRoot.getRight(),'R');
		   rotate(oldSubRoot,'L');
	   }
   }
   
   /**
    * private static void updateHighetsAfterRotation(IAVLNode parent, char dir) 
    *
    * Receives a node which is the root of the sub-tree 
    * after rotation and a character indicating the direction
    * of the son of which the height need to be updated.
    * Updates the height of both the parent and the son.
    * 
    * time complexity: O(1)
    */
   private static void updateHighetsAfterRotation(IAVLNode parent, char dir) {
	   IAVLNode son;
	   if (dir=='R') {
		   son = parent.getRight();
	   }
	   else {
		   son = parent.getLeft();
	   }
	   son.updateHeight();
	   parent.updateHeight();
   }
   
   /**
    * private static void updateSizesAfterRotation(IAVLNode newSubRoot, char dir) 
    *
    * Receives a node which is the root of the sub-tree 
    * after rotation and a character indicating the direction
    * of the son of which the size need to be updated.
    * Updates the size of both the parent and the son.
    * 
    * time complexity: O(1)
    */
   private static void updateSizesAfterRotation(IAVLNode newSubRoot, char dir) {
	   if (dir=='R') {
		   newSubRoot.getRight().updateSize();   
	   }
	   else{
		   newSubRoot.getLeft().updateSize(); 
	   }
	   newSubRoot.updateSize();
   }
   
   /**
    * private static void updateSizesfromNode(IAVLNode startNode)
    *
    * Receives a node and updates its size and all the sizes 
    * of nodes in the route from it upto the root.
    * 
    * time complexity: O(1)
    */
   private static void updateSizesfromNode(IAVLNode startNode) {
	   IAVLNode node = startNode;
	   while(node!=null) {
		   if(node.isRealNode())
			   node.updateSize();
		   node = node.getParent();
	   }
   } 
   
   /**
    * private static int heightDiff(IAVLNode parent, IAVLNode son)
    *
    * Receives 2 nodes and returns the height difference between them
    * 
    * time complexity: O(1)
    */
   private static int heightDiff(IAVLNode parent, IAVLNode son) {
	   return parent.getHeight() - son.getHeight();
   }
   
   /**
    * private static boolean isHeightDiff(IAVLNode parent, IAVLNode son, int num)
    *
    * Receives 2 nodes and integer 
    * and returns true iff the height difference between them is the integer
    * 
    * time complexity: O(1)
    */
   private static boolean isHeightDiff(IAVLNode parent, IAVLNode son, int num) {
	   return (parent.getHeight() - son.getHeight()) == num;
   }
   
   /**
    * private static int rank(AVLTree tree)
    *
    *if the tree is empty returns -1
    *otherwise, returns the root height
    * 
    * time complexity: O(1)
    */
   private static int rank(AVLTree tree) {
	   if (tree.empty()) return -1;
	   return tree.getRoot().getHeight();
   }
  

  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k was not found in the tree.
   * 
   * time complexity: O(log(n))
   */
   public int delete(int k)
   {
	   //This will be the number of balancing operations
	   int deleteVal = 0;
	   // deleteNode is the node we want to delete
	   IAVLNode deleteNode = searchNode(k);
	   IAVLNode deleteNodeSon;
	   // The key we want to delete is not in the tree
	   if(deleteNode == null)
		   return -1;
	   deleteUpdateMinMax(k);
	   boolean deleteNodeHasLeft = deleteNode.whichSons()[0];
	   boolean deleteNodeHasRight = deleteNode.whichSons()[1];
	   IAVLNode deleteNodeParent = deleteNode.getParent();
	   if (!(deleteNodeHasLeft && deleteNodeHasRight)){ // deleteNode has 0 or 1 child
		   if(deleteNodeHasRight)
			   deleteNodeSon = deleteNode.getRight();  
		   else
			   deleteNodeSon = deleteNode.getLeft();
		   if(deleteNodeParent != null) { //deleteNode is not root
			   if(deleteNode.isRightSon())
				   deleteNodeParent.connectSon(deleteNodeSon, 'R');
			   else
				   deleteNodeParent.connectSon(deleteNodeSon, 'L');
			   // In case of 0 or 1 child, the balance starts with the
			   // son of the deleteNode (if 0, then it is virtual)
			   deleteVal = rebalanceAfterDelete(deleteNodeSon);
		   }
		   else { //deleteNode is root
			   deleteNodeSon.setParent(deleteNodeParent);
			   if(deleteNodeSon.isRealNode()) //the root has 1 non-virtual children
				   this.setRoot(deleteNodeSon);
			   else // the root has 0 non-virtual children
				   this.setRoot(null);
		   }
	   }
	   else { //deleteNode has 2 children
		   IAVLNode deleteNodeSucc = successor(deleteNode);
		   IAVLNode toBalance = deleteNodeSucc.getRight();
		   IAVLNode deleteNodeSuccPar = deleteNodeSucc.getParent();
		   //"Disconnect" the successor of deleteNode
		   if(deleteNodeSucc.isRightSon())
			   deleteNodeSuccPar.connectSon(deleteNodeSucc.getRight(), 'R');
		   else
			   deleteNodeSuccPar.connectSon(deleteNodeSucc.getRight(), 'L');
		   if (deleteNodeParent != null) {
			   if(deleteNode.isRightSon())
				   deleteNodeParent.connectSon(deleteNodeSucc, 'R');
			   else
				   deleteNodeParent.connectSon(deleteNodeSucc, 'L');
		   }
		   else {
			   deleteNodeSucc.setParent(deleteNodeParent);
			   this.setRoot(deleteNodeSucc);
		   } //Replace deleteNode with its successor 
		   deleteNodeSucc.connectSon(deleteNode.getRight(), 'R');
		   deleteNodeSucc.connectSon(deleteNode.getLeft(), 'L');
		   deleteNodeSucc.updateHeight();
		   // In case of 2 children, the balances starts with
		   // the node which was the succesor's son before "disconnecting"
		   deleteVal = rebalanceAfterDelete(toBalance);
	   }
	   return deleteVal;	
   }
   

   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    * 
    * time complexity: O(1)
    */
   public String min()
   {
	   if (this.empty()) return null;
	   return this.min.getValue();
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    * 
    * time complexity: O(1)
    */
   public String max()
   {
	   if (this.empty()) return null;
	   return this.max.getValue();   
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   * 
   * time complexity: O(n)
   */
   public int[] keysToArray()
   {
	   if (this.empty()) return new int[0];//return an empty array
	   int []arr = new int[this.size()];
	   keysToArrayRec(this.getRoot(), arr, 0);//fills the array
	   return arr; 
   }
   
   /**
    * private int keysToArrayRec(IAVLNode node, int[] arr, int index)
    *
    * a recursive method that fill the given array with the keys of the tree's nodes,
    * starting from the smallest key all the way to the maximum key.
    * the function uses the returned index in order to place the key in the right index in the array. 
    *
    * time complexity: O(n)
    */
   private int keysToArrayRec(IAVLNode node, int[] arr, int index)
   {
	   if(!node.isRealNode()) return index;
	   index = keysToArrayRec(node.getLeft(),arr, index); //recur the left subtree
	   arr[index] = node.getKey(); //insert the node's key
	   index ++;
	   index = keysToArrayRec(node.getRight(),arr,index); //recur the right subtree
	   return index;
   }
 

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   * 
   * time complexity: O(n)
   */
   public String[] infoToArray()
   {
	  if (this.empty()) return new String[0];//return an empty array
	  String []arr = new String[this.size()];
	  infoToArrayRec(this.getRoot(), arr, 0);//fills the array
	  return arr;                  
  }
  
   /**
    * private int infoToArrayRec(IAVLNode node, String[] arr, int index)
    *
    * a recursive method that fill the given array with the values of the tree's nodes,
    * starting from the value of the node with the smallest key, all the way to the maximum key.
    * the function uses the returned index in order to place node's value in the right index in the array. 
    *
    * time complexity: O(n)
    */
  private int infoToArrayRec(IAVLNode node, String[] arr, int index)
  {
	   if(!node.isRealNode()) return index;
	   index = infoToArrayRec(node.getLeft(),arr, index); //recur the left subtree
	   arr[index] = node.getValue();//insert the node's key
	   index ++;
	   index = infoToArrayRec(node.getRight(),arr,index); //recur the right subtree
	   return index;
  }  

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    * 
    * time complexity: O(1)
    */
   public int size()
   {
	   if(this.empty()) return 0;
	   return this.getRoot().getSize();
   }
   
   /**
    * public IAVLNode getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    * 
    * time complexity: O(1)
    */
   public IAVLNode getRoot()
   {
	   return this.root;
   }
   
   /**
    * private void setRoot(IAVLNode node)
    *
    * updates the root AVL node with given node
    * 
    * time complexity: O(1)
    */
   private void setRoot(IAVLNode node) 
   {
	   this.root = node;
   }
     /**
    * public AVLTree[] split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	* precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
    * postcondition: none
    * 
    * time complexity: O(log(n))
    */   
   public AVLTree[] split(int x)
   {
	   IAVLNode splitNode = treePosition(x,this.getRoot());
	   AVLTree t1 = new AVLTree();
	   AVLTree t2 = new AVLTree();
	   t1.setRoot(splitNode.getLeft());//initialize the smaller keys tree
	   t2.setRoot(splitNode.getRight());//initialize the bigger keys tree
	   IAVLNode []small = new IAVLNode[rank(this)];
	   IAVLNode []large = new IAVLNode[rank(this)];
	   
	   //update the min and max values of the trees
	   IAVLNode min1 = null, max1 = null, min2 = null, max2 = null;
	   if (this.min.getKey() < x) {
		   min1 = this.min;
		   max1 = this.predecessor(splitNode);
	   }
	   if (this.max.getKey() > x) {
		   min2 = this.successor(splitNode);
		   max2 = this.max;
	   }
	   
	   //store all the sub-trees according to their keys in the suitable array
	   IAVLNode currNode = splitNode;
	   int s = 0,l = 0;
	   while (currNode.getParent() != null) {
		   if (currNode.isRightSon()) {
			   small[s] = currNode.getParent();
			   s++;
		   }
		   else {
			   large[l] = currNode.getParent();
			   l++;
		   }
		   currNode = currNode.getParent();
	   }
	   
	   //create the smaller keys tree
	   for (int i = 0; i < s; i++){
		   AVLTree add = new AVLTree();
		   add.setRoot(small[i].getLeft());
		   add.min = min1;
		   add.max = max1;
		   t1.join(small[i],add);
	   }
	   //create the bigger keys tree
	   for (int i = 0; i < l; i++){
		   AVLTree add = new AVLTree();
		   add.setRoot(large[i].getRight());
		   add.min = min2;
		   add.max = max2;
		   t2.join(large[i],add);
	   }
	   t1.min = min1;
	   t1.max = max1;
	   t2.min = min2;
	   t2.max = max2;
	   t1.getRoot().setParent(null);
	   t2.getRoot().setParent(null);
	   AVLTree []retVal = {t1,t2};
	   return retVal;
   }
   /**
    * public int join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	* precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
    * 
    * time complexity: O(log(n))
    */   
   public int join(IAVLNode x, AVLTree t)
   {
	   int complexity = Math.abs(rank(this)-rank(t)) + 1;
	   
	   //cases one of the trees is empty
	   if (t.empty() || this.empty()) {
		   if (t.empty()) {
			   this.insert(x.getKey(), x.getValue());
			   this.getRoot().setParent(null);
		   }
		   else if (this.empty()) {
			   t.insert(x.getKey(), x.getValue());
			   this.setRoot(t.getRoot());
			   this.getRoot().setParent(null);
			   this.min = t.min;
			   this.max = t.max;
		   }
		   
		   return complexity;
	   }
	   
	   AVLTree large,small;
	   int hd = heightDiff(this.getRoot(), t.getRoot());
	   IAVLNode left, right, min, max;
	   if (hd > 0) {
		   large = this;
		   small = t;
		}
	   else
	   {
		   //two trees has the same height
		   if (hd == 0) {
			   if (t.getRoot().getKey() < this.getRoot().getKey()) {
				   min = t.min;
				   max = this.max;
				   left = t.getRoot();
				   right = this.getRoot();
				}
			   else {
				   min = this.min;
				   max = t.max;
				   left = this.getRoot();
				   right = t.getRoot();
				}
			   x.setLeft(left);
			   left.setParent(x);
			   x.setRight(right);
			   right.setParent(x);
			   x.updateSize();
			   x.setHeight(rank(t) + 1);
			   this.setRoot(x);
			   this.getRoot().setParent(null);
			   this.min = min;
			   this.max = max;
			   
			   return complexity;
			}
		   large = t;
		   small = this;
		}
	   
	   //trees have different heights
	   x.setHeight(rank(small) + 1);
	   IAVLNode currNode = large.getRoot(), parent = currNode;
	   if (large.getRoot().getKey() < small.getRoot().getKey()) {
		   min = large.min;
		   max = small.max;
		   //find the first right node which its height is smaller then the height of the shorter tree
		   while (rank(small) < currNode.getHeight()) {
			   parent = currNode;
			   currNode = currNode.getRight();
		   }
		   parent.setRight(x);
		   x.setParent(parent);
		   left = currNode;
		   right = small.getRoot();
	   }
	   else {
		   min = small.min;
		   max = large.max;
		   //find the first left node which its height is smaller then the height of the shorter tree
		   while (rank(small) < currNode.getHeight()) {
			   parent = currNode;
			   currNode = currNode.getLeft();
		   }
		   parent.setLeft(x);
		   x.setParent(parent);
		   left = small.getRoot();
		   right = currNode;
	   }
	   x.setLeft(left);
	   left.setParent(x);
	   x.setRight(right);
	   right.setParent(x);
	   this.setRoot(large.getRoot());
	   this.getRoot().setParent(null);
	   this.rebalanceAfterInsert(x);
	   this.min = min;
	   this.max = max;
	   
	   return complexity;
	   
   }

	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
	
    	public int getSize();
    	public void updateSize();
    	public void updateHeight();
    	public boolean isRightSon();
    	public boolean[] whichSons();
    	public void connectSon(IAVLNode son, char dir);
	
	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{
	  private int key;
	  private String info;
	  private IAVLNode left;
	  private IAVLNode right;
	  private IAVLNode parent;
	  private boolean isRealNode;
	  private int height;
	  private int size;
	  
	  /**
	   * public AVLNode(int key, String info)
	   *
	   * constructor - creates real node
	   * 
	   * time complexity: O(1)
	   *
	   */
	  public AVLNode(int key, String info) {
		  this.isRealNode = true;
		  this.key = key;
		  this.info = info;
		  this.left = new AVLNode();
		  this.right = new AVLNode();
		  this.size = 1;
		  this.height = 0;
	  }
	  
	  /**
	   * public AVLNode()
	   *
	   * constructor - creates virtual node
	   * 
	   * time complexity: O(1)
	   *
	   */
	  public AVLNode() {
		  this.isRealNode = false;
		  this.key = -1;
		  this.info = null;
		  this.left = null;
		  this.right = null;
		  this.size = 0;
		  this.height = -1;
	  }
	  
	  /**
	    * public int getKey()
	    *
	    * returns the key of the node, or null if the node is virtual
	    * 
	    * time complexity: O(1)
	    */
		public int getKey()
		{
			return this.key;
		}
		
		/**
		* public String getValue()
		*
		* returns the info of the node, or null if the node is virtual
		* 
		* time complexity: O(1)
		*/
		public String getValue()
		{
			return this.info;
		}
		
		/**
		* public void setLeft(IAVLNode node)
		*
		* updates the left son of the node
		* 
		* time complexity: O(1)
		*/
		public void setLeft(IAVLNode node)
		{
			if(isRealNode())
				this.left = node; 
		}
		
		/**
		* public IAVLNode getLeft()
		*
		* returns the left son of the node
		* 
		* time complexity: O(1)
		*/
		public IAVLNode getLeft()
		{
			return this.left;
		}
		
		/**
		* public void setRight(IAVLNode node)
		*
		* updates the right son of the node
		* 
		* time complexity: O(1)
		*/
		public void setRight(IAVLNode node)
		{
			if(isRealNode())
				this.right = node;
		}
		
		/**
		* public IAVLNode getRight()
		*
		* returns the right son of the node
		* 
		* time complexity: O(1)
		*/
		public IAVLNode getRight()
		{
			return this.right;
		}
		
		/**
		* public void setParent(IAVLNode node)
		*
		* updates the parent of the node
		* 
		* time complexity: O(1)
		*/
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}
		
		/**
		* public IAVLNode getParent()
		*
		* returns the parent of the node
		* 
		* time complexity: O(1)
		*/
		public IAVLNode getParent()
		{
			return this.parent;
		}
		
		/**
		* public boolean isRealNode()
		*
		* returns True if this is a non-virtual AVL node
		* 
		* time complexity: O(1)
		*/
		public boolean isRealNode()
		{
			return this.isRealNode;
		}
		
		/**
		* public void setHeight(int height)
		*
		* updates the height of the node
		* 
		* time complexity: O(1)
		*/
	    public void setHeight(int height)
	    {
	    	this.height = height;
	    }
	    
	    /**
		* public void updateHeight()
		*
		* updates the height of the node according the node's sons' height
		* 
		* time complexity: O(1)
		*/
	    public void updateHeight() {
	    	this.height = Math.max(this.getLeft().getHeight(), this.getRight().getHeight()) + 1;
	    }
	    
	    /**
		* public int getHeight()
		*
		* returns the height of the node 
		* 
		* time complexity: O(1)
		*/
	    public int getHeight()
	    {
	    	return this.height;
	    }
	    
	    /**
		* public int getSize()
		*
		* returns the size of the subtree that its root is the node 
		* 
		* time complexity: O(1)
		*/
	    public int getSize()
	    {
			return this.size;
	    }
	    
	    /**
		* public void updateSize()
		*
		* updates the size of the subtree that its root is the node according the node's sons' height
		* 
		* time complexity: O(1)
		*/
	    public void updateSize()
	    {
	    	this.size = this.getLeft().getSize() + this.getRight().getSize() + 1;		
	    }
	    
	    /**
		* public boolean isRightSon()
		*
		* returns True if this is a right son to its parent
		* 
		* time complexity: O(1)
		*/
		public boolean isRightSon() {
			return this.getParent().getRight() == this;
		}
		
		/**
		* public boolean[] whichSons()
		*
		* returns array that contains if node has real left son and real right son
		* 
		* time complexity: O(1)
		*/
		public boolean[] whichSons() {
			boolean hasLeft = this.getLeft().isRealNode();
			boolean hasRight = this.getRight().isRealNode();
			boolean[] sons = new boolean[] {hasLeft, hasRight};
			return sons; 
		}
		
		/**
		* public void connectSon(IAVLNode son, char dir)
		*
		* gets son and direction and make this node its parent according to the direction
		* 
		* time complexity: O(1)
		*/
		public void connectSon(IAVLNode son, char dir) {
			if (dir=='R')
				this.setRight(son);
			else
				this.setLeft(son);
			son.setParent(this);
		}
	    
  }

}
