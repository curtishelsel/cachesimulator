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

		if(m.getOperationType().equals("W")){
			checkWritePolicy();
		}
	}

	public void addFifo(MemoryAccess m){
	
		for(int i = 1; i < cache.getAssoc(); i++){
			cache.getCacheArray()[m.getSetNumber()][i-1] = cache.getCacheArray()[m.getSetNumber()][i];
		}		
			
		cache.getCacheArray()[m.getSetNumber()][cache.
			getCacheArray()[m.getSetNumber()].length-1] = m.getTag();

		if(m.getOperationType().equals("W")){
			checkWritePolicy();
		}
	}

	void checkWritePolicy(){
		if(cache.getWBPolicy()){
			read++;
		}
		else{
			write++;
		}
	}

	public void simulate(MemoryAccess m){
		
		for(int i = 0; i < cache.getAssoc(); i++){
			
			if(m.getTag().equals(cache.getCacheArray()[m.getSetNumber()][i])){
			
				hit++;
				if(cache.getRPolicy().equals("LRU")){
					LRU(i, m.getSetNumber());
				}

				if(m.getOperationType().equals("W")){
					checkWritePolicy();
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

	public void printStatistics(){
		
		System.out.println((double) miss/(miss + hit));
		System.out.println(write);
		System.out.println(read);
	}

	public static void main(String[] args){
		
		if(args.length != 5){
			System.out.println("Please provide the correct amount of arguements");
			return;
		}

		CacheSimulator cs = new CacheSimulator(args);

		cs.printStatistics();
	}
}
