package mutant.business;

import static java.lang.Math.min;

import mutant.persistence.MutantDynamoBDPersistence;

import static java.lang.Math.max;

public class BusinessMutant {
	
	public MutantDynamoBDPersistence bd;
	
	public BusinessMutant(MutantDynamoBDPersistence bd) {
		super();
		this.bd = bd;
	}
	
	public BusinessMutant() {
		bd = new MutantDynamoBDPersistence();
	}

	public boolean isMutantString (String string) {
		
		switch (string) {
		case "AAAA":
			return true;			
		case "TTTT":
			return true;			
		case "CCCC":
			return true;			
		case "GGGG":
			return true;	
		default:
			return false;
		}
	};
	
	public int numberOfMutantStringsInString(String string) {
		String aux;
		int numberOfMutantStrings = 0;	
		int i = 0;
		while (i+4 <= string.length()) {
			aux = string.substring(i, i+4);
			if (isMutantString(aux)) {
				numberOfMutantStrings ++;
				i=i+4;
			}else {
				i++;
			}
		}
		return numberOfMutantStrings;
		};

	public int numberOfMutantStringsInRows(String[] array) {
		int numberOfMutantStrings =0;
		
		for (String string : array) {
			numberOfMutantStrings += numberOfMutantStringsInString(string);
		}		
		return numberOfMutantStrings;
		}
	
	public int numberOfMutantStringsInColumns(String[] array) {
		int numberOfMutantStringsInColumns=0;
		String aux = "";
		for (int i = 0; i < array.length; i++) {			
			for (int j = 0; j < array.length; j++) {
				 aux = aux + array[j].substring(i, i+1);				
			}
			numberOfMutantStringsInColumns += numberOfMutantStringsInString(aux);
			aux="";
		}
		 return numberOfMutantStringsInColumns;		
	}
	
	public int numberOfMutantStringsInDiagonals(String[] array) {
		int numberOfMutantStringsInDiag=0;
		int n = array.length;
		String aux = "";
		for (int i = 1 - n; i < n; i++) {
		    for (int x = -min(0, i), y = max(0, i); x < n && y < n; x++, y++) {
		    	 aux = aux + array[y].substring(x, x+1);
		    	 }
		    numberOfMutantStringsInDiag += numberOfMutantStringsInString(aux);
		    aux="";
		}
		
		return numberOfMutantStringsInDiag;
		
	}

	public boolean isMutant(String[] array, String jsonDNA) {
		
		bd.initDynamoDbClient();
		int numberofMutantStringInDNA = numberOfMutantStringsInRows(array)+ numberOfMutantStringsInColumns(array)+ numberOfMutantStringsInDiagonals(array) ;
		if (2<= numberofMutantStringInDNA) {
			bd.saveMutantAnalisys(jsonDNA, true);
			return true;
		}else {
			bd.saveMutantAnalisys(jsonDNA, false);
			return false;
		}
		
		
		
		
	}
	
	public boolean isValidMatrixOrder(String[] array) {
		int n = array.length;
		for (String string : array) {
			if(n!= string.length()) {
				return false;
			}
		}		
		return true;	
	}

}
