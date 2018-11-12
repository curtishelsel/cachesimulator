/* Curtis Helsel
   EEL4768 - Fall 2018
   November 3, 2018

   Class implementation of a memory access.
*/

import java.math.BigInteger;

public class MemoryAccess{
	
	private String operationType;
	private BigInteger address;
	private int setNumber;
	private BigInteger tag;

	public MemoryAccess(String operationType, String address){
		this.operationType = operationType;
		this.address = new BigInteger(address.substring(2), 16);		
	}

	public String getOperationType(){
		return operationType;
	}

	public BigInteger getAddress(){
		return address;
	}

	public void setSetNumberAndTag(int blockSize, int numSets){
		BigInteger bs = BigInteger.valueOf((long) blockSize);
		BigInteger tagIndex = address.divide(bs);
		setNumber = tagIndex.mod(BigInteger.valueOf((long) numSets)).intValue();
		tag = tagIndex.divide(BigInteger.valueOf((long) numSets));
	}

	public int getSetNumber(){
		return setNumber;
	}

	public BigInteger getTag(){
		return tag;
	}

}
