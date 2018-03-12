package org.walmartlabs.ticketservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class SeatReservation extends SeatArrangement  {

	private String reservationCode;
	
	public SeatReservation(String reservationCode, String customerEmail, List<Seat> seats) {
		super(customerEmail, seats);
		this.reservationCode = reservationCode;
		for(Seat seat : seats) {
			seat.reserveSeat();
		}
	}

	public String getReservationCode() {
		return reservationCode;
	}

	public String toString() {
		
		Calendar calendar = new GregorianCalendar();
	    calendar.setTimeInMillis(creationTime);
	    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	    formatter.setCalendar(calendar);

	    StringBuilder str = new StringBuilder();
		str.append("Seat Reservation [ ");
		str.append("reservation code: " + reservationCode + ", ");
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
