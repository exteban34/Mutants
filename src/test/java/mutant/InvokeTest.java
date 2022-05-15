package mutant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;

import mutant.business.BusinessMutant;
import mutant.exception.InvalidOrderMatrixException;
import mutant.exception.NoMutantDNAException;
import mutant.model.Dna;
import mutant.persistence.MutantDynamoBDPersistence;

class InvokeTest {
  private static final Logger logger = LoggerFactory.getLogger(InvokeTest.class);
  
  @Mock
  BusinessMutant businessMutant;
	 
  private HandlerMutant handler;
  private AutoCloseable closeable;

  @BeforeEach
  public void initBussiness() {
	 closeable = MockitoAnnotations.openMocks(this);
	 handler =  new HandlerMutant(businessMutant);
  }
	 
  @AfterEach
  public void closeBusiness() throws Exception {
	 closeable.close();
  }  
  
  @Test
  void invokeMutantDNATest() { 
    Dna event = new Dna();
    String[] arrayOneMutantString=new String[] {"AGTAG", "GTTTT", "ATCGA", "TTTAG", "ATGGG"};
    event.setDna(arrayOneMutantString);  
    Context context = new TestContext();
    Mockito.when(businessMutant.isValidMatrixOrder(Mockito.any())).thenReturn(true);
    Mockito.when(businessMutant.isMutant(Mockito.any(), Mockito.anyString())).thenReturn(true);   
    assertTrue("Congratulations is mutant DNA !!".equals(handler.handleRequest(event, context )) );
  }
 
  
  @Test
  void invokeNoMutantDNATest() {
	Dna event = new Dna();
	String[] arrayOneMutantString=new String[] {"AGT", "GTT", "GAT"};
	event.setDna(arrayOneMutantString);  
	Context context = new TestContext();
	Mockito.when(businessMutant.isValidMatrixOrder(Mockito.any())).thenReturn(true);
	Mockito.when(businessMutant.isMutant(Mockito.any(), Mockito.anyString())).thenReturn(false);
	Exception exception = assertThrows(NoMutantDNAException.class, () -> {
		handler.handleRequest(event, context);
	    });
    String expectedMessage = "403 Forbidden: Not mutant DNA";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));  
  }
  
  @Test
  void invokeInvalidMatrixTest() {
	    Dna event = new Dna();
	  String[] arrayOneMutantString=new String[] {"AGTAG", "GTTTT", "GA", "TTTAG", "ATGGG"};
	    event.setDna(arrayOneMutantString);  
	    Context context = new TestContext();	    
	Exception exception = assertThrows(InvalidOrderMatrixException.class, () -> {
		handler.handleRequest(event, context);
	    });
    String expectedMessage = "400 Bad Request: The input matrix is not a square matrix";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));  
  }
  
  
  
}
