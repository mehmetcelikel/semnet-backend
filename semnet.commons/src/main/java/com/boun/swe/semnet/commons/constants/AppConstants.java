package com.boun.swe.semnet.commons.constants;

public interface AppConstants {

    String PROD = "prod";
    String DEV = "dev";
    
    public static final String OS_NAME = "os.name";
    public static final String WINDOWS = "Windows";
    
    public static final Object PERSISTED_WINDOWS_PATH = "C:/SEMNET/persistence/";
    public static final Object PERSISTED_UNIX_PATH = "/home/deploy/chroniclemap/";
    public static final String UNDERSCORE = "_";
    public static final Object DAT = ".dat";
    
    public static final float SEMANTIC_CO_OCCURANCE_FACTOR = 10F;
	public static final float SEMANTIC_SAME_TAG_FACTOR = 100F;
	public static final float SEMANTIC_SAME_CONTEXT_FACTOR = 80F;
	public static final float SEMANTIC_CONTEXT_RELATION_FACTOR = 60F;
	
	public static final float SEMANTIC_INDEX_LIMIT = 60F;
}
