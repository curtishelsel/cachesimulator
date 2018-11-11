/*	Curtis Helsel
	EEl4768 - Fall 2018
	November 3, 2018

	This program simulates a cache using the least recently used
	and first in first out replacement policies.
*/

import java.util.*;
import java.io.*;
import java.math.*;

public class CacheSimulator{

	private Cache cache;
	private int hit;
	private int miss;
	private int write;
	private int read;

	public CacheSimulator (String[] args){

		cache = new Cache(args[0], args[1], args[2], args[3]);
		
		try{

			Scanner in = new Scanner(new File(args[4]));
			
			while(in.hasNext()){

				String op = in.next();
				String add = in.next();
				MemoryAccess m = new MemoryAccess(op, add);
				
				m.setSetNumber(cache.getBlockSize());
				m.setTag(cache.getNumSets());
			
				simulate(m);
			}
		}
		catch(FileNotFoundException ex){
			System.out.println(ex.toString());
		}
	}

	public void updateCache(int setNumber, BigInteger tag, String op){
		
		
		for(int i = 0; i < cache.getAssoc(); i++){
				if(cache.getCacheArray()[setNumber][i] == null){
					cache.getCacheArray()[setNumber][i] = tag;
					
					if(cache.getRPolicy().equals("LRU")){
						updateLRU(setNumber);
					}

					if(op.equals("W")){
						cache.getDirty()[setNumber][i] = 1;		
					}
					else{
						cache.getDirty()[setNumber][i] = 0;
					}

					return;
				}
		}

		if(cache.getRPolicy().equals("FIFO")){
			

			if(cache.getDirty()[setNumber][0] == 1){
				write++;
			}
			for(int i = 1; i < cache.getAssoc(); i++){
				cache.getCacheArray()[setNumber][i-1] = cache.getCacheArray()[setNumber][i];
				cache.getDirty()[setNumber][i-1] = cache.getDirty()[setNumber][i];
			}		
			
			cache.getCacheArray()[setNumber][cache.getCacheArray()[setNumber].length-1] = tag;
			
			if(cache.getWBPolicy() && op.equals("W")){
				
			}

		}
		if(cache.getRPolicy().equals("LRU")){
		
			int index = updateLRU(setNumber);

			if(cache.getDirty()[setNumber][index] == 1){
				write++;
			}
			cache.getCacheArray()[setNumber][index] = tag;

			if(op.equals("W")){
				cache.getDirty()[setNumber][index] = 1;
			}
			else{
				cache.getDirty()[setNumber][index] = 0;
			}

		}
	}

	public int updateLRU(int setNumber){
	
		int index = 0;
		int min = (int) cache.getAssoc();

		for(int i = 0; i < cache.getAssoc(); i++){
			if(cache.getReplace()[setNumber][i] < min){
				min = cache.getReplace()[setNumber][i];
				index = i;
			}
			cache.getReplace()[setNumber][i]--;
		}

		cache.getReplace()[setNumber][index] = (int) cache.getAssoc() - 1;

		if(setNumber == 6){
			for(int i = 0; i < cache.getNumSets(); i++){

		    	for(int j = 0; j < cache.getAssoc(); j++){
		    		System.out.print(cache.getReplace()[i][j] + " "); 
				}
				System.out.println();
        	}
		}
		return index;
		
	}

	public void LRU(int index, int setNumber){

		for(int j = 0; j < cache.getAssoc(); j++){
			cache.getReplace()[setNumber][j]--;
		}	
		
		cache.getReplace()[setNumber][index] = cache.getReplace()[setNumber].length - 1;
		

	}
	
	public void simulate(MemoryAccess m){
		
		int setNumber = m.getSetNumber();

		for(int i = 0; i < cache.getAssoc(); i++){
			
			if(m.getTag().equals(cache.getCacheArray()[setNumber][i])){
			
				hit++;

				if(cache.getRPolicy().equals("LRU")){
					LRU(i, setNumber);
				}

				//dirty bit
				return;
			}
		}
			
		miss++;
		read++;
		updateCache(setNumber, m.getTag(), m.getOperationType());
	}

	public static void main(String[] args){
		
		if(args.length != 5){
			System.out.println("Please provide the correct amount of arguements");
			return;
		}

		CacheSimulator cs = new CacheSimulator(args);

		


		System.out.println(cs.write);
		//print statistics
	}
}
