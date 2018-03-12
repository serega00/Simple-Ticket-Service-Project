package org.walmartlabs.ticketservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class SeatHold extends SeatArrangement {

	private int holdId;
	
	public SeatHold(int holdId, String customerEmail, List<Seat> seats) {
		super(customerEmail, seats);
		this.holdId = holdId;
		for(Seat seat : seats) {
			seat.holdSeat();
		}
	}

	public void releaseHold() {
		for(Seat seat : seats) {
			seat.releaseSeat();
		}
	}

	public int getHoldId() {
		return holdId;
	}

	public String toString() {
		
		Calendar calendar = new GregorianCalendar();
	    calendar.setTimeInMillis(creationTime);
	    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	    formatter.setCalendar(calendar);

	    StringBuilder str = new StringBuilder();
		str.append("Seat Hold [ ");
		str.append("hold id: " + holdId + ", ");
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
