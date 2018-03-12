package org.walmartlabs.ticketservice;

public class TicketServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8702491613009174249L;
	
	private String error;
	
	public String getError() {
		return error;
	}

	public TicketServiceException(String error) {
		this.error = error;
	}
	
	
}
