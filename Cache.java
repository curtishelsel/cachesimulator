public class Cache{

	private int size;
	private int associativity;
	private String replacementPolicy;
	private boolean writeBackPolicy;

	public Cache(){
	
	}

	public int getSize(){
		return size;
	}

	public int getAssoc(){
		return associativity;
	}

	public String getRPolicy(){
		return replacementPolicy;
	}
	
	public boolean getWBPolicy(){
		return writeBackPolicy;
	}


}
