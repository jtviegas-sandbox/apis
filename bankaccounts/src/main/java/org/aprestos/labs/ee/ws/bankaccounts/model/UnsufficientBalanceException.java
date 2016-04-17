package org.aprestos.labs.ee.ws.bankaccounts.model;

public class UnsufficientBalanceException extends AccountException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UnsufficientBalanceException() {
	// TODO Auto-generated constructor stub
    }

    public UnsufficientBalanceException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    public UnsufficientBalanceException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    public UnsufficientBalanceException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    public UnsufficientBalanceException(String message, Throwable cause,
	    boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
	// TODO Auto-generated constructor stub
    }

}
