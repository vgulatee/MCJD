package sort;

import search.Query;

public class SortController {

	public SortController(){
	
		long now;
		long start;
		start = System.currentTimeMillis();
		JargonSort.sort();
		now = System.currentTimeMillis();
        System.out.println((now - start) / 1000.0);
	}
		
}
