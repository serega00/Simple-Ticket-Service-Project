package org.walmartlabs.ticketservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public abstract class SeatArrangement {
	
	protected String customerEmail;
	protected List<Seat> seats = new ArrayList<>();
	protected long creationTime; 
	
	public SeatArrangement(String customerEmail, List<Seat> seats) {
		this.customerEmail = customerEmail;
		this.seats = seats;
		creationTime = System.currentTimeMillis();
	}

	public long getCreationTime() {
		return creationTime;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	public int getNumSeats() {
		return seats.size();
	}
	public List<Seat> getSeats() {
		return seats;
	}
	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public String toString() {
		
		Calendar calendar = new GregorianCalendar();
	    calendar.setTimeInMillis(creationTime);
	    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	    formatter.setCalendar(calendar);

	    StringBuilder str = new StringBuilder();
		str.append("Seat Arrangement [ ");
		str.append("customer email: " + customerEmail + ", ");
		str.append("created: " + formatter.format(calendar.getTime()) + ", ");
		str.append(getNumSeats() + " seats: ");
		for(Seat seat : seats) {
			str.append(seat.getSeatInfo(Constants.SEAT_INFO_SEATNUMBER) + " ");
		}
		str.append("]");
	    
		return str.toString();
	}
	
}
