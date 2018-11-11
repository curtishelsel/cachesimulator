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

	public void setSetNumber(int blockSize){
		BigInteger bs = BigInteger.valueOf((long) blockSize);
		BigInteger tagIndex = address.divide(bs);
		setNumber = tagIndex.mod(bs).intValue();
	}

	public int getSetNumber(){
		return setNumber;
	}

	public void setTag(int numSets){
		tag = address.divide(BigInteger.valueOf((long) numSets));
	}

	public BigInteger getTag(){
		return tag;
	}

}
