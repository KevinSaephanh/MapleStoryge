package exceptions;

public class AccountAlreadyExistsException extends Exception {
	public AccountAlreadyExistsException(String e) {
		super(e);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
