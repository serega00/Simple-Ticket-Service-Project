package org.walmartlabs.ticketservice;

import java.util.List;

public class SeatArrangementFactory {

	public SeatHold createSeatHold(int holdId, String customerEmail, List<Seat> seats) {
		return new SeatHold(holdId, customerEmail, seats);
	}
	
	public SeatReservation createSeatReservation(SeatHold seathold, String reservationCode) {
		return new SeatReservation(reservationCode, seathold.customerEmail, seathold.seats);
	}
}
