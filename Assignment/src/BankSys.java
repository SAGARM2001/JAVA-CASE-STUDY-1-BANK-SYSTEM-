import java.util.*;
import java.sql.*;


class Customer{
	public int accno;
	public String accname;
	public double accbal;
	
	//Default Constructor
	public Customer() {
		
	}

	//Parameterized Constructor
	public Customer(int accno, String accname, double accbal) {
		this.accno = accno;
		this.accname = accname;
		this.accbal = accbal;
	}

	//Getter for Account No
	public int getAccno() {
		return accno;
	}
}

//ADMINISTRATOR CLASS
class Administrator {
	
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/java_assignment";
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "Sagar@2001"; 

    
    //ADDING CUSTOMER METHOD
    public void addCustomer(int accNo, String accName, double accBal) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String insertQuery = "INSERT INTO customers (accno, accname, accbal) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, accNo);
            preparedStatement.setString(2, accName);
            preparedStatement.setDouble(3, accBal);
            preparedStatement.executeUpdate();
            
            preparedStatement.close();
            connection.close();
            System.out.println("Customer added successfully.");
            System.out.println("----------------------------");
        } catch (SQLException e) {
            System.out.println("Error adding customer.");
        }
    }
    
    
    //SEARCH CUSTOMER METHOD
    public Customer searchCustomer(String nameOrAccNo) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String selectQuery = "SELECT * FROM customers WHERE accname = ? OR accno = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, nameOrAccNo);
            preparedStatement.setString(2, nameOrAccNo);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int accNo = resultSet.getInt("accno");
                String accName = resultSet.getString("accname");
                double accBal = resultSet.getDouble("accbal");
                preparedStatement.close();
                connection.close();
                return new Customer(accNo, accName, accBal);
                
            } else {
                preparedStatement.close();
                connection.close();
                return null; // Customer not found
            }
        } catch (SQLException e) {
            return null;
        }
    }

    
    //MODIFY CUSTOMER METHOD
    public void modifyCustomer(int accNo, String newName, double newBalance) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String updateQuery = "UPDATE customers SET accname = ?, accbal = ? WHERE accno = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newName);
            preparedStatement.setDouble(2, newBalance);
            preparedStatement.setInt(3, accNo);
            preparedStatement.executeUpdate();
            
            preparedStatement.close();
            connection.close();
            System.out.println("Customer details modified successfully.");
            System.out.println("----------------------------");
        } catch (SQLException e) {
            System.out.println("Error modifying customer details.");
        }
    }

    
    //GET BALANCE ENQUIRY METHOD
    public double getBalance(int accNo) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String selectQuery = "SELECT accbal FROM customers WHERE accno = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, accNo);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double accBal = resultSet.getDouble("accbal");
                preparedStatement.close();
                connection.close();
                return accBal;
            } else {
                preparedStatement.close();
                connection.close();
                return -1; // Account not found
            }
        } catch (SQLException e) {
            return -1;
        }
    }

    
    //DELETE CUSTOMER METHOD
    public void deleteCustomer(int accNo) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String deleteQuery = "DELETE FROM customers WHERE accno = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, accNo);
            preparedStatement.executeUpdate();
            
            preparedStatement.close();
            connection.close();
            System.out.println("Customer account deleted successfully.");
            System.out.println("----------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting customer account.");
        }
    }
    
    
    //Display all customers
    public void displayAllCustomers() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String selectQuery = "SELECT * FROM customers";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int accNo = resultSet.getInt("accno");
                String accName = resultSet.getString("accname");
                double accBal = resultSet.getDouble("accbal");
                System.out.println("Account Number: " + accNo);
                System.out.println("Account Name: " + accName);
                System.out.println("Account Balance: " + accBal);
                System.out.println("-------------------------");
            }
            
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error displaying customer details.");
        }
    }
    
    
    //Is Valid Customer Method
    public boolean isValidCustomer(int accNo) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String selectQuery = "SELECT COUNT(*) FROM customers WHERE accno = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, accNo);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            boolean isValid = false;
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    isValid = true;
                }
            }
            
            resultSet.close();
            preparedStatement.close();
            connection.close();
            
            return isValid;
        } catch (SQLException e) {
            return false;
        }
    }

}


//CUSTOMERACCOUNT CLASS
class CustomerAccount {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/java_assignment";
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "Sagar@2001";
	
    public void deposit(int accNo, double amount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String updateQuery = "UPDATE customers SET accbal = accbal + ? WHERE accno = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, accNo);
            preparedStatement.executeUpdate();

            // Add transaction to history
            String insertHistoryQuery = "INSERT INTO transaction_history (accno, transaction_type, amount) VALUES (?, ?, ?)";
            PreparedStatement historyStatement = connection.prepareStatement(insertHistoryQuery);
            historyStatement.setInt(1, accNo);
            historyStatement.setString(2, "Deposit");
            historyStatement.setDouble(3, amount);
            historyStatement.executeUpdate();

            preparedStatement.close();
            historyStatement.close();
            connection.close();
            System.out.println("Amount deposited successfully.");
            
        } catch (SQLException e) {
            System.out.println("Error depositing amount.");
        }
    }

    // Withdraw Method
    public void withdraw(int accNo, double amount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Checking sufficient balance
            String selectQuery = "SELECT accbal FROM customers WHERE accno = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, accNo);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                double accBal = resultSet.getDouble("accbal");
                if (accBal >= amount) {
                    // After Withdraw Amt is deducted
                    String updateQuery = "UPDATE customers SET accbal = accbal - ? WHERE accno = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setDouble(1, amount);
                    updateStatement.setInt(2, accNo);
                    updateStatement.executeUpdate();

                    // Adding details of transaction to history
                    String insertHistoryQuery = "INSERT INTO transaction_history (accno, transaction_type, amount) VALUES (?, ?, ?)";
                    PreparedStatement historyStatement = connection.prepareStatement(insertHistoryQuery);
                    historyStatement.setInt(1, accNo);
                    historyStatement.setString(2, "Withdrawal");
                    historyStatement.setDouble(3, amount);
                    historyStatement.executeUpdate();

                    updateStatement.close();
                    historyStatement.close();
                    System.out.println("Amount withdrawn successfully.");
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Account not found.");
            }

            resultSet.close();
            selectStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error withdrawing amount.");
        }
    }
    
    
    
    //Print MiniStatement
    public void printMiniStatement(int accNo) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String selectQuery = "SELECT * FROM transaction_history WHERE accno = ? ORDER BY id DESC LIMIT 5";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, accNo);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Mini Statement for Account Number: " + accNo);
            while (resultSet.next()) {
                String transactionType = resultSet.getString("transaction_type");
                double amount = resultSet.getDouble("amount");
                System.out.println(transactionType + ": " + amount);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error printing mini statement.");
        }
    }
    
    //Valid Customer or not
    public boolean isValidCustomer(int accNo) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String selectQuery = "SELECT COUNT(*) FROM customers WHERE accno = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, accNo);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            boolean isValid = false;
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    isValid = true;
                }
            }
            
            resultSet.close();
            preparedStatement.close();
            connection.close();
            
            return isValid;
        } catch (SQLException e) {
            return false;
        }
    }
    
    
    //Transfer Method
    public void transfer(int fromAccNo, int toAccNo, double amount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Check if sender account exists and has sufficient balance
            String selectSenderQuery = "SELECT accbal FROM customers WHERE accno = ?";
            PreparedStatement selectSenderStatement = connection.prepareStatement(selectSenderQuery);
            selectSenderStatement.setInt(1, fromAccNo);
            ResultSet senderResultSet = selectSenderStatement.executeQuery();

            if (senderResultSet.next()) {
                double senderBal = senderResultSet.getDouble("accbal");
                if (senderBal >= amount) {
                    // Check if recipient account exists
                    if (isValidCustomer(toAccNo)) {
                        // Deducting amount from sender account
                        String updateSenderQuery = "UPDATE customers SET accbal = accbal - ? WHERE accno = ?";
                        PreparedStatement updateSenderStatement = connection.prepareStatement(updateSenderQuery);
                        updateSenderStatement.setDouble(1, amount);
                        updateSenderStatement.setInt(2, fromAccNo);
                        updateSenderStatement.executeUpdate();

                        // Add transaction to sender's history
                        String insertSenderHistoryQuery = "INSERT INTO transaction_history (accno, transaction_type, amount) VALUES (?, ?, ?)";
                        PreparedStatement senderHistoryStatement = connection.prepareStatement(insertSenderHistoryQuery);
                        senderHistoryStatement.setInt(1, fromAccNo);
                        senderHistoryStatement.setString(2, "Transfer to " + toAccNo);
                        senderHistoryStatement.setDouble(3, amount);
                        senderHistoryStatement.executeUpdate();

                        // Adding amount to recipient account
                        String updateReceiverQuery = "UPDATE customers SET accbal = accbal + ? WHERE accno = ?";
                        PreparedStatement updateReceiverStatement = connection.prepareStatement(updateReceiverQuery);
                        updateReceiverStatement.setDouble(1, amount);
                        updateReceiverStatement.setInt(2, toAccNo);
                        updateReceiverStatement.executeUpdate();

                        // Adding details of transaction to recipient's history
                        String insertReceiverHistoryQuery = "INSERT INTO transaction_history (accno, transaction_type, amount) VALUES (?, ?, ?)";
                        PreparedStatement receiverHistoryStatement = connection.prepareStatement(insertReceiverHistoryQuery);
                        receiverHistoryStatement.setInt(1, toAccNo);
                        receiverHistoryStatement.setString(2, "Transfer from " + fromAccNo);
                        receiverHistoryStatement.setDouble(3, amount);
                        receiverHistoryStatement.executeUpdate();

                        System.out.println("Amount transferred successfully.");
                    } else {
                        System.out.println("Recipient account not found.");
                    }
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Sender account not found.");
            }

            senderResultSet.close();
            selectSenderStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error transferring amount.");
        }
    }

}


//Main Method where login logic will be implemented
public class BankSys {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Administrator admin = new Administrator();
        CustomerAccount customerAccount = new CustomerAccount();

        while (true) {
            System.out.println("----------------------------");
            System.out.println("1. Administrator Login");
            System.out.println("2. Customer Login");
            System.out.println("3. Exit");
            System.out.println("----------------------------");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter Administrator Username: ");
                String adminUsername = scanner.next();
                System.out.print("Enter Administrator Password: ");
                String adminPassword = scanner.next();
                System.out.println("----------------------------");

                if (adminUsername.equals("admin") && adminPassword.equals("adminpass")) {
                    // Administrator is logged in
                    while (true) {
                        // Administrator Menu
                        System.out.println("Administrator Menu:");
                        System.out.println("1. Add Customer");
                        System.out.println("2. Search Customer");
                        System.out.println("3. Modify Customer Details");
                        System.out.println("4. Inquire about Balance");
                        System.out.println("5. Delete Customer Account");
                        System.out.println("6. Display All Customers");
                        System.out.println("7. Logout");
                        System.out.println("----------------------------");
                        System.out.print("Enter your choice: ");
                        int adminChoice = scanner.nextInt();

                        // Administrator Actions
                        switch (adminChoice) {
                            case 1:
                                // Adding Customer
                                System.out.print("Enter Account Number: ");
                                int accNo = scanner.nextInt();
                                System.out.print("Enter Account Name: ");
                                String accName = scanner.next();
                                System.out.print("Enter Initial Balance: ");
                                double accBal = scanner.nextDouble();
                                
                                admin.addCustomer(accNo, accName, accBal);
                                break;
                            case 2:
                                // Search Customer
                                System.out.print("Enter Customer Name or Account Number: ");
                                String searchQuery = scanner.next();
                                
                                Customer foundCustomer = admin.searchCustomer(searchQuery);
                                if (foundCustomer != null) {
                                    System.out.println("Customer found!");
                                    System.out.println("Account Number: " + foundCustomer.accno);
                                    System.out.println("Account Name: " + foundCustomer.accname);
                                    System.out.println("Account Balance: " + foundCustomer.accbal);
                                    System.out.println("----------------------------");
                                } else {
                                    System.out.println("Customer not found.");
                                }
                                break;
                            case 3:
                                // Modify Customer Details
                                System.out.print("Enter Account Number to Modify: ");
                                int modifyAccNo = scanner.nextInt();
                                System.out.print("Enter New Account Name: ");
                                String newName = scanner.next();
                                System.out.print("Enter New Account Balance: ");
                                double newBalance = scanner.nextDouble();
                                
                                admin.modifyCustomer(modifyAccNo, newName, newBalance);
                                break;
                            case 4:
                                // Inquire about Balance
                                System.out.print("Enter Account Number to Inquire: ");
                                int inquireAccNo = scanner.nextInt();
                                double balance = admin.getBalance(inquireAccNo);
                                if (balance != -1) {
                                    System.out.println("Account Balance: " + balance);
                                    System.out.println("----------------------------");
                                } else {
                                    System.out.println("Account not found.");
                                }
                                break;
                            case 5:
                                // Delete Customer Account
                                System.out.print("Enter Account Number to Delete: ");
                                int deleteAccNo = scanner.nextInt();
                                
                                admin.deleteCustomer(deleteAccNo);
                                break;
                            case 6:
                                // Display All Customers
                                admin.displayAllCustomers();
                                break;
                            case 7:
                                // Logout Administrator
                                System.out.println("Administrator logged out.");
                                break;
                            default:
                                System.out.println("Invalid choice. Please enter a valid option.");
                        }

                        if (adminChoice == 7) {
                            break; // Exit Administrator menu
                        }
                    }
                } else {
                    System.out.println("Invalid Administrator credentials.");
                }
            } else if (choice == 2) {
                // Customer Login
                System.out.print("Enter Account Number: ");
                int customerAccNo = scanner.nextInt();
                
                // Check if the entered account number exists in the database
                boolean isValidCustomer = admin.isValidCustomer(customerAccNo);
                
                if (isValidCustomer) {
                    while (true) {
                        // Display Customer Menu
                    	System.out.println("----------------------------");
                        System.out.println("Customer Menu:");
                        System.out.println("1. Deposit Money");
                        System.out.println("2. Withdraw Money");
                        System.out.println("3. Transfer Money");
                        System.out.println("4. Print Mini Statement");
                        System.out.println("5. Logout");
                        System.out.println("----------------------------");
                        System.out.print("Enter your choice: ");
                        int customerChoice = scanner.nextInt();

                        // Perform Customer Actions
                        switch (customerChoice) {
                            case 1:
                                // Deposit Money
                                System.out.print("Enter amount to deposit: ");
                                double depositAmount = scanner.nextDouble();
                                customerAccount.deposit(customerAccNo, depositAmount);
                                break;
                            case 2:
                                // Withdraw Money
                                System.out.print("Enter amount to withdraw: ");
                                double withdrawAmount = scanner.nextDouble();
                                customerAccount.withdraw(customerAccNo, withdrawAmount);
                                break;
                            case 3:
                                // Transfer Money
                                System.out.print("Enter recipient's Account Number: ");
                                int recipientAccNo = scanner.nextInt();
                                System.out.print("Enter amount to transfer: ");
                                double transferAmount = scanner.nextDouble();
                                customerAccount.transfer(customerAccNo, recipientAccNo, transferAmount);
                                break;
                            case 4:
                                // Print Mini Statement
                                customerAccount.printMiniStatement(customerAccNo);
                                break;
                            case 5:
                                // Logout Customer
                                System.out.println("Customer logged out.");
                                break;
                            default:
                                System.out.println("Invalid choice. Please enter a valid option.");
                        }

                        if (customerChoice == 5) {
                            break; // Exit Customer menu
                        }
                    }
                } else {
                    System.out.println("Invalid customer account number.");
                }

            }else if (choice == 3) {
                System.out.println("Exiting the Bank System. Thank you!");
                break; // Exit the application
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        scanner.close();
    }
}

