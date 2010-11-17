package org.apromore.toolbox.similaritySearch.common;

import java.util.StringTokenizer;

import org.apromore.toolbox.similaritySearch.common.stemmer.SnowballStemmer;


public class Settings {
	public static String STRING_DELIMETER = " ,.:;&/?!#()";

	public static boolean logResult = true;
	public static boolean considerEvents = true;
	public static boolean considerGateways = true;
	
	// weights for greedy and other algorithms
	public static double vweight = 1.0;
	public static double sweight = 1.0;
	public static double eweight = 1.0;	
	
	private static SnowballStemmer englishStemmer;
	
	public static SnowballStemmer getEnglishStemmer() {
		if (englishStemmer == null) {
			englishStemmer = getStemmer("english");
		}
		
		return englishStemmer;
	}
	
	public static SnowballStemmer getStemmer(String language){
		@SuppressWarnings("rawtypes")
		Class stemClass;
		SnowballStemmer stemmer;

		try {
			stemClass = Class.forName("org.apromore.toolbox.similaritySearch.common.stemmer.ext." + language + "Stemmer");
			stemmer = (SnowballStemmer) stemClass.newInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return stemmer;
	}
	
	public static String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s);
		String result = "";
		
		while (st.hasMoreTokens()) {
			result += st.nextToken()+ (st.hasMoreTokens() ? " " : "");
		}
		
		return result;
	}
}