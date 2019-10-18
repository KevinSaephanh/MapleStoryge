package exceptions;

public class InvalidPasswordException extends Exception {
	public InvalidPasswordException(String e) {
		super(e);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
