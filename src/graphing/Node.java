package graphing;

import java.util.ArrayList;
import java.util.LinkedList;

public class Node {
	
	private final char value;												//Character value of the node
	private LinkedList<Node> nextChar = new LinkedList<Node>();				//List of child nodes
	private boolean terminal = false;										//Whether or not there is a valid string ending with this character
	private boolean beginning;												//Whether or not this is a starting Node for a string
	private static int numberOfNodes = 0;									//Number of nodes created
	private static ArrayList<Node> nodeList = new ArrayList<Node>();
	private final int ID;
	private String res = "";
	private String pre = "";	
	
	/**
	 * Creates an empty Node
	 */
	public Node(){
		this.value = ' ';
		this.ID = -1;
	}
	
	/**
	 * @param value the character value of this Node
	 */
	public Node(char value){
		this.value = value;
		this.ID = Node.numberOfNodes;
		nodeList.add(this);
		Node.numberOfNodes++;
	}
	
	/**
	 * @param value the character value of this Node
	 */
	public Node(char value, String str){
		this.value = value;
		this.ID = Node.numberOfNodes;
		this.res = str;
		nodeList.add(this);
		Node.numberOfNodes++;
	}

	public void setEnd(String eos){
		this.res = eos;
	}
	
	public String getEnd(){
		return this.res;
	}
	
	public void setPre(String bos){
		this.pre = bos;
	}
	
	public String getPre(){
		return this.pre;
	}
	
	public static void resetNodes(){
		nodeList = new ArrayList<Node>();
		numberOfNodes = 0;
	}
	
	/**
	 * @return the unique numerical ID of the Node
	 */
	public int getID() {
		return ID;
	}


	/**
	 * @return the character value of this Node
	 */
	public char getValue() {
		return value;
	}

	/**
	 * @return the list of subsequent Nodes
	 */
	public LinkedList<Node> getChild() {
		return nextChar;
	}

	/**
	 * @param nextChar the list of subsequent Nodes
	 */
	public void setNextChar(LinkedList<Node> nextChar) {
		this.nextChar = nextChar;
	}

	/**
	 * @return true if a string ends at this Node, otherwise false
	 */
	public boolean isTerminal() {
		return terminal;
	}

	/**
	 * @param terminal whether or not a string ends at this Node
	 */
	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
	
	/**
	 * @return true if a string begins at this Node, otherwise false
	 */
	public boolean isBeginning() {
		return beginning;
	}

	/**
	 * @param terminal whether or not a string ends at this Node
	 */
	public void setBeginning(boolean beginning) {
		this.beginning = beginning;
	}
	
	/**
	 * @param val the character value to look for
	 * @return	whether or not the next character is equal to val
	 */
	public boolean containsChild(Node val) {
		return nextChar.contains(val);		
	}
	
	/**
	 * @param val the child Node (subsequent character) to add
	 */
	public void addChildNode(Node val){
		if (!nextChar.contains(val)){
			nextChar.add(val);
		}
	}
	
	/**
	 * @param val the character value to search for
	 * @return the child Node containing this character, otherwise null 
	 */
	public Node getNode(Node val){
		int index = nextChar.indexOf(val);
		if (index != -1) return nextChar.get(index);
		else return null;
	}
	
	public Node getNode(char ch){
		for (Node n : this.getChild())
			if (n.getValue() == ch) return n;
		return null;
	}
	
	/**
	 * @return the number of Nodes currently active
	 */
	public static int count(){
		return numberOfNodes;
	}
	
	/**
	 * @param index the ID of the Node to be found
	 * @return Node with the given ID
	 */
	public static Node getNode(int index){
		return nodeList.get(index);
	}
	
/*	public static boolean compareID(Node a, Node b){
		return a.getID() == b.getID();
	}*/
	
	
	/* (non-Javadoc)
	 * Overrides the equals method in Object so that the Nodes can be compared by their character value
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other){
		Node second = (Node) other;
		//return this.getID() == second.getID();
		return (this.getValue() == second.getValue()) || (this.getID() == second.getID());
	}
}
