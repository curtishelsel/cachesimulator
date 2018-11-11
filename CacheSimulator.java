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
						//updateLRU(setNumber);
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
						if(cache.getWBPolicy() && op.equals("W")){
				
			}

		}
	}

	public void LRU(int index, int setNumber){

		for(int j = 0; j < cache.getAssoc(); j++){
			cache.getReplace()[setNumber][j]--;
		}	
		
		cache.getReplace()[setNumber][index] = cache.getReplace()[setNumber].length - 1;
	}
	

	public void addLRU(MemoryAccess m){
		
		int index = 0;
		int min = (int) cache.getAssoc();

		for(int i = 0; i < cache.getAssoc(); i++){
			if(cache.getReplace()[m.getSetNumber()][i] < min){
				min = cache.getReplace()[m.getSetNumber()][i];
				index = i;
			}
		}

		LRU(index, m.getSetNumber());

		cache.getCacheArray()[m.getSetNumber()][index] = m.getTag();

	}

	public void addFifo(MemoryAccess m){
	
		for(int i = 1; i < cache.getAssoc(); i++){
			cache.getCacheArray()[m.getSetNumber()][i-1] = cache.getCacheArray()[m.getSetNumber()][i];
		}		
			
		cache.getCacheArray()[m.getSetNumber()][cache.getCacheArray()[m.getSetNumber()].length-1] = m.getTag();

	}

	public void simulate(MemoryAccess m){
		
		for(int i = 0; i < cache.getAssoc(); i++){
			
			if(m.getTag().equals(cache.getCacheArray()[m.getSetNumber()][i])){
			
				hit++;

				if(cache.getRPolicy().equals("LRU")){
					LRU(i, m.getSetNumber());
				}

				//dirty bit
				return;
			}
		}
			
		miss++;
		read++;

		if(cache.getRPolicy().equals("LRU")){
			addLRU(m);
		}

		if(cache.getRPolicy().equals("FIFO")){
			addFifo(m);
		}
	}

	public static void main(String[] args){
		
		if(args.length != 5){
			System.out.println("Please provide the correct amount of arguements");
			return;
		}

		CacheSimulator cs = new CacheSimulator(args);

		System.out.println((double) cs.miss/(cs.miss + cs.hit));
		//print statistics
	}
}
