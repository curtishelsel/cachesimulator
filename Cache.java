/* Curtis Helsel
   EEL4768 - Fall 2018
   November 3, 2018

   Class implementation of a cache
*/

import java.math.BigInteger;

public class Cache{

	private long size;
	private long associativity;
	private int numSets;
	private static final int blockSize = 64;
	private String replacementPolicy;
	private boolean writeBackPolicy;
	private BigInteger[][] cacheArray;
	private int replace[][];
	private int dirty[][];

	public Cache(String size, String associativity, String replacementPolicy, String writeBackPolicy){
	
		this.size = Long.parseLong(size);
		this.associativity = Long.parseLong(associativity);

		numSets = (int) (getSize() / (blockSize * getAssoc()));

		cacheArray = new BigInteger[numSets][(int) getAssoc()];
		replace = new int[numSets][(int) getAssoc()];
	
		// Set initial values for replace array as 0-associativity
		// for use with LRU replacement policy.
		for(int i = 0; i < numSets; i++){
			for(int j = 0; j < getAssoc(); j++){
				replace[i][j] = j;
			}   
		}

		dirty = new int[numSets][(int) getAssoc()];

		if(replacementPolicy.equals("1")){
			this.replacementPolicy = "FIFO";
		}
		else{
			this.replacementPolicy = "LRU";
		}

		if(writeBackPolicy.equals("1")){
			this.writeBackPolicy = true;
		}
		else{
			this.writeBackPolicy = false;
		}
	}

	public long getSize(){
		return size;
	}

	public long getAssoc(){
		return associativity;
	}

	public String getRPolicy(){
		return replacementPolicy;
	}
	
	public boolean getWBPolicy(){
		return writeBackPolicy;
	}

	public int getNumSets(){
		return numSets;
	}

	public int getBlockSize(){
		return blockSize;
	}
	public BigInteger[][] getCacheArray(){
		return cacheArray;
	}

	public int[][] getDirty(){
		return dirty;
	}
	
	public int[][] getReplace(){
		return replace;
	}
}
