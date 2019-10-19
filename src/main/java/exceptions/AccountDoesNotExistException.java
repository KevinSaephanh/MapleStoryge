package exceptions;

public class AccountDoesNotExistException extends Exception {
	public AccountDoesNotExistException(String e) {
		super(e);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
