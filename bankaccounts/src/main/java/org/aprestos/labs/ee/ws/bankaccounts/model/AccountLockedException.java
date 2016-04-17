package org.aprestos.labs.ee.ws.bankaccounts.model;

public class AccountLockedException extends AccountException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AccountLockedException() {
	// TODO Auto-generated constructor stub
    }

    public AccountLockedException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    public AccountLockedException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    public AccountLockedException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    public AccountLockedException(String message, Throwable cause,
	    boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
	// TODO Auto-generated constructor stub
    }

}
