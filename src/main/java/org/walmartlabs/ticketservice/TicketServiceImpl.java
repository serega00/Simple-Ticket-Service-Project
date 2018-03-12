package org.walmartlabs.ticketservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class TicketServiceImpl implements TicketService {

	private int holdTimeout;
    private Timer timer;

    private Seat[][] venueSeats;
	private int venueRows;
	private int venueColumns;
	private Map<Integer, SeatHold> seatHolds = new ConcurrentHashMap<Integer, SeatHold>();
	private Map<String, SeatReservation> seatReservations = new ConcurrentHashMap<String, SeatReservation>();
	private SeatArrangementFactory seatArrangementFactory;
	private static int currentSeatHoldId = 0;
	
	public TicketServiceImpl(int venueRows, int venueColumns, int holdTimeout, SeatArrangementFactory seatArrangementFactory) {
		venueSeats = new Seat[venueRows][venueColumns];
		for (int row = 0; row < venueRows; row++) {
		    for (int col = 0; col < venueColumns; col++) {
		    	venueSeats[row][col] = new Seat(row, col);
		    }
		}
		this.venueRows = venueRows;
		this.venueColumns = venueColumns;
		this.seatArrangementFactory = seatArrangementFactory;
		this.holdTimeout = holdTimeout;
	}
	
	protected void finalize() throws Throwable { 
		stopTimer();
    }
    
	private int getNumberSeats(char status) {
		int numberOfSeats;
		
		if(status == Constants.SEAT_AVAILABLE || status == Constants.SEAT_HOLD || status == Constants.SEAT_RESERVED) 
		{
			numberOfSeats = 0;
			for (int row = 0; row < this.venueRows; row ++) {
			    for (int col = 0; col < this.venueColumns; col++) {
					if(this.venueSeats[row][col].getSeatStatus() == status) {
						numberOfSeats++;
					}
			    }
			}
		}
		else {
			numberOfSeats = venueRows*venueColumns;
		}
		
		return numberOfSeats;
	}
	
    private void startTimer() {
		//System.out.println("Started timer");
    	timer = new Timer();
        timer.schedule(new TimerTask() {
	        @Override
	        public void run () {
	            for (Map.Entry<Integer, SeatHold> entry : seatHolds.entrySet()) {
	            	SeatHold seatHold = entry.getValue();
	            	if ((System.currentTimeMillis() - seatHold.getCreationTime()) >= holdTimeout) {
	            		seatHold.releaseHold();
	            		Integer holdId = entry.getKey();
	            		seatHolds.remove(entry.getKey());
	            		System.out.println("Seat Hold with id " + holdId.toString() + " was released");
			    		if (seatHolds.size() == 0) {
			    			stopTimer();
			    		}
	            	}
	            }
	        }
        }, 1000, 1000);

	}

    private void stopTimer() {
		//System.out.println("Stoped timer");
		timer.cancel();
    }
    
	public Seat[][] getVenueSeats() {
		return venueSeats;
	}

   	@Override
	public int numSeatsAvailable() {
		return getNumberSeats(Constants.SEAT_AVAILABLE);
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) throws TicketServiceException {
		boolean done = false;
		SeatHold seatHold = null;
		
		//validation: not enough available seats
		if (getNumberSeats(Constants.SEAT_RESERVED) == (venueRows*venueColumns)) {
			throw new TicketServiceException("Error: Venue is sold out");
		}
		//validation: customer email is empty
		if (customerEmail.length() == 0) {
			throw new TicketServiceException("Error: Customer email can't be empty");
		}
		//validation: number of seats is invalid number
		if (numSeats <= 0) {
			throw new TicketServiceException("Error: Invalid number of seats");
		}
		//validation: not enough available seats
		if (getNumberSeats(Constants.SEAT_AVAILABLE) < numSeats) {
			throw new TicketServiceException("Error: Not enough available seats");
		}
		
		List<Seat> seats = new ArrayList<>();
		for (int row = 0; row < this.venueRows; row++) {
		    for (int col = 0; col < this.venueColumns; col++) {
		    	Seat seat = this.venueSeats[row][col];
		    	if(seat.getSeatStatus() == Constants.SEAT_AVAILABLE) {
		    		seats.add(seat);
			    	if (seats.size() == numSeats) {
			    		currentSeatHoldId++;
			    		seatHold = seatArrangementFactory.createSeatHold(currentSeatHoldId, customerEmail, seats);
			    		seatHolds.put(new Integer(currentSeatHoldId), seatHold);
			    		done = true;
			    		
			    		//start timer if this is first object in the map
			    		if (seatHolds.size() == 1) {
			    			startTimer();
			    		}
			    		break;
			    	}
		    	}
		    }
		    if (done) break;
		}

		return seatHold;
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) throws TicketServiceException {

		// validation: customer email is empty
		if (customerEmail.length() == 0) {
			throw new TicketServiceException("Error: Customer email can't be empty");
		}
		// validation: invalid seat hold id
		if (seatHoldId <= 0) {
			throw new TicketServiceException("Error: Invalid seat hold id");
		}
		
		String reservationCode = "";
    	SeatHold seatHold = seatHolds.get(new Integer(seatHoldId));
    	if (seatHold != null) {

    		if (!seatHold.getCustomerEmail().equals(customerEmail)) {
    			throw new TicketServiceException("Error: Can't reserve seats - customer email does not match");
        	}
    		
    		//generate reservation code, create reservation object and add to the map
    		reservationCode = customerEmail + "-" + seatHoldId;
    		SeatReservation seatReservation = seatArrangementFactory.createSeatReservation(seatHold, reservationCode);
    		seatReservations.put(reservationCode, seatReservation);
    		
    		//remove seat hold object from the map
    		seatHolds.remove(new Integer(seatHoldId));
    		if (seatHolds.size() == 0) {
    			stopTimer();
    		}

    	}
    	else {
			throw new TicketServiceException("Error: Can't find seat hold to reserve");
    	}
		return reservationCode;
	}

	/**
	* Find seat holds by seatHoldId
	*
	* @param unique seatHoldId; if seatHoldId <= 0 then return all seat holds
	* @return list of seat holds
	*/
	public List<SeatHold> findSeatHolds(int seatHoldId) {
		
		List<SeatHold> holds = new ArrayList<>();
		if(seatHoldId > 0) {
        	SeatHold seatHold = seatHolds.get(new Integer(seatHoldId));
			if (seatHold != null)
			{
				holds.add(seatHold);
			}
		}
		else  //return all seat holds
		{
	        for (Map.Entry<Integer, SeatHold> entry : seatHolds.entrySet()) {
	        	SeatHold seatHold = entry.getValue();
	        	holds.add(seatHold);
	        }
		}
		return holds;
		
	}

	/**
	* Find seat reservation by reservation code
	*
	* @param unique reservation code; if reservation code is blank then return all seat reservations
	* @return list of reservations
	*/
	public List<SeatReservation> findSeatReservations(String reservationCode) {
		
		List<SeatReservation> reservations = new ArrayList<>();
		if(reservationCode.length() > 0) {
			SeatReservation seatReservation = seatReservations.get(reservationCode);
			if (seatReservation != null)
			{
				reservations.add(seatReservation);
			}
		}
		else  //return all seat reservations
		{
	        for (Map.Entry<String, SeatReservation> entry : seatReservations.entrySet()) {
	        	SeatReservation seatReservation = entry.getValue();
	        	reservations.add(seatReservation);
	        }
		}
		return reservations;
		
	}

	/**
	* Print venue seat map
	*
	* @return String representing venue map
	*/
	public String printVenueMap() {
		
		int lineLenght = 0;
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < this.venueRows; row ++) {
		    for (int col = 0; col < this.venueColumns; col++) {
		    	Seat seat = this.venueSeats[row][col];
		    	str.append(seat.getSeatInfo(Constants.SEAT_INFO_STATUS));
		    	str.append(" ");
		    }
		    if (row == 0) {
		    	lineLenght = str.length();
		    }
			str.append(System.lineSeparator());
		}
		
		StringBuilder stage1 = new StringBuilder();
		StringBuilder stage2 = new StringBuilder();
		String stage = "[[  STAGE  ]]";
		if (lineLenght >= 13) {
			for(int i = 0; i < lineLenght-1; i++) {
				stage1.append("-");
			}
			stage1 = stage1.replace(stage1.length()/2-6, stage1.length()/2+7, stage);
			stage1.append(System.lineSeparator());
			
			for(int i = 0; i < lineLenght-1; i++) {
				stage2.append("-");
			}
			stage2.append(System.lineSeparator());
		}
		else {
			stage1.append(stage);
			stage1.append(System.lineSeparator());
			
			for(int i = 0; i < stage.length(); i++) {
				stage2.append("-");
			}
			stage2.append(System.lineSeparator());
		}

		StringBuilder info = new StringBuilder();
		info.append(System.lineSeparator());
		info.append("Total Seats: " + getNumberSeats(' '));
		info.append(System.lineSeparator());
		info.append("Available Seats: " + getNumberSeats(Constants.SEAT_AVAILABLE));
		info.append(System.lineSeparator());
		info.append("Seats On Hold: " + getNumberSeats(Constants.SEAT_HOLD));
		info.append(System.lineSeparator());
		info.append("Reserved Seats: " + getNumberSeats(Constants.SEAT_RESERVED));

		return stage1.toString() + stage2.toString() + str.toString() + info.toString();
	}

	
}
