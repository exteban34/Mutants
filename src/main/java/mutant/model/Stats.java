package mutant.model;

public class Stats {
	private double numberOfMutants;
	private double numberofAnalisys;
	private double ratio;
	
	
	
	public Stats() {
		numberOfMutants =0;
		numberofAnalisys=0;
		ratio = 0L;
	}
	
	public Stats(double numberOfMutants, double numberofAnalisys, double ratio) {
		super();
		this.numberOfMutants = numberOfMutants;
		this.numberofAnalisys = numberofAnalisys;
		this.ratio = ratio;
	}


	public double getNumberOfMutants() {
		return numberOfMutants;
	}
	public void setNumberOfMutants(double numberOfMutants) {
		this.numberOfMutants = numberOfMutants;
	}
	public double getNumberofAnalisys() {
		return numberofAnalisys;
	}
	public void setNumberofAnalisys(double numberofAnalisys) {
		this.numberofAnalisys = numberofAnalisys;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	
	
	
	
}
