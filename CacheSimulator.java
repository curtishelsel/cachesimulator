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

	// Constructor that reads from the file and calls to simulate the cache
	// with every memory access.
	public CacheSimulator (String[] args){

		cache = new Cache(args[0], args[1], args[2], args[3]);
		
		try{
			Scanner in = new Scanner(new File(args[4]));
			
			while(in.hasNext()){

				// For each line, pull the operation and address and
				// create a new memory access object.
				String op = in.next();
				String add = in.next();
				MemoryAccess m = new MemoryAccess(op, add);
				
				// Set the set number and tag for each memory access.
				m.setSetNumberAndTag(cache.getBlockSize(), cache.getNumSets());
			
				simulate(m);
			}
		}
		catch(FileNotFoundException ex){
			System.out.println(ex.toString());
		}
	}

	// Method updates the least recently used array base on
	// index being changed.
	public void LRU(int index, int setNumber){

		// Decrement all of the indices recently used place
		for(int j = 0; j < cache.getAssoc(); j++){
			cache.getReplace()[setNumber][j]--;
		}
		
		// Set the index to the highest recently used place 
		// to be last in line for changes.
		cache.getReplace()[setNumber][index] = cache.getReplace()[setNumber].length - 1;
	}

	// Method to add memory access based on least recently used
	// replacement policy.
	public void addLRU(MemoryAccess m){
		
		int index = 0;
		int min = (int) cache.getAssoc();

		// Locate the least recently used by finding the lowest
		// index in the replacement array.
		for(int i = 0; i < cache.getAssoc(); i++){
			if(cache.getReplace()[m.getSetNumber()][i] < min){
				min = cache.getReplace()[m.getSetNumber()][i];
				index = i;
			}
		}

		// Update the LRU array.
		LRU(index, m.getSetNumber());
		// Add the tag to the index of the least recently used tag.
		cache.getCacheArray()[m.getSetNumber()][index] = m.getTag();

		// For write-back, if the previous tag was a write,
		// write the data back to memory. For write-through,
		// all the dirty bits are automatically set to zero and
		// will never increment the writes.
		if(cache.getDirty()[m.getSetNumber()][index] == 1){
			write++;
		}

		// Set the dirty bit to zero.
		cache.getDirty()[m.getSetNumber()][index] = 0;

		// If the operation is a write, check the write back
		// policy and update accordingly.
		if(m.getOperationType().equals("W")){
			checkWritePolicy(m.getSetNumber(), index);
		}
	}

	// Method to add memory access based on first in first out
	// replacement policy.
	public void addFifo(MemoryAccess m){
	
		// For write-back, if the previous tag was a write,
		// write the data back to memory. For write-through,
		// all the dirty bits are automatically set to zero and
		// will never increment the writes.
		if(cache.getDirty()[m.getSetNumber()][0] == 1){
			write++;
		}

		// Shift all tags down in the Cache and dirty bit array 
		// so the first one in is the first one out. 
		for(int i = 1; i < cache.getAssoc(); i++){
			cache.getCacheArray()[m.getSetNumber()][i-1] = cache.getCacheArray()[m.getSetNumber()][i];
			cache.getDirty()[m.getSetNumber()][i-1] = cache.getDirty()[m.getSetNumber()][i];
		}		
		
		// Update the cache with the new tag at the last location
		cache.getCacheArray()[m.getSetNumber()][cache.getCacheArray()[m.getSetNumber()].length-1] = m.getTag();

		// Set the dirty bit to zero. This will be updated if 
		// the operation type is a write.
		cache.getDirty()[m.getSetNumber()][cache.getCacheArray()[m.getSetNumber()].length-1] = 0; 
		
		// If the operation is a write, check the write back
		// policy and update accordingly.
		if(m.getOperationType().equals("W")){
			checkWritePolicy(m.getSetNumber(), cache.getCacheArray()[m.getSetNumber()].length-1);
		}
	}

	// Method checks the write-back policy of the cache.
	void checkWritePolicy(int setNumber, int index){
		// If the write-back policy is a write-back,
		// set the dirty bit to 1 to be updated next time
		// it is removed from the cache.
		if(cache.getWBPolicy()){
			cache.getDirty()[setNumber][index] = 1;
		}
		// If the write-back policy is a write-throug,
		// immediately write to memory.
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
					checkWritePolicy(m.getSetNumber(), i);
				}
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
		System.out.println("Miss Ratio: " + (double) miss/(miss + hit));
		System.out.println("Number of writes: " + write);
		System.out.println("Number of reads: " + read);
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
