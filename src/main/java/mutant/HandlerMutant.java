package mutant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mutant.business.BusinessMutant;
import mutant.exception.InvalidOrderMatrixException;
import mutant.exception.NoMutantDNAException;
import mutant.model.Dna;

// Handler value: example.HandlerDivide
public class HandlerMutant implements RequestHandler<Dna, String>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  BusinessMutant businessMutant;
  
  public HandlerMutant(BusinessMutant businessMutant) {
	super();
	this.businessMutant = businessMutant;
  }
  
  public HandlerMutant() {
	super();
	businessMutant = new BusinessMutant();
  }


@Override
  public String handleRequest(Dna event, Context context)
  {
	String jsonDNA =  gson.toJson(event); 
    LambdaLogger logger = context.getLogger();
    logger.log("EVENT: " + jsonDNA); 
    // process event
       
    //Validamos que la matriz ingresada sea una matriz cuadrada y cumpla con las condiciones indicadas
    //En caso que no sea matriz cuadrada se lanza una RuntimeException Customizada a la cual se le da tratamiento desde API Gateway
    if(!businessMutant.isValidMatrixOrder(event.getDna())) {
    	logger.log("Exception: 400 Bad Request: The input matrix is not a square matrix");
    	throw new InvalidOrderMatrixException("400 Bad Request: The input matrix is not a square matrix");   	
    }
    //Validamos las condiciones para que se trate de un mutante
    if(businessMutant.isMutant(event.getDna(),jsonDNA)) {    	
    	return "Congratulations is mutant DNA !!";
    }else {
    	//En caso de que no se trate de un mutante se lanza una RuntimeException customizada para ser captada en Api Gateway 
    	// y así dar el comportamiento esperado
    	logger.log("Exception: 403 Forbidden: Not mutant DNA ");    	
    	throw new NoMutantDNAException("403 Forbidden: Not mutant DNA");
	}    
  }
}