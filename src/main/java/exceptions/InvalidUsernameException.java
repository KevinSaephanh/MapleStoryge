package exceptions;

public class InvalidUsernameException extends Exception {
	public InvalidUsernameException(String e) {
		super(e);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
