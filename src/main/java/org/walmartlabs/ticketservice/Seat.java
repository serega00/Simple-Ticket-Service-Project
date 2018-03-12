package org.walmartlabs.ticketservice;

public class Seat {

	private int row;
	private int column;
	private char seatStatus; // A-available, H-on hold, R-reserved
	
	public Seat() {
	}
	
	public Seat(int row, int column) {
		super();
		this.row = row;
		this.column = column;
		this.seatStatus = Constants.SEAT_AVAILABLE;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public char getSeatStatus() {
		return seatStatus;
	}

	public String getSeatInfo(String option) {
		StringBuilder str = new StringBuilder();
		if (option.equals(Constants.SEAT_INFO_FULL)) {
			str.append(Constants.ALPHABET[row]);
			str.append(column+1);
			str.append("-");
			str.append(seatStatus);
		}
		else if (option.equals(Constants.SEAT_INFO_SEATNUMBER)) {
			str.append(Constants.ALPHABET[row]);
			str.append(column+1);
		}
		else if (option.equals(Constants.SEAT_INFO_STATUS)) {
			str.append(seatStatus);
		}
		return str.toString();
	}

	public boolean holdSeat() {
		boolean result = false;
		this.seatStatus = Constants.SEAT_HOLD;
		return result;
	}
	
	public boolean reserveSeat() {
		boolean result = false;
		this.seatStatus = Constants.SEAT_RESERVED;
		return result;
	}

	public boolean releaseSeat() {
		boolean result = false;
		this.seatStatus = Constants.SEAT_AVAILABLE;
		return result;
	}

}
