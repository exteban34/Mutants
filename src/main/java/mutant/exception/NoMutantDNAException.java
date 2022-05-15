package mutant.exception;

public class NoMutantDNAException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NoMutantDNAException(String errorMessage) {
        super(errorMessage);
    }

}
