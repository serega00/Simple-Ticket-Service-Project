Simple Ticket Service Project
=============================

This project is built as programming challenge for WalmartLabs. Its built using Eclipse Java EE IDE Version: Oxygen.2 Release (4.7.2) using Maven 4.0.0 as the build tool. 

The project contains 12 classes (package org.walmartlabs.ticketservice):
1. App class - contains main method to run the app and prints out all menus and messages
2. Constants class - contains constants used throughout the project
3. Seat class - represents a single seat in the venue
4. SeatArrangement class - this is abstract class that contains data and methods common for all seat arrangements like holds and reservations
5. SeatArrangementFactory class - class that produces SeatArrangements
6. SeatHold class - extends SeatArrangement class, represent a seat hold
7. SeatReservation class - extends SeatArrangement class, represent a seat reservation
8. TicketService interface - must be used by the original specification
9. TicketServiceImpl class - implements all TicketService interface methods and adds some additional functionalities
10. TicketServiceException class - exception class that is used by this app
11. TestTicketServiceImpl class - class that implements JUnit tests for all public methods of the TicketServiceImpl class
12. TestRunner class - main JUnit runner class


Original challenge description
------------------------------

Implement a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

For example, see the seating arrangement below.

----------[[ STAGE ]]----------
---------------------------------
sssssssssssssssssssssssssssssssss
sssssssssssssssssssssssssssssssss
sssssssssssssssssssssssssssssssss
sssssssssssssssssssssssssssssssss
sssssssssssssssssssssssssssssssss
sssssssssssssssssssssssssssssssss
sssssssssssssssssssssssssssssssss
sssssssssssssssssssssssssssssssss
sssssssssssssssssssssssssssssssss

Your homework assignment is to design and write a Ticket Service that provides the following functions:

Find the number of seats available within the venue
Note: available seats are seats that are neither held nor reserved.

Find and hold the best available seats on behalf of a customer
Note: each ticket hold should expire within a set number of seconds.

Reserve and commit a specific group of held seats for a customer
Requirements

The ticket service implementation should be written in Java
The solution and tests should build and execute entirely via the command line using either Maven or Gradle as the build tool
A README file should be included in your submission that documents your assumptions and includes instructions for building the solution and executing the tests

Implementation mechanisms such as disk-based storage, a REST API, and a front-end GUI are not required
Your solution will be reviewed by engineers that you will be working with if you join the Walmart Technology team. We are interested in seeing how you design, code, and test software.

You will need to implement the following interface. The design of the SeatHold object is entirely up to you.

public interface TicketService {
/**
* The number of seats in the venue that are neither held nor reserved
*
* @return the number of tickets available in the venue
*/
int numSeatsAvailable();
/**
* Find and hold the best available seats for a customer
*
* @param numSeats the number of seats to find and hold
* @param customerEmail unique identifier for the customer
* @return a SeatHold object identifying the specific seats and related
information
*/
SeatHold findAndHoldSeats(int numSeats, String customerEmail);
/**
* Commit seats held for a specific customer
*
* @param seatHoldId the seat hold identifier
* @param customerEmail the email address of the customer to which the
seat hold is assigned
* @return a reservation confirmation code
*/
String reserveSeats(int seatHoldId, String customerEmail);
}



The following tools must be installed on your machine
-----------------------------------------------------
1. Eclipse IDE
2. JAVA 8 JDK binary
3. Maven 4.0.0 binary
4. JAVA and Maven binary locations must be in the System Path



How to open, build and run the project in Eclipse?
--------------------------------------------------
Open in Eclipse IDE:
1. File -> Open Projects From File System...
2. Type root directory of the project in Import Source text box or use Directory button to find directory
3. Click Finish button to open the project in IDE

Build in Eclipse IDE:
Project -> Build Project

Run in Eclipse IDE:
1. Run -> Run Configurations...
2. Switch to Arguments tab and put input arguments in Program Arguments text box. For example: 10 10 60
3. Click Run button. The app starts running in Console tab



How to build jar using Maven?
-----------------------------
1. Goto project root directory
2. Run command: mvn clean install
3. Maven will build ticketservice-0.0.1-SNAPSHOT.jar file in <project root directory>\target



How to run the app from command line?
-------------------------------------

1. Goto location where ticketservice-0.0.1-SNAPSHOT.jar is located
2. Run ticket service app as following:

> java -cp ticketservice-0.0.1-SNAPSHOT.jar org.walmartlabs.ticketservice.App <venueRows> <venueColumns> <holdTimeout>

<venueRows> - number of rows in the venue; must enter number between 1-26 
<venueColumns> - number of columns (seats in one row) in the venue; must enter number greater than 0.
<holdTimeout> - hold timeout in seconds, time in which seat hold expires (seats are released from hold). Must enter number greater than 5.

Example:
java -cp ticketservice-0.0.1-SNAPSHOT.jar org.walmartlabs.ticketservice.App 10 10 60



Using Ticket Service App
------------------------

Example of venue seat map:

---[[  STAGE  ]]---
-------------------
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 


Main menu
---------

=====================
Simple Ticket Service
=====================
Menu Options:
1. Show Number of Available Seats
2. Hold Seats
3. Reserve Seats
4. Find Seat Holds
5. Find Seat Reservations
6. Show Venue Seat Map
7. Exit the program

Please select an option from 1-7


1. Show Number of Available Seats
---------------------------------
This option shows number of available seats in the venue

=====================
Simple Ticket Service
=====================
Menu Options:
1. Show Number of Available Seats
2. Hold Seats
3. Reserve Seats
4. Find Seat Holds
5. Find Seat Reservations
6. Show Venue Seat Map
7. Exit the program

Please select an option from 1-7
1

Number of Available Seats: 100


2. Hold Seats
-------------
This option lets user to create seat hold and displays seat hold info if action is successful. 
Errors will be displayed in the following cases:
- if entered number of seats to hold is <= 0
- if entered number of seats to hold is greater then number of available seats
- if entered email is blank

=====================
Simple Ticket Service
=====================
Menu Options:
1. Show Number of Available Seats
2. Hold Seats
3. Reserve Seats
4. Find Seat Holds
5. Find Seat Reservations
6. Show Venue Seat Map
7. Exit the program

Please select an option from 1-7
2
Please enter number of seats to hold
5
Please enter customer email
user@test.com
Seat Hold [ hold id: 1, customer email: user@test.com, created: 03/12/2018 09:41, 5 seats: A1 A2 A3 A4 A5 ]


If seat hold expires before its reserved, the following message will be displayed:

Seat Hold with id 1 was released


3. Reserve Seats
----------------
This option lets user to reserve seats that were previously on hold and displays seat reservation info if action is successful. 
Errors will be displayed in the following cases:
- if entered seat hold id is invalid (hold with such id does not exist or expired)
- if entered email does not match to email used to create seat hold

=====================
Simple Ticket Service
=====================
Menu Options:
1. Show Number of Available Seats
2. Hold Seats
3. Reserve Seats
4. Find Seat Holds
5. Find Seat Reservations
6. Show Venue Seat Map
7. Exit the program

Please select an option from 1-7
3
Please enter valid seat hold id
1
Please enter customer email
user@test.com
Seat Reservation [ reservation code: user@test.com-1, customer email: user@test.com, created: 03/12/2018 09:44, 5 seats: A1 A2 A3 A4 A5 ]


4. Find Seat Holds
------------------
This option lets user to find and display seat holds using seat hold id. 
If entered seat hold id is blank than the app will display all current seat holds in the system.

=====================
Simple Ticket Service
=====================
Menu Options:
1. Show Number of Available Seats
2. Hold Seats
3. Reserve Seats
4. Find Seat Holds
5. Find Seat Reservations
6. Show Venue Seat Map
7. Exit the program

Please select an option from 1-7
4
Please enter valid seat hold id [enter blank to display all holds]

Seat Hold [ hold id: 1, customer email: u1@test.com, created: 03/12/2018 10:10, 5 seats: A1 A2 A3 A4 A5 ]
Seat Hold [ hold id: 2, customer email: u2@test.com, created: 03/12/2018 10:10, 5 seats: A6 A7 A8 A9 A10 ]
Seat Hold [ hold id: 3, customer email: u3@test.com, created: 03/12/2018 10:10, 6 seats: B1 B2 B3 B4 B5 B6 ]


5. Find Seat Reservations
-------------------------
This option lets user to find and display seat reservations using reservation code. 
If entered reservation code is blank than the app will display all current seat reservations in the system.

=====================
Simple Ticket Service
=====================
Menu Options:
1. Show Number of Available Seats
2. Hold Seats
3. Reserve Seats
4. Find Seat Holds
5. Find Seat Reservations
6. Show Venue Seat Map
7. Exit the program

Please select an option from 1-7
5
Please enter valid seat reservation code [enter blank to display all reservations]

Seat Reservation [ reservation code: u2@test.com-2, customer email: u2@test.com, created: 03/12/2018 10:12, 5 seats: A6 A7 A8 A9 A10 ]
Seat Reservation [ reservation code: u1@test.com-1, customer email: u1@test.com, created: 03/12/2018 10:12, 5 seats: A1 A2 A3 A4 A5 ]


6. Show Venue Map
-----------------
This option lets user to view venue map with all available, on hold and reserved seats: A - seat is available, H - seat is on hold, R - seat is reserved
Also, this option displays info about number of seats in all categories.

=====================
Simple Ticket Service
=====================
Menu Options:
1. Show Number of Available Seats
2. Hold Seats
3. Reserve Seats
4. Find Seat Holds
5. Find Seat Reservations
6. Show Venue Seat Map
7. Exit the program

Please select an option from 1-7
6

---[[  STAGE  ]]---
-------------------
R R R R R H H A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 
A A A A A A A A A A 

Total Seats: 100
Available Seats: 93
Seats On Hold: 2
Reserved Seats: 5


7. Exit the program
-------------------
This option lets user to quit the app.

=====================
Simple Ticket Service
=====================
Menu Options:
1. Show Number of Available Seats
2. Hold Seats
3. Reserve Seats
4. Find Seat Holds
5. Find Seat Reservations
6. Show Venue Seat Map
7. Exit the program

Please select an option from 1-7
7

You have quit the program