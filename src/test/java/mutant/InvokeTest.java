package mutant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.amazonaws.services.lambda.runtime.Context;

import mutant.business.BusinessMutant;
import mutant.exception.InvalidOrderMatrixException;
import mutant.exception.NoMutantDNAException;
import mutant.model.Dna;

//Clase de Pruebas de invocacion del Handler
class InvokeTest {
  
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
  
  //Test del llamado con una matriz DNA Mutante
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
 
  //Test del llamado con una matriz DNA Mutante 
  // Valida el lanzamiento de la excepcion  NoMutantDNAException
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
  
  //Test del llamado con una matriz incorrecta (matriz no cuadrada)
  // Valida el lanzamiento de la excepcion  InvalidOrderMatrixException
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
