package dbapp.exceptions;

@SuppressWarnings("serial")
public class DBAppException extends Exception {
	public DBAppException () {
		super();
	}
	
	public DBAppException (String s) {
		super(s);
	}
}
