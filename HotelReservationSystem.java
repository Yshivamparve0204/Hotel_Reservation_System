package mysql1;
import java.util.*;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class HotelReservationSystem {
   private static final String url ="jdbc:mysql://localhost:3306/shiva";
   private static final String username ="root";
   private static final String password = "Pass@123";
   
	public static void main(String[] args) throws ClassNotFoundException,SQLException {
		try
		{
			Class.forName("con.mysql.cj.jdbc.Driver");
			
		}catch (ClassNotFoundException e){
			System.out.println(e.getMessage());
			
			
		
		try {
			Connection connection = DriverManager.getConnection(url,username,password);
			while(true) {
				System.out.println();
				System.out.println("Hotel Management System");
				Scanner scanner = new Scanner(System.in);
				System.out.println("1. Reserve a room");
				System.out.println("2. View Reservation ");
				System.out.println("3. Get Room Number");
				System.out.println("4. Update Reservation");
				System.out.println("5. Delete Reservation");
				System.out.println("0. Exit");
				System.out.println("Choose an Option");
				int choice = scanner.nextInt();
				switch (choice) {
				case 1:
					reserveRoom(connection,scanner);
				    break;
				case 2:
					viewReservations(connection);
					break;
			    case 3:
				    getRoomNumber(connection,scanner);
			        break;
				case 4:
					UpdateReservation(connection,scanner);
					break;
				case 5:
				    deleteReservation(connection,scanner);
					 break;
				case 0:
					exit();
					scanner.close();
					return;
				default:
					System.out.println("Invalid choice. Try again.");
				}


	    
				}
			}catch(SQLException e1) {
				System.out.println(e.getMessage());
			} catch(InterruptedException e2) {
		  
				throw new RuntimeException(e);
			}}
		}
		private static void reserveRoom(Connection connection, Scanner scanner) {
			try {
				System.out.println("enter reservation ID : ");
				int reservationID = scanner.nextInt();
				System.out.println("enter guest name : ");
				String guestName = scanner.next();
				scanner.nextLine();
				System.out.println("enter room number : ");
				int roomNumber = scanner.nextInt();
				System.out.println("enter contact number : ");
				String contactNumber = scanner.next();
				System.out.println("enter reservation Date : ");
				String reservationDate = scanner.next();
				scanner.nextLine();
				
				
				String sql = "INSERT INTO reservations (reservation_id,guest_name,room_number,contact_number,reservation_date)"+
				"VALUES('"+ reservationID +"','"+ guestName +"','"+ roomNumber +"','" + contactNumber + "','" + reservationDate;
				try(Statement statement = connection.createStatement()){
					int affectedRows = statement.executeUpdate(sql);
					if(affectedRows > 0) {
						System.out.println("reservation successful!!");
					}else {
						System.out.println("reservation failed");
					}
				}

			
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		private static void viewReservations(Connection connection) throws SQLException {
			String sql = "SELECT reservation_id ,guest_name,room_number,contact_number,reservation_date";
			try(Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(sql)) {
				
				System.out.println("current Reservation :");
				System.out.println("+----------------+---------------+-----------------+---------------+----------------+");
				System.out.println("| Reservation ID | Guest         |Room Number      | Contact Number| Reservation Date");
				System.out.println("+----------------+---------------+-----------------+---------------+----------------+");
				
				
				
		while(resultSet.next()) {
			int reservationID = resultSet.getInt("reservation_id");
			String guestName = resultSet.getString("guest_name");
			int roomNumber = resultSet.getInt("room_number");
			String contactNumber = resultSet.getString("contact_number");
			String reservationDate = resultSet.getString("reservation_date");
			
			
			System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",
					reservationID,guestName,roomNumber,contactNumber,reservationDate);
			
		}
		System.out.println("-----------+-------------+--------------+-------------+-------------+");
			
			
			}
		}
	   private static void getRoomNumber(Connection connection,Scanner scanner) {
		   try {
			   System.out.println("enter reservation ID :");
	    	   int reservationID = scanner.nextInt();
			   System.out.println("enter guest name :");
			   String guestName = scanner.next();
			   
			   
			   String sql = "SELECT room_number FROM reservations "+
			   "WHERE reservation-id = " + reservationID +
			   "AND guest_name = '"+guestName+"'";
			   
			   try(Statement statement = connection .createStatement();
					   ResultSet resultSet = statement.executeQuery(sql)){
				   
				   if(resultSet.next()) {
					   int roomNumber = resultSet.getInt("room_number");
					   System.out.println("room number for reservation id "+ reservationID +
							   " and guest " + guestName + "is :"+ roomNumber);
					   }else {
						   System.out.println("reservation not found for the given id and guest name.");
						   
					   }
			   }
		   }catch (SQLException e) {
			   e.printStackTrace();
		   }
	   }
	   
	   private static void UpdateReservation(Connection connection , Scanner scanner) {
		   try {
			   System.out.println("enter reservation id to update :");
			   int reservationID = scanner.nextInt();
			   scanner.nextLine();
			   
			   if(!reservationExists(connection,reservationID)) {
				   System.out.println("reservation not found for the given ID ");
				   return;
			   }
			   
			   System.out.println("enter new guest name :");
			   String newGuestName = scanner.nextLine();
			   System.out.println("enter new room number :");
			   int newRoomNumber = scanner.nextInt();
			   System.out.println("enter new contact number :");
			   String newContactNumber = scanner.next();
			   
			   String sql = "UPDATE reservation SET guest_name ='"+ newGuestName + "', " +
			   "room_number = " + newRoomNumber + ", " +
					   "contact_number = '" + newContactNumber + "' " +
			   "WHERE reservation_id = " + reservationID;
			   
			   try(Statement statement = connection.createStatement()){
				   int affectedRows = statement.executeUpdate(sql);
				   
				   if(affectedRows > 0) {
					   System.out.println("reservation updated successfully!");
				   }else {
					   System.out.println("reservation update failed.");
				   }
			   }
		   }catch (SQLException e) {
			   e.printStackTrace();
		   }
	   }
	   
	   private static void deleteReservation(Connection connection,Scanner scanner) {
		   try {
			   System.out.println("enter reservation ID to deleten:");
			   int reservationID = scanner.nextInt();
			   
			   if(!reservationExists(connection,reservationID)) {
				   System.out.println("reservatiojn not found for the given id :");
				   return;
			   }
			   String sql = "DELETE FROM reservations WHERE reservation_id = "+ reservationID;
			   
			   try(Statement statement = connection.createStatement()){
				   int affectedRows = statement.executeUpdate(sql);
				   
				   if (affectedRows > 0) {
					   System.out.println("reservation deleted successfully!");
				   }else {
					   System.out.println("reservation deletion failed.");
				   }
			   }
		   }catch (SQLException e) {
			   e.printStackTrace();
		   }
	   }
	   
	   private static boolean reservationExists(Connection connection,int reservationID) {
		   try {
			   String sql = "SELECT reservation_id FROM reservation WHERE reservation_id = "+ reservationID;
			   
			   
			   try(Statement statement = connection.createStatement();
					   ResultSet resultSet = statement.executeQuery(sql)){
				   return resultSet.next();
			   }
		   }catch(SQLException e) {
			   e.printStackTrace();
			   return false;
		   }
	   }
	   
	   public static void exit() throws InterruptedException {
		   System.out.println("Existing system");
		   int i = 5;
		   while(i!=0) {
			   System.out.print(".");
			   Thread.sleep(1000);
			   i--;
		   }
		   System.out.println();
		   System.out.println("thank you for using hotel reservation system!!!!");
	   
			
		}
	

	}


