package exceptions;

public class UserDoesNotExistException extends Exception {
	public UserDoesNotExistException(String e) {
		super(e);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
