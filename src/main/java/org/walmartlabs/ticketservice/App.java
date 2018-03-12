package org.walmartlabs.ticketservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class App 
{
    public static void main( String[] args )
    {
    	
    	// read input arguments: arg0 - venueRows, arg1 - venueColumns, arg2 - holdTimeout
    	int venueRows = 0;
    	int venueColumns = 0;
    	int holdTimeout = 0; 
    	try {
    		
	    	venueRows = Integer.parseInt(args[0]);
	    	if (venueRows <= 0 || venueRows > 26) {
	            System.out.println("venueRows must be between 1-26. You have quit the program");
	            System.exit(0);
	    	}

	    	venueColumns = Integer.parseInt(args[1]);
	    	if (venueColumns <= 0) {
	            System.out.println("venueColumns must be greater than 0. You have quit the program");
	            System.exit(0);
	    	}
	    	
	    	holdTimeout = Integer.parseInt(args[2]);
	    	if (holdTimeout < 5) {
	            System.out.println("holdTimeout must be greater than 5. You have quit the program");
	            System.exit(0);
	    	}

    	} catch (RuntimeException ex) {
            System.out.println("Unable to read input arguments. You have quit the program");
            System.exit(0);
	   	}
       
    	App app = new App();
    	app.runTicketService(venueRows, venueColumns, holdTimeout*1000);
    }
    
    public void runTicketService(int venueRows, int venueColumns, int holdTimeout) {
    	
		// create arrangement factory
		SeatArrangementFactory seatArrangementFactory = new SeatArrangementFactory();
		
		//Creating TicketServiceImpl and injecting venue, arrangement factory and hold timeout into TicketServiceImpl
		TicketServiceImpl ticketService = new TicketServiceImpl(venueRows, venueColumns, holdTimeout, seatArrangementFactory);

		// main menu loop
		int input = 0;
        while (input != 7){

            System.out.println("");
            System.out.println("=====================");
            System.out.println("Simple Ticket Service");
            System.out.println("=====================");
            System.out.println("Menu Options:");
            System.out.println("1. Show Number of Available Seats");
            System.out.println("2. Hold Seats");
            System.out.println("3. Reserve Seats");
            System.out.println("4. Find Seat Holds");
            System.out.println("5. Find Seat Reservations");
            System.out.println("6. Show Venue Seat Map");
            System.out.println("7. Exit the program");
            System.out.println("");
            System.out.print("Please select an option from 1-7\n");
            
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            try {
                input = Integer.parseInt(br.readLine());
                
                if(input < 0 || input > 7) {
                	
                    System.out.println("\nYou have entered an invalid selection, please try again");
                    
                } else if(input == 1) {
                	
                	System.out.println("\nNumber of Available Seats: " + ticketService.numSeatsAvailable());
                	
                } else if(input == 2) {
                	
                	Integer numberSeats = 0;
                	String email = "";
                	
                    try {
                        System.out.print("Please enter number of seats to hold\n");
                        numberSeats = Integer.parseInt(br.readLine());
	                } catch (RuntimeException ioe) {
            			System.out.println("Error: Invalid number entered");
            			continue;
	                }

                    try {
                        System.out.print("Please enter customer email\n");
                        email = new String(br.readLine());
	                } catch (RuntimeException ioe) {
            			System.out.println("Error: Invalid string entered");
            			continue;
	                }
                        
                    try {
                        SeatHold seatHold = ticketService.findAndHoldSeats(numberSeats, email);
                        if (seatHold != null) {
                    		System.out.println(seatHold.toString());
                        }
	                } catch (TicketServiceException e) {
            			System.out.println(e.getError());
 	                }

                } else if(input == 3) {
                	
                	String email = "";
                    Integer holdId = 0;
                    try {
                        System.out.print("Please enter valid seat hold id\n");
                        holdId = Integer.parseInt(br.readLine());
	                } catch (RuntimeException ioe) {
            			System.out.println("Error: Invalid number entered");
            			continue;
	                }
                    
                    try {
                        System.out.print("Please enter customer email\n");
                        email = new String(br.readLine());
	                } catch (RuntimeException ioe) {
            			System.out.println("Error: Invalid string entered");
            			continue;
	                }
                    
                    try {
                        String reservationCode = ticketService.reserveSeats(holdId, email);
                        if (reservationCode.length() > 0) {
                        	List<SeatReservation> reservations = ticketService.findSeatReservations(reservationCode);
                        	if (reservations.size() == 0) {
                    			System.out.println("No reservations found");
                        	}
                        	else {
                        		for(SeatReservation reservation : reservations) {
                        			System.out.println(reservation.toString());
                        		}
                        	}
                        }
	                } catch (TicketServiceException e) {
            			System.out.println(e.getError());
	                }
                    
                } else if(input == 4) {
                	
                    Integer holdId = 0;
                    try {
                        System.out.print("Please enter valid seat hold id [enter blank to display all holds]\n");
                        holdId = Integer.parseInt(br.readLine());
	                } catch (RuntimeException ioe) {
	                	holdId = 0;
	                }
                	List<SeatHold> holds = ticketService.findSeatHolds(holdId);
                	if (holds.size() == 0) {
            			System.out.println("No holds found");
                	}
                	else {
                		for(SeatHold hold : holds) {
                			System.out.println(hold.toString());
                		}
                	}
                    
                } else if(input == 5) {
                	
                    String reservationCode = "";
                    try {
                        System.out.print("Please enter valid seat reservation code [enter blank to display all reservations]\n");
                        reservationCode = new String(br.readLine());
	                } catch (RuntimeException ioe) {
	                	reservationCode = "";
	                }
                	List<SeatReservation> reservations = ticketService.findSeatReservations(reservationCode);
                	if (reservations.size() == 0) {
            			System.out.println("No reservations found");
                	}
                	else {
                		for(SeatReservation reservation : reservations) {
                			System.out.println(reservation.toString());
                		}
                	}
                    
                } else if(input == 6) {
                	
                    System.out.println("");
                    System.out.println(ticketService.printVenueMap());
                    
                } else if(input == 7) {
                    System.out.println("\nYou have quit the program");
                    System.exit(0);
                }
            } catch (IOException ioe) {
                System.out.println("\nIO error trying to read your input!");
            }
        }
    }
}
