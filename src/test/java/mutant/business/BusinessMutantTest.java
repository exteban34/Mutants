package mutant.business;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import mutant.model.Stats;
import mutant.persistence.MutantDynamoBDPersistence;

public class BusinessMutantTest {

	String[] arrayOneMutantString=new String[] {"AGTAG", "GTTTT", "ATCGA", "TTTAG", "ACGGG"};
	String[] arrayTwoMutantString=new String[] {"AGTAG", "GTTTT", "ATCGA", "TTTAG", "AGGGG"};
	String[] arrayTwoMutantStringInOneString=new String[] {"AGTAGATG", "GTATTTAC", "ATCGAATC", "TTTAGCAT", "AACATGGG","TTTTTTTT","ATCGAACG","AATTGGCC"};
	String[] arrayzeroMutantString=new String[] {"AGTAG", "GATAG", "ATAGG", "TTTAG", "ATGGG"};
	String[] noMutantMatrix =new String[] {"ATGA", "AAAG", "ATAA", "CTGC"};
	String[] invlaidMatrix=new String[] {"AGTAG", "ATAG", "ATGG", "TTTAG", "ATGGG"};
	Stats stats = new Stats(40, 100, 0L);
	
	
	 @Mock
	 MutantDynamoBDPersistence mutantDynamoBDPersistence;
	 
	 private BusinessMutant businessMutant;
	 private AutoCloseable closeable;
	 
	 @BeforeEach
	 public void initBussiness() {
		 closeable = MockitoAnnotations.openMocks(this);
		 businessMutant =  new BusinessMutant(mutantDynamoBDPersistence);
	 }
	 
	 @AfterEach
	 public void closeBusiness() throws Exception {
		 closeable.close();
	 }
	 
	 
	// Bloque de pruebas del metodo isMutantString
	@Test
	public void isMutantStringA(){		
		assertTrue(new BusinessMutant().isMutantString("AAAA"));
	}
	@Test
	public void isMutantStringT(){		
		assertTrue(businessMutant.isMutantString("TTTT"));
	}	
	@Test
	public void isMutantStringC(){
		assertTrue(businessMutant.isMutantString("CCCC"));
	}	
	@Test
	public void isMutantStringG(){
		assertTrue(businessMutant.isMutantString("GGGG"));
	}	
	@Test
	public void notMutantString(){		
		assertFalse(businessMutant.isMutantString("AGTC"));
	}	
	
	// Bloque de pruebas del metodo numberOfMutantStringsInString
	@Test
	public void oneOnlyMutantString() {		
		assertTrue(1== businessMutant.numberOfMutantStringsInString("CCCC"));		
	}	
	@Test
	public void twoNonConsecutiveMutantStrings() {		
		assertTrue(2== businessMutant.numberOfMutantStringsInString("CCCCTTAAAA"));		
	}	
	@Test
	public void twoConsecutiveMutantStrings() {		
		assertTrue(2== businessMutant.numberOfMutantStringsInString("CCCCAAAA"));		
	}	
	@Test
	public void zeroMutantStrings() {		
		assertTrue(0== businessMutant.numberOfMutantStringsInString("AGTC"));		
	}	
	@Test
	public void twoConsecutiveMutantStringsSameLetter() {		
		assertTrue(2== businessMutant.numberOfMutantStringsInString("TTTTTTTT"));		
	}
	
	// Bloque de prubeas del metodo numberOfMutantStringsInRows
	@Test
	public void oneMutantStringInRows() {
		assertTrue(1== businessMutant.numberOfMutantStringsInRows(arrayOneMutantString));		
	}		
	@Test
	public void twoMutantStringInRows() {
		assertTrue(2== businessMutant.numberOfMutantStringsInRows(arrayTwoMutantString));		
	}	
	@Test
	public void twoMutantStringInRowsInOneString() {
		assertTrue(2== businessMutant.numberOfMutantStringsInRows(arrayTwoMutantStringInOneString));		
	}	
	@Test
	public void zeroMutantStringInRows() {
		assertTrue(0== businessMutant.numberOfMutantStringsInRows(arrayzeroMutantString));		
	}	
	
	// Bloque de pruebas del metodo numberOfMutantStringsInColumns
	@Test
	public void oneMutantStringInColumns() {
		assertTrue(1 ==businessMutant.numberOfMutantStringsInColumns(arrayzeroMutantString));
	}	
	@Test
	public void zeroMutantStringInColumns() {
		assertTrue(0 ==businessMutant.numberOfMutantStringsInColumns(arrayOneMutantString));
	}		
	
	// Bloque de pruebas del metodo numberOfMutantStringsInDiagonals
	@Test
	public void zeroMutantStringInDiagonals() {
		assertTrue(0 ==businessMutant.numberOfMutantStringsInDiagonals(arrayOneMutantString));
	}	
	@Test
	public void oneMutantStringInDiagonals() {
		assertTrue(1 ==businessMutant.numberOfMutantStringsInDiagonals(arrayzeroMutantString));
	}	
	
	// Bloque de pruebas del metodo isMutant
	@Test
	public void isMutantVerify() {
		Mockito.when(mutantDynamoBDPersistence.saveMutantAnalisys(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(true);
		assertTrue( businessMutant.isMutant(arrayzeroMutantString,""));
	}	
	@Test
	public void isMutantVerify2() {
		Mockito.when(mutantDynamoBDPersistence.saveMutantAnalisys(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(true);
		assertTrue( businessMutant.isMutant(arrayTwoMutantStringInOneString,""));
	}	
	@Test
	public void isNotMutantVerify() {
		Mockito.when(mutantDynamoBDPersistence.saveMutantAnalisys(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(true);
		assertFalse( businessMutant.isMutant(noMutantMatrix,""));
	}	
	
	// Bloque de pruebas del metodo isValidMatrixOrder
	@Test
	public void isValidMatrix() {
		assertTrue( businessMutant.isValidMatrixOrder(arrayOneMutantString));
	}	
	@Test
	public void isInvalidMatrix() {
		assertFalse(businessMutant.isValidMatrixOrder(invlaidMatrix));
	}
	
	// Bloque de pruebas del metodo getStats
	@Test
	public void validateStatsWithZeroAnalisys() {
		Mockito.when(mutantDynamoBDPersistence.getStats()).thenReturn(new Stats());
		Stats stats =  businessMutant.getStats();
		assertTrue(stats.getRatio() == 0);
	}
	@Test
	public void validateStats() {
		Mockito.when(mutantDynamoBDPersistence.getStats()).thenReturn(stats);
		Stats stats =  businessMutant.getStats();
		assertTrue(stats.getRatio() == 0.4);
	}
	
	
	
	
	
}
