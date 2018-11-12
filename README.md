# cachesimulator

Two ways to run this file using java virtual machine.

1. Use provide jar file with appropriate command line paramenters.

	java -jar CacheSim.jar <CACHE_SIZE> <ASSOC> <REPLACEMENT> <WB> <TRACE_FILE>

	Example:

		java -jar CacheSim.jar 32768 8 0 1 ./XSBENCH.t
	


2. Compile source files and run
	
	javac <Java files>
	java <Class file> <CACHE_SIZE> <ASSOC> <REPLACEMENT> <WB> <TRACE_FILE>
	
	Example:
	
		javac CacheSimulator.java MemoryAccess.java Cache.java
		java CacheSimulator 32768 8 0 1 ./XSBENCH.t
