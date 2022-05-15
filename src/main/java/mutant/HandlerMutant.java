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
       
    if(!businessMutant.isValidMatrixOrder(event.getDna())) {
    	logger.log("Exception: 400 Bad Request: The input matrix is not a square matrix");
    	throw new InvalidOrderMatrixException("400 Bad Request: The input matrix is not a square matrix");   	
    }
    if(businessMutant.isMutant(event.getDna(),jsonDNA)) {    	
    	return "Congratulations is mutant DNA !!";
    }else {
    	logger.log("Exception: 403 Forbidden: Not mutant DNA ");    	
    	throw new NoMutantDNAException("403 Forbidden: Not mutant DNA");
	}    
  }
}