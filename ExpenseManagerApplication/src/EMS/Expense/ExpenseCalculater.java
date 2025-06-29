package Expense;

/**
 * Write a description of class JDBCExample here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

//For JDBC
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.awt.*;

//Date formating
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

//  to get user input,
import java.util.Scanner;

//i/o exception
import java.io.IOException;

//for JAVA GUI and Java GUI Table
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ImageIcon;

public class ExpenseCalculater {
    //Variables
    static String QUERYDISPLAY = "SELECT id, Expense_Name, Yearly_Payment, Monthly_Payment FROM Expenses2022";
    //DB connection dbconn to be accessed
    static DBConnection dbconn = new DBConnection();//why static

    //The Statement objects allow you to execute basic SQL queries and retrieve the results through the ResultSet class
    //https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-statements.html
    static Statement stmt = null;
    static ResultSet rs = null; 

    //Methods

    static void ClearResources()
    {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqlEx) { } // ignore

            rs = null;
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqlEx) { } // ignore

            stmt = null;
        }

    }

    //this functions returns true if there are no records in the DB
    static boolean IsRecordsEmpty()throws SQLException
    {

        //System.out.println("Static method IsRecordsEmpty");
        //We could have simply used count(*) to get number of records but to print colunm names I used the following code
        //referening to oracle site
        String QUERYDISPLAY = "SELECT * FROM expenses2022";
        try(
        Statement stmt = dbconn.DBGetConn().createStatement();
        //Executing the query
        ResultSet rs = stmt.executeQuery(QUERYDISPLAY);)
        {

            //Retrieving the result
            if (!rs.next()) { 
                System.out.print("No Expense Records in Database Table! Please add the expenses first.");
                // Get column names from table using ResultSetMetaData
                //https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSetMetaData.html
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                System.out.println("Printing the columns in our Expense table : ");
                // Get the column labels; column indices start from 1
                for (int i=1; i<=columnCount; i++) {

                    String columnName = rsmd.getColumnLabel(i);
                    System.out.println(columnName); 
                } 

                return true;
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Expense table NOT found in the Database ");
            return true;
        }
        return false;
    }

    /*
    Others ways to declare an array String[][] myArray = new String[5][]; // OK String[][] yourArray = new String[5][4];

    Read more: https://www.java67.com/2014/10/how-to-create-and-initialize-two-dimensional-array-java-example.html#ixzz7dNOEQW44
     */
    static void DisplayTable() throws Exception
    {

        boolean Rempty = IsRecordsEmpty();
        if (Rempty == true)
        {
            // System.out.print("No Expense Records in Database Table to display! Please add the expenses!");  
        }

        else
        {           String QUERYROWS = "SELECT COUNT(*) FROM expenses2022";
            stmt = dbconn.DBGetConn().createStatement();

            //have an array of type object to accomodate different datatypes from db instead of string

            Object[][] Array_Expenses;
            //Executing the query
            int Array_row =0;

            rs = stmt.executeQuery(QUERYROWS);
            if (rs.next()) { 
                /* this is important to get row numbers from mysql table to initialise our 2D arrays
                 * we have 6 colunms currentlyyyyy ,refering the mysql create table
                 */
                Array_row = rs.getInt(1);
                //System.out.println("Row Count " + rs.getInt(1) +" " + Array_row );             
                Array_Expenses = new Object[Array_row][5];

                String QUERY = "SELECT * FROM expenses2022";
                stmt = dbconn.DBGetConn().createStatement();
                //Executing the query
                rs = stmt.executeQuery(QUERY);
                int i=0;
                while(rs.next()){
                    //Display values

                    //Converting Date object to String
                    Date sdate = rs.getDate("Start_Date");
                    String s_date = sdate.toString();
                    // System.out.println("Date: "+sdate.toString());

                    Date edate = rs.getDate("expiry_date");
                    String e_date = edate.toString();
                    //System.out.println("Date: "+edate.toString());
                    //String.valueOf(rs.getInt(1)

                    Array_Expenses[i][0] = rs.getString("expense_Name");
                    //System.out.print(Array_Expenses[i][0]+" ,");
                    Array_Expenses[i][1] = s_date;
                    //System.out.print(Array_Expenses[i][1]+" ,");
                    Array_Expenses[i][2] = "£"+rs.getBigDecimal("Yearly_Payment");
                    //System.out.print(Array_Expenses[i][2]+" ,");
                    Array_Expenses[i][3] = "£"+rs.getBigDecimal("Monthly_Payment"); 
                    //System.out.print(Array_Expenses[i][3]+" ,");
                    Array_Expenses[i][4] = e_date;  
                    //System.out.print(Array_Expenses[i][4]);
                    i++;
                    //System.out.println(); //change line on console as row comes to end in the matrix.
                    // System.out.println("Hi"+i);

                }
                try{
                    for (int d = 0; d < Array_Expenses.length; d++) { //this equals to the row in our matrix.
                        for (int j = 0; j < Array_Expenses[d].length; j++) { //this equals to the column in each row.
                            //System.out.print(Array_Expenses[d][j] + " ");
                        }
                        //System.out.println(); 
                    }
                }

                catch(Exception e){

                    System.out.print("Error : " +e);
                    return; 
                }

                // With two dimensional array, set the columns of a table. Additionally, we have set the rows using a one-dimensional array as shown below 
                JTable table = new JTable(Array_Expenses,new String[] {"Expense_Name","Start_Date","Yearly_Payment","Monthly_Payment","Expiry_Date"});
                Font font = new Font("Verdana", Font.PLAIN, 12);
                table.setFont(font);          

                JFrame frame = new JFrame();

                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                //changing table icon image
                //https://www.javatpoint.com/how-to-change-titlebar-icon-in-java-awt-swing
                Image EMSIcon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Gaj\\Downloads\\gb.png"); 
                frame.setIconImage(EMSIcon);   

                //Title of frame

                frame.setTitle("My Expenses");

                //this will oveide the setSize to adjustable view table
                //table.setPreferredScrollableViewportSize(table.getPreferredSize());

                //frame.setSize(900,900);
                frame.add(new JScrollPane(table));         
                frame.pack();

                //Adjusting the size of window and  setting its position at top left hand corner (0,0)
                Toolkit theKit = frame.getToolkit(); 
                Dimension wndSize = theKit.getScreenSize(); 
                frame.setBounds(0,0,//position
                    wndSize.width / 2, wndSize.height / 2); // Size

                //frame.setExtendedState(JFrame.MAXIMIZED_FULL); to make full screen of device
                // Set's the window to be "always on top"
                frame.setAlwaysOnTop( true );
                //Displaying the window
                frame.setVisible(true);
            } 

        }
    }

    //Adds an expense to expense table in mysql DB
    static void AddExpense()throws SQLException
    {
        try
        {
            System.out.print("Enter the Expense Name to be Added: ");
            Scanner user_input = new Scanner(System.in);

            String Ex_name;
            double YPayment = 0;
            double MPayment = 0;
            //Expense name input
            Ex_name = user_input.nextLine().toUpperCase();

            //Client Request
            String sql1="select * from expenses2022" +" WHERE expense_Name='"+ Ex_name +"' ";
            Statement stmt1 = dbconn.DBGetConn().createStatement();
            //System.out.print(sql1);

            ResultSet rs1 = null;
            //Executing the query
            rs1 = stmt1.executeQuery(sql1);
            if(rs1.next() == true)
            {
                System.out.print("\n");
                System.out.print("The Expense Name already exists in the database!"+"\nIf want to edit this record Use option 3 of Main Menu.\nTaking you back to the Main Menu now. ");  
                return;
            }

            System.out.println("You entered a Expense Name to be "+Ex_name); 
            //Check to see if only two characters are entered
            if((Ex_name.length() <=2 ))
            {
                System.out.print("Please enter a Meaningful/Realistic Expense Name!"); 
                return;
            }
            //Check to see if duplicate data exist in the database.If so tell the user.

            //Start Date input          
            String Sst_date;
            System.out.print("\n Enter the Start Date in the format dd/mm/yyyy: ");
            Sst_date = user_input.next();
            //date format yyyy/mm/dd as per client's request
            //https://www.tutorialspoint.com/java/java_string_matches.htm

            String pattern ="\\d{1,2}/\\d{1,2}/\\d{4}";
            if (Sst_date.matches(pattern))
            {
                //System.out.println("Correct format");
            }
            else{
                System.out.println("Wrong Date format/ Date, Please try again");
                return;
            }

            //Monthly or Yearly Payment input
            int Confirm;

            System.out.println("Press 1 to Enter Monthly Payment");
            System.out.println("OR");
            System.out.println("Press 2 to Enter Yearly Payment"); 

            if(user_input.hasNextInt()) { 
                Confirm = user_input.nextInt();
                while((Confirm !=1 ) && (Confirm !=2))
                {

                    System.out.println("Invalid choice!!! Please make a Valid Choice. \n\n");
                    System.out.println("Press 1 to Enter Monthly Payment");
                    System.out.println("OR");
                    System.out.println("Press 2 to Enter Yearly Payment"); 
                    Confirm = user_input.nextInt();
                }

                //Creating switch case branch based on choice made by user 
                switch(Confirm){
                        //If its monthly payment:
                    case 1:
                        System.out.println("The choice is to Enter Monthly Payment") ;
                        System.out.print("Enter the Monthly Payment: ");
                        MPayment = user_input.nextDouble();
                        YPayment = MPayment * 12.0;
                        break;

                    case 2:
                        System.out.println("The choice is to Enter Yearly Payment") ;
                        System.out.print("Enter the Yearly Payment: ");
                        YPayment = user_input.nextDouble();
                        MPayment = YPayment/12.0;
                        break;

                        //default case to display the message invalid choice made by the user
                    default:
                        System.out.println("Invalid choice!!! Please make a Valid Choice. \\n\\n");

                }
            }

            else if(!user_input.hasNextInt())
            {
                System.out.println("Invalid choice!!! Please make a Valid Choice.Taking you back to Main Menu \n");
                return;

            }
            //End of Contract Date input
            String uext_date;
            System.out.print("Enter the Expiry Date in the format dd/mm/yyyy: ");
            uext_date = user_input.next();

            if (!uext_date.matches(pattern))
            {   System.out.println("Wrong Date format/ Date, Please try again");
                return;
            }                    
            //Creating the Statement object   
            //System.out.println("\ncalling db class."); 
            Statement stmt = dbconn.DBGetConn().createStatement();
            //System.out.println("\n after connection."); 
            //inserting rows
            String sql = "INSERT INTO Expenses2022 (Expense_Name,start_date,expiry_date,Monthly_Payment,Yearly_Payment) VALUES('"+Ex_name+"',STR_TO_DATE('"+Sst_date+"','%d/%m/%Y'),STR_TO_DATE('"+uext_date+"','%d/%m/%Y'),'"+MPayment+"','"+YPayment+"')";
            //System.out.println("Inserting the record into the table..." + sql);   
            stmt.executeUpdate(sql);
            System.out.println("\nThe above entered data is inserted into the Database table."); 
            System.out.println("You can verify this using: MENU Option 4: Display Expense Table."); 
            System.out.println("And Update if required using: MENU Option 3: Update Expense."); 
            user_input.close(); 
        }
        catch (SQLException e) {
            e.printStackTrace();

            return; 
        } 

    }

    static void Update_RemoveExpense(int Indicator) throws Exception
    {
        //System.out.println("Static method Remove/Update can be called without creating objects");
        boolean Rempty = IsRecordsEmpty();
        if (Rempty == true)
        {
            System.out.print("No Expense Records in Database to Remove/Update! Please add an Expense Record before Removing/Updating."); 
        }

        else{
            DisplayTable();
            Scanner user_input = new Scanner(System.in);

            String Ex_name;
            int U_Confirm;

            System.out.print("\nCheck the table above and Enter the 'Expense Name' which you want to Remove/Update: ");
            Ex_name = user_input.nextLine().toUpperCase();
            //System.out.print("1Expense name you entered"+Ex_name);

            String sql="select * from expenses2022" +" WHERE expense_Name='"+ Ex_name +"' ";
            stmt = dbconn.DBGetConn().createStatement();
            //Executing the query
            rs = stmt.executeQuery(sql);

            if(rs.next() == false)
            { 
                System.out.print("\nPlease enter a Valid Expense Name!  ");  
                System.out.print("\nThe Expense Name does not exists in the database! Try Again, taking you back the Main Menu now.");  
                return;
            }

            System.out.print("\nAre you sure you want to Remove/Update the Expense entry for: "+ Ex_name );
            System.out.print("\nKindly enter 1 for Confirmation or 2 to Exit/Retry: ");

            U_Confirm = user_input.nextInt();

            System.out.print("\n Your Choice is :" +U_Confirm +"\n");
            if(U_Confirm == 1)
            {
                System.out.print("Choice Confirmed.");
                String QUERYDELUP="";
                if(Indicator == 0)
                {
                    QUERYDELUP = "DELETE FROM expenses2022" +" WHERE expense_Name='"+ Ex_name +"' ";
                    //System.out.println("SQL QUERY TO DEL RECORD"+ QUERYDELUP);
                    System.out.println("Connecting to Database..."); 
                    stmt = dbconn.DBGetConn().createStatement();
                    //Executing the query
                    stmt.executeUpdate(QUERYDELUP);
                    DisplayTable();
                }  
                else if (Indicator == 1)
                {
                    QUERYDELUP = "Select * from  expenses2022 " + "WHERE expense_Name='"+ Ex_name +"' ";
                    System.out.println(" Connecting to Database..."); 
                    stmt = dbconn.DBGetConn().createStatement();
                    //Executing the query
                    rs = stmt.executeQuery(QUERYDELUP);
                    while(rs.next()){
                        //Display values
                        System.out.println("   EXPENSE NAME: " + rs.getString("expense_Name"));
                        System.out.println("   START DATE: " + rs.getDate("start_date"));
                        System.out.println("   YEARLY PAYMENT: " + rs.getDouble("Yearly_Payment"));
                        System.out.println("   MONTHLY PAYMENT: " + rs.getDouble("Monthly_Payment"));
                        System.out.println("   EXPIRY DATE: " + rs.getDate("expiry_date"));
                    }

                    System.out.println("Press 1 to Edit EXPENSE NAME");
                    System.out.println("Press 2 to Edit START DATE");
                    System.out.println("Press 3 to Edit YEARLY PAYMENT VALUE");
                    System.out.println("Press 4 to Edit MONTHLY PAYMENT VALUE");
                    System.out.println("Press 5 to Edit EXPIRY DATE");
                    System.out.println("Make your Choice");

                    int user_choice =0;
                    double YPay = 0;
                    double MPay = 0;
                    String ExpName="s";
                    String s_date="";
                    String e_date="";

                    //Check for invalid input choice
                    if(user_input.hasNextInt()) {
                        user_choice = user_input.nextInt();
                        //Creating switch case branch based on choice made by user
                        /* 
                         * At this point, the scanner is still on the second line at the end
                         * of the double, so we need to move the scanner to the next line
                         * scans to the end of the previous line which contains the int. 
                         * 
                         **/
                        user_input.nextLine(); 
                        switch (user_choice) {

                                //First case add record to your expense table
                            case 1:
                                System.out.print("Enter the new 'Expense Name':");         
                                ExpName = user_input.nextLine().toUpperCase(); 

                                QUERYDELUP = "UPDATE expenses2022" + " SET expense_Name = '"+ExpName+"' WHERE expense_Name='"+ Ex_name +"' ";
                                System.out.print("\n Connecting to Database..."); 
                                //System.out.println("SQL QUERY TO Update RECORD"+ QUERYDELUP);
                                stmt = dbconn.DBGetConn().createStatement();
                                //Executing the query
                                stmt.executeUpdate(QUERYDELUP);
                                System.out.print("Check Table for Updation");
                                DisplayTable();
                                break;

                                //Second case remove a record in expense table
                            case 2:
                                System.out.println("To Edit START DATE " + "Please enter new value in proper format 'dd/mm/yyyy':");
                                s_date = user_input.next();
                                //System.out.println(s_date);
                                try {
                                    String pattern ="\\d{1,2}/\\d{1,2}/\\d{4}";
                                    if (s_date.matches(pattern))
                                    {
                                        //System.out.println("Correct format");
                                    }
                                    else{
                                        System.out.println("Wrong Date format/ Date, Please try again");
                                        return;
                                    }

                                    //System.out.println("The date you entered: "+ s_date);
                                }
                                catch (Exception e) {
                                    System.out.print("Wrong date format, Try again!");
                                    return;

                                }
                                QUERYDELUP = "UPDATE expenses2022" + " SET start_date = STR_TO_DATE('"+s_date+"','%d/%m/%Y') WHERE expense_Name='"+ Ex_name +"' ";
                                //System.out.println("SQL QUERY TO UPDATE RECORD"+ QUERYDELUP);
                                stmt = dbconn.DBGetConn().createStatement();
                                //Executing the query
                                stmt.executeUpdate(QUERYDELUP);
                                System.out.print("Check Table for Updation");
                                DisplayTable();                              
                                break;

                                //Third case Update record in your expense table
                            case 3:
                                System.out.println("To Edit YEARLY PAYMENT VALUE, " + "Please enter new value :");
                                YPay = user_input.nextDouble();
                                MPay = (YPay/12);
                                QUERYDELUP = "UPDATE expenses2022" + " SET Yearly_Payment = '"+YPay+"',Monthly_Payment = '"+MPay+"' WHERE expense_Name='"+ Ex_name +"' ";
                                //System.out.println("SQL QUERY TO EDIT RECORD"+ QUERYDELUP);
                                stmt = dbconn.DBGetConn().createStatement();
                                //Executing the query
                                stmt.executeUpdate(QUERYDELUP);
                                System.out.print("Check Table for Updation");
                                DisplayTable();
                                break;

                                //Fourth case DisplayTable()
                            case 4:
                                System.out.println("To Edit MONTHLY PAYMENT VALUE, " + "Please enter new value: ");

                                MPay = user_input.nextDouble();
                                YPay = MPay * 12;
                                QUERYDELUP = "UPDATE expenses2022" + " SET Yearly_Payment = '"+YPay+"', Monthly_Payment = '"+MPay+"' WHERE expense_Name='"+ Ex_name +"' ";
                                //System.out.println("SQL QUERY TO EDIT RECORD"+ QUERYDELUP);
                                stmt = dbconn.DBGetConn().createStatement();
                                //Executing the query
                                stmt.executeUpdate(QUERYDELUP);
                                System.out.print("Check Table for Updation");
                                DisplayTable();
                                break;

                            case 5:
                                System.out.println("To Edit Contract End Date, " + "Please enter new value in proper format 'dd/mm/yyyy': ");
                                e_date = user_input.next();
                                //System.out.println(e_date);

                                try {
                                    String pattern ="\\d{1,2}/\\d{1,2}/\\d{4}";
                                    if (e_date.matches(pattern))
                                    {
                                        //System.out.println("Correct format");
                                    }
                                    else{
                                        System.out.println("Wrong Date format/ Date, Please try again");
                                        return;
                                    }
                                    //System.out.println(e_date);
                                }
                                catch (Exception e) {
                                    System.out.print("Wrong date format");
                                    return;

                                }

                                QUERYDELUP = "UPDATE expenses2022" + " SET expiry_date = STR_TO_DATE('"+e_date+"','%d/%m/%Y') WHERE expense_Name='"+ Ex_name +"' ";
                                //System.out.println("SQL QUERY TO EDIT RECORD"+ QUERYDELUP);
                                stmt = dbconn.DBGetConn().createStatement();
                                //Executing the query
                                stmt.executeUpdate(QUERYDELUP);
                                System.out.print("Check Table for Updation");
                                DisplayTable(); 
                                break;

                                //default case to display the message invalid choice made by the user
                            default:
                                System.out.println("Invalid choice!!! Please make a valid choice. Back to main menu>");

                        }// case
                    }//if

                    else {
                        System.out.println("Invalid choice!!! Make a valid integer Choice. Back to main menu."); 
                    }

                }  //else if  (1)

            }
            else if (U_Confirm == 2)
            {                
                System.out.print("Going back to Main Menu");
            }

            else
            {
                System.out.print("Invalid entry! Kindly enter 1 for confirmation or 2 to Exit/Retry");

            }

            user_input.close();   
        }

    }


    static void CalculateExpense()
    {
        //difference between print and println
        //System.out.println("Static method Calculate and Display Yearly or monthly can be called without creating objects");
        String QUERYCal = "SELECT SUM(Yearly_Payment),SUM(Monthly_Payment) FROM expenses2022";
        try{
            stmt = dbconn.DBGetConn().createStatement();
            //Executing the query
            rs = stmt.executeQuery(QUERYCal);
            while(rs.next()){
                //Display values
                System.out.print("\n");
                System.out.println("*******************************************************");   
                System.out.println("             Total Yearly Expense: " +"£"+rs.getBigDecimal(1));
                System.out.println("             Totaly Monthly Expense: " +"£"+ rs.getBigDecimal(2));
                System.out.println("*******************************************************");  
                System.out.print("\n");
            }

        }

        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return; 
        } 
    }

    //Added after client request
    static void DisplayAnExpense() throws Exception
    {

        //System.out.println("Static DisplayAnExpense Method, can be called without creating objects");
        //Checking if records are empty
        boolean Rempty = IsRecordsEmpty();
        if (Rempty == true)
        {
            System.out.print("No Expense Records in Database to Display!");
        }

        else{
            // DisplayTable();
            Scanner user_input = new Scanner(System.in);

            String Ex_name;
            int U_Confirm;

            System.out.print("\nEnter the 'Expense Name' which you want to be displayed: ");
            //Client Request
            Ex_name = user_input.nextLine().toUpperCase();
            String sql="select * from expenses2022" +" WHERE expense_Name='"+ Ex_name +"' ";
            stmt = dbconn.DBGetConn().createStatement();
            //System.out.print(sql);
            ResultSet rs2 = null;
            //Executing the query

            rs = rs2 = stmt.executeQuery(sql);
            if(rs2.next() == false)
            {

                System.out.print("\nPlease enter a Valid Expense Name!  ");  
                System.out.print("The Expense Name does not exists in the database!\n Try Again, taking you back the Main Menu now.  ");  
                return;
            }

            try{
                rs = stmt.executeQuery(sql);
                //System.out.print("inside try block");
                int size = 0;
                while(rs.next()){
                    //Get Row numbers fr above query
                    size++;

                }
                // System.out.print("   ROWS: " + size);
                Object [][] Array_Expenses;
                Array_Expenses = new Object[size][5];
                rs = stmt.executeQuery(sql);
                int i=0;
                //lets populate a 2 D array to pass to Jtable
                while(rs.next()){
                    //Display values  
                    Array_Expenses[i][0] = rs.getString("expense_Name");
                    System.out.print(Array_Expenses[i][0]+" ,");

                    Array_Expenses[i][1] = rs.getDate("start_date");
                    System.out.print(Array_Expenses[i][1]+" ,");

                    Array_Expenses[i][2] = "£"+rs.getBigDecimal("Yearly_Payment");
                    System.out.print(Array_Expenses[i][2]+" ,");

                    Array_Expenses[i][3] = "£"+rs.getBigDecimal("Monthly_Payment");
                    System.out.print(Array_Expenses[i][3]+" ,");

                    Array_Expenses[i][4] = rs.getDate("expiry_date");  
                    System.out.print(Array_Expenses[i][4]);      
                    System.out.print("\n");

                    System.out.println();
                    i++;
                }
                JTable table = new JTable(Array_Expenses,new String[] {"Expense_Name","Start_Date","Yearly_Payment","Monthly_Payment","Expiry_Date"});
                Font font = new Font("Verdana", Font.PLAIN, 12);
                table.setFont(font);          
                table.setBackground(Color.blue);
                //table.setBackground(new Color(46, 46, 46));//black 
                table.setForeground(Color.white);
                table.setRowHeight(30);

                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                Image EMSIcon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Gaj\\Downloads\\gb.png"); 
                frame.setIconImage(EMSIcon); 

                frame.setTitle("My Expense");

                frame.setPreferredSize(new Dimension(600, 100));                                                                

                //Jscroll pane is needed to display colunm headers in this case.
                //Table is child of scrollPane.

                frame.add(new JScrollPane(table));

                frame.pack();
                // Set's the window to be "always on top"        
                frame.setAlwaysOnTop( true );
                //Display the window.
                frame.setVisible(true);

            }
            catch (SQLException e) {
                System.out.print("Couldn't print the Expense Record :"+e);
            }
            user_input.close();
        }      

    }
}