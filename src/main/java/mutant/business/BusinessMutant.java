package mutant.business;

import static java.lang.Math.min;

import mutant.model.Stats;
import mutant.persistence.MutantDynamoBDPersistence;

import static java.lang.Math.max;

public class BusinessMutant {
	
	public MutantDynamoBDPersistence mutantDynamoBDPersistence;
	
	public BusinessMutant(MutantDynamoBDPersistence bd) {
		super();
		this.mutantDynamoBDPersistence = bd;
	}
	
	public BusinessMutant() {
		mutantDynamoBDPersistence = new MutantDynamoBDPersistence();
	}

	// Metodo que verifica si un String es una cadena mutante (secuencia de cuatro letras iguales​)
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
	
	//Metodo que cuenta cuantas  cadenas mutantes existen en un String (No se consideran traslapes)
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
		
	//Metodo que cuenta cuantas cadenas mutantes existen en la matriz recorriendola por filas
	public int numberOfMutantStringsInRows(String[] array) {
		int numberOfMutantStrings =0;
		
		for (String string : array) {
			numberOfMutantStrings += numberOfMutantStringsInString(string);
		}		
		return numberOfMutantStrings;
		}
	
	//Metodo que cuenta cuantas cadenas mutantes existen en la matriz recorriendola por Columnas
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
	
	//Metodo que cuenta cuantas cadenas mutantes existen en la matriz recorriendola de manera oblicua
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

	//Metodo que cuenta la totalidad de cadenas mutantes y define si se trata de un mutante
	//Se encarga de lanzar la capa de percistencia para guardar la data
	public boolean isMutant(String[] array, String jsonDNA) {
		
		mutantDynamoBDPersistence.initDynamoDbClient();
		int numberofMutantStringInDNA = numberOfMutantStringsInRows(array)+ numberOfMutantStringsInColumns(array)+ numberOfMutantStringsInDiagonals(array) ;
		if (2<= numberofMutantStringInDNA) {
			mutantDynamoBDPersistence.saveMutantAnalisys(jsonDNA, true);
			mutantDynamoBDPersistence.updateMutantAnalisysStats(true);
			return true;
		}else {
			mutantDynamoBDPersistence.saveMutantAnalisys(jsonDNA, false);
			mutantDynamoBDPersistence.updateMutantAnalisysStats(false);
			return false;
		}		
	}
	
	//Metodo para verificar que se trata d euna matriz cuadrada
	public boolean isValidMatrixOrder(String[] array) {
		int n = array.length;
		for (String string : array) {
			if(n!= string.length()) {
				return false;
			}
		}		
		return true;	
	}
	
	//Metodo para obtener las estadisticas y controlar un posible error cuando no existen analisis
	public Stats getStats() {
		mutantDynamoBDPersistence.initDynamoDbClient();
		Stats stats = mutantDynamoBDPersistence.getStats();		
		if(stats.getNumberofAnalisys()==0) {
			stats.setRatio(0);
		}else {
			stats.setRatio(stats.getNumberOfMutants()/stats.getNumberofAnalisys());
		}		
		return stats;
		
	}

}
