package sort;

import java.util.Arrays;

public class QuickThreeString {

	private static int depth = 0;
	
	public static void main(String[] args){
		
		String[] a = new String[]{"antipathy", "boo", "chow", "theme", "boo", "tea", "she", "sells", "antigen", "seashells", "by", "the", "antibody", "sea", "shore", "the", "antibodies", "shells", "she", "sells", "antioxidant", "are", "surely", "seashells"};		
		System.out.println("Pre:  " + Arrays.toString(a));
		sortComparable(a, 0, a.length-1, 0);
		System.out.println("Post: " + Arrays.toString(a));
		System.out.println("Input size: " + a.length);
		System.out.println(depth + " recursive sort calls");		
	}
	
	public static void sortComparable(String[] a, int lower, int upper, int pos){
		
		if (lower >= upper) return;		
		depth++;
				
		
		/* 
		 * Inputs
		 * a[lower..upper] is the input list to be sorted
		 * lower is the start index of the sublist to be sorted
		 * upper is the end index of the sublist to be sorted
		 * 
		 */
		
		int pivotValue = getChar(a[lower],pos);				//Use first element of sublist as pivot value 
		int pivotIndex = lower;								//Keeps track of pivot element (or the start index for values equal to pivot)
		int end = upper;									//Keeps track of start index for values greater 		
		
		int index = lower + 1;		//Start scan on element at index 1		
				
		
		while (index <= end){
			
			int current = getChar(a[index], pos);
			
			if (current < pivotValue){
				exchange(a, pivotIndex, index);				//Put the smaller element in front of the pivot element
				index++;									//Increment index to check next element
				pivotIndex++;								//Keep start as the index for the pivot element
			} else if (current > pivotValue){
				exchange(a, index, end);					//Put the bigger element at the end of the list
				end--;										//Set upper bound to the next highest index
			} else {
				index++;
			}	
/*			System.out.println(current);
			System.out.println(pivotIndex);
			System.out.println(index);
			System.out.println(end);
			System.out.println(Arrays.toString(a));
*/		}		
		
		
		/*
		 * Post-values
		 * pivotIndex - the start index for the section containing values equal to the pivot element
		 * end - the start index for the section containing values greater than the pivot element
		 * a[lower..pivotIndex-1] - values less than the pivot element
		 * a[pivotIndex..end] - values equal to the pivot element
		 * a[end+1..upper] - values greater than the pivot element (end+1 because it will be decremented one extra time before completion)
		 * 
		 */
		
		sortComparable(a, lower, pivotIndex-1, pos);
		if (pivotValue >= 0) sortComparable(a, pivotIndex, end, pos+1);
		sortComparable(a, end+1, upper, pos);
		
	}		
	
	private static int getChar(String str, int i){
		if (i >= str.length()) return -1;
		else return str.charAt(i);
	}
	
	private static void exchange(String[] a, int index1, int index2){
		String temp = a[index1];
		a[index1] = a[index2];
		a[index2] = temp;		
	}

	private static boolean isSorted(Comparable[] a, int length){
		
		if (length == 1) return true;
		
		for (int i = 1; i < length; i++){
			if (a[i].compareTo(a[i-1]) < 0) return false;
		}
		
		return true;
	}
	
}