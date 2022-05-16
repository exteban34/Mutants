package mutant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import mutant.business.BusinessMutant;
import mutant.model.Stats;

public class HandlerMutantAnalisysStats implements  RequestHandler<String, Stats>  {

	BusinessMutant businessMutant;
	
	public HandlerMutantAnalisysStats(BusinessMutant businessMutant) {
		super();
		this.businessMutant = businessMutant;
	}

	public HandlerMutantAnalisysStats() {
		super();
		businessMutant = new BusinessMutant();
	}

	@Override
	public Stats handleRequest(String input, Context context) {
		// TODO Auto-generated method stub
		return  businessMutant.getStats();
	}

}
