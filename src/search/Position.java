package search;

public class Position {

	private static int number = 0;
	private final String id;
	private final int start;
	private final int end;
	private final int num;
	private final int numH;
	private final int numQ;	

	public Position(String ID, int s, int e, int h, int q){
		
		this.num = number;
		this.id = ID.intern();				//Might not need this field
		this.start = s;
		this.end = e;
		this.numH=h;
		this.numQ=q;

		number += 1;
	}

	public int getStart(){
		return start;
	}
	
	public int getS(){
		return start-start;
	}
	
	public int getH(){
		return (numH-start);
	}
	
	public int getQ(){
		return (numQ-start);
	}
	
	public int getE(){
		return (end-start);
	}
	
	public int getEnd(){
		return end;
	}

	public String ID(){
		return id.intern();
	}

	public int getNum(){
		return this.num;
	}

	public static int getTotal(){
		return number;
	}

	public String toString(){
		return id + " " + start + " " + end;
	}

}
