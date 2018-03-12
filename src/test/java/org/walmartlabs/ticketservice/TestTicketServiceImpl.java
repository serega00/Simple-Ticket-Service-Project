package org.walmartlabs.ticketservice;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTicketServiceImpl {

	private static SeatArrangementFactory seatArrangementFactory = null;
	private static TicketServiceImpl ticketService = null;
	private static Seat[][] venueSeats;
	private static int venueRows;
	private static int venueColumns;
	private static String email = "user@server.com";
	private static SeatHold expectedSeatHold;
	private static String expectedReservationCode;
	private static int holdTimeout;
	
	@BeforeClass
	public static void InitTicketServiceImpl()
	{
		venueRows = 10;
		venueColumns = 10;
		holdTimeout = 60000; 

		seatArrangementFactory = new SeatArrangementFactory();
		ticketService = new TicketServiceImpl(venueRows, venueColumns, holdTimeout, seatArrangementFactory);
		venueSeats = ticketService.getVenueSeats();
	}
	
	@Test
	public void test1NumSeatsAvailable() {
		System.out.println("In testNumSeatsAvailable");
		int actualNumSeats = ticketService.numSeatsAvailable();
		System.out.println("actualNumSeats: " + actualNumSeats);
		int expectedNumSeats = venueRows*venueColumns;
		assertEquals(expectedNumSeats, actualNumSeats);
	}
	
	@Test
	public void test2FindAndHoldSeats() {
		
		System.out.println("In testFindAndHoldSeats");

		List<Seat> seats = new ArrayList<>();
		seats.add(venueSeats[0][0]);
		seats.add(venueSeats[0][1]);
		seats.add(venueSeats[0][2]);
		seats.add(venueSeats[0][3]);
		SeatHold actualSeatHold = ticketService.findAndHoldSeats(seats.size(), email);
		System.out.println(actualSeatHold.toString());
		
		expectedSeatHold = seatArrangementFactory.createSeatHold(1, email, seats);

		assertEquals(expectedSeatHold.getHoldId(), actualSeatHold.getHoldId());
		assertEquals(expectedSeatHold.getCustomerEmail(), actualSeatHold.getCustomerEmail());
		assertEquals(expectedSeatHold.getSeats(), actualSeatHold.getSeats());
		assertEquals(expectedSeatHold.getNumSeats(), actualSeatHold.getNumSeats());
		
	}
	
	@Test
	public void test3FindSeatHolds() {
		System.out.println("In testFindSeatHolds");
    	List<SeatHold> actualSeatHold = ticketService.findSeatHolds(1);
		System.out.println(actualSeatHold.get(0).toString());

		assertEquals(expectedSeatHold.getHoldId(), actualSeatHold.get(0).getHoldId());
		assertEquals(expectedSeatHold.getCustomerEmail(), actualSeatHold.get(0).getCustomerEmail());
		assertEquals(expectedSeatHold.getSeats(), actualSeatHold.get(0).getSeats());
		assertEquals(expectedSeatHold.getNumSeats(), actualSeatHold.get(0).getNumSeats());
	}

	@Test
	public void test4ReserveSeats() {
		System.out.println("In testReserveSeats");
		String holdId = new Integer(1).toString();
		String actualReservationCode = ticketService.reserveSeats(1, email);
       	List<SeatReservation> reservations = ticketService.findSeatReservations(actualReservationCode);
		for(SeatReservation reservation : reservations) {
			System.out.println(reservation.toString());
		}
		
		expectedReservationCode = email + "-" + holdId;
		assertEquals(expectedReservationCode, actualReservationCode);
	}
	
	@Test
	public void test5FindSeatReservations() {
		System.out.println("In testFindSeatReservations");
		List<SeatReservation> actualReservations = ticketService.findSeatReservations(email + "-1");
		System.out.println(actualReservations.get(0).toString());

		SeatReservation expectedSeatReservation = seatArrangementFactory.createSeatReservation(expectedSeatHold, expectedReservationCode);

		assertEquals(expectedSeatReservation.getReservationCode(), actualReservations.get(0).getReservationCode());
		assertEquals(expectedSeatReservation.getCustomerEmail(), actualReservations.get(0).getCustomerEmail());
		assertEquals(expectedSeatReservation.getSeats(), actualReservations.get(0).getSeats());
		assertEquals(expectedSeatReservation.getNumSeats(), actualReservations.get(0).getNumSeats());
	}
	
}
