import java.sql.*;
import java.util.Scanner;
public class BankManagementSystem {
    private static Connection conn = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankmanagementsystem ", "root", "rishav");
            stmt = conn.createStatement();
            System.out.println("Connected to the database successfully.");
             showMenu();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                System.out.println("Disconnected from the database successfully.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
     private static void showMenu() {
        int choice = 0;
         do {
             System.out.println("Welcome to the Bank Management System.");
             System.out.println("1. Add Customer");
             System.out.println("2. Display Customer");
             System.out.println("3. Add Account");
             System.out.println("4. Display Account");
             System.out.println("5. Deposit Amount");
             System.out.println("6. Withdraw Amount");
             System.out.println("7. Exit");
             System.out.print("Enter your choice (1-7): ");
             try {
                 Scanner scanner = new Scanner(System.in);
                 choice = scanner.nextInt();
             } catch (NumberFormatException ex) {
                 System.out.println("Invalid input. Please enter a number.");
             }
             switch (choice) {
                 case 1:
                     addCustomer();
                     break;
                 case 2:
                     displayCustomer();
                     break;
                 case 3:
                     addAccount();
                     break;
                 case 4:
                     displayAccount();
                     break;
                 case 5:
                     depositAmount();
                     break;
                 case 6:
                     withdrawAmount();
                     break;
                 case 7:
                     System.out.println("Exiting the program. Goodbye!");
                     break;
                 default:
                     System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                     break;
             }
         } while (choice != 7);
     }
    private static void addAccount() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter customer ID: ");
            int id = scanner.nextInt();
            System.out.print("Enter account number: ");
            int accountNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter account type: ");
            String accountType = scanner.nextLine();
            System.out.print("Enter balance: ");
            double balance = scanner.nextDouble();
            String sql = "INSERT INTO account (customer_id, account_number, account_type, balance) VALUES (" + id + ", " + accountNumber + ", '" + accountType + "', " + balance + ")";
            int rowsInserted = stmt.executeUpdate(sql);
            if (rowsInserted > 0) {
                System.out.println("Account added successfully.");
            } else {
                System.out.println("Account not added.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static void displayAccount() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter account number: ");
            int accNum = scanner.nextInt();
            String sql = "SELECT * FROM account WHERE account_number = " + accNum;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                System.out.println("Account Number: " + rs.getInt("account_number"));
                System.out.println("Account Type: " + rs.getString("account_type"));
                System.out.println("Balance: " + rs.getDouble("balance"));
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static void addCustomer() {
         try {
             Scanner scanner = new Scanner(System.in);
             System.out.print("Enter customer name: ");
             String name = scanner.nextLine();
             System.out.print("Enter customer address: ");
             String address = scanner.nextLine();
             System.out.print("Enter customer phone number: ");
             String phone = scanner.nextLine();
             System.out.print("Enter customer email: ");
             String email = scanner.nextLine();
             String sql = "INSERT INTO customer (name, address, phone, email) VALUES ('" + name + "', '" + address + "', '" + phone + "', '" + email + "')";
             int result = stmt.executeUpdate(sql);
             if (result > 0) {
                 System.out.println("Customer added successfully.");
             } else {
                 System.out.println("Failed to add customer.");
             }
         } catch (SQLException ex) {
             ex.printStackTrace();
         }
     }
    private static void depositAmount() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter account number: ");
            int accountNumber = scanner.nextInt();
            System.out.print("Enter amount to deposit: ");
            double amount = scanner.nextDouble();
            String sql = "SELECT * FROM account WHERE account_number = " + accountNumber;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                double newBalance = currentBalance + amount;
                sql = "UPDATE account SET balance = " + newBalance + " WHERE account_number = " + accountNumber;
                int rowsUpdated = stmt.executeUpdate(sql);
                if (rowsUpdated > 0) {
                    System.out.println("Deposit successful.");
                    System.out.println("New balance: " + newBalance);
                } else {
                    System.out.println("Deposit failed.");
                }
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void withdrawAmount() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter account number: ");
            int accountNumber = scanner.nextInt();
            System.out.print("Enter amount to withdraw: ");
            double amount = scanner.nextDouble();
            String sql = "SELECT * FROM account WHERE account_number = " + accountNumber;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                if (currentBalance >= amount) {
                    double newBalance = currentBalance - amount;
                    sql = "UPDATE account SET balance = " + newBalance + " WHERE account_number = " + accountNumber;
                    int rowsUpdated = stmt.executeUpdate(sql);
                    if (rowsUpdated > 0) {
                        System.out.println("Withdrawal successful.");
                        System.out.println("New balance: " + newBalance);
                    } else {
                        System.out.println("Withdrawal failed.");
                    }
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static void displayCustomer() {
         try {
             Scanner scanner = new Scanner(System.in);
             System.out.print("Enter customer ID: ");
             int id = scanner.nextInt();
             String sql = "SELECT * FROM customer WHERE id = " + id;
             rs = stmt.executeQuery(sql);
             if (rs.next()) {
                 System.out.println("ID: " + rs.getInt("id"));
                 System.out.println("Name: " + rs.getString("name"));
                 System.out.println("Address: " + rs.getString("address"));
                 System.out.println("Phone: " + rs.getString("phone"));
                 System.out.println("Email: " + rs.getString("email"));
             } else {
                 System.out.println("Customer not found.");
             }
         } catch (Exception ex) {
             ex.printStackTrace();
         }
     }
 }

