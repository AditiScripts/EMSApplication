package Expense;

/**
 * Write a description of class EmsLogin here.
 * This class starts a session of EMS through a user login
 * Passwords are given to the users of system
 * A Jframe Window GUI is created for authentication purposes
 * On successful authentication the EMS menu is presented to the user
 * @version ( version number:V1 or a 11/08/2022)
 **/

//Importing the Java GUI packages
import javax.swing.*;
import java.awt.*; //abstract window toolkit - later replaced by Swing
import java.awt.event.*; 

//User input and Exception classes
import java.util.Scanner;
import java.io.IOException;

/*
 * An interface is an abstract "class" that is used to group related methods with "empty" bodies:
 * To access the interface methods, the interface must be "implemented" (like being inherited) by another class 
with the implements keyword (instead of extends).

 * The body of the interface method is provided by the "implement" class:
Note: Below we implement an ActionListener that listens to an event. The ActionListener class can be found in the java.awt.event package. 
It has only one method actionPerformed().
Here it keeps listening to any change on the button - if there is a click, and if so performs an action (display the EMS menu).

(Here, the action is attached to a button. The action event is triggered on the button click.)
 */

public class EMSLogin extends JFrame implements ActionListener  {
    // Class Variables
    private static JLabel password1, label;
    private static JTextField username;
    private static JButton button;
    private static JPasswordField Password;
    //This variable is needed to handle the multithread situation for control flow.  
    private static int loginsuccess = 0;

    public EMSLogin()  {
        /*Adding JPanel to the window
        JPanel class covers the entire space or a window where we attach the component to visualize it along with other panels.
        Instantiates a JPanel class using the variable name: panel. Then, we set the layout to be a null value, 
        meaning: the layout should take the entire width and height of the screen.
         */

        // creating a JPanel class
        JPanel panel = new JPanel(null);

        //Set Jframe parameters
        //Setting the JFrame title name that will appear at the top of the frame
        setTitle("Expense Management System");

        //Positioning the frame on the window - 100 pixels from the left, 100 pixels from the top of the screen
        setLocation(new Point(100, 100));

        //We add the panel to the frame
        add(panel);

        //Sets the size of the entire frame - 400 pixels wide (width), 200 pixels long (height)
        setSize(new Dimension(400, 200));

        //This will end and close the process whenever the user exits the window
        setDefaultCloseOperation(HIDE_ON_CLOSE);
       
        Image EMSIcon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Gaj\\Downloads\\ganapa.jpg"); 
        setIconImage(EMSIcon); 
        //Additing a window listen to frame.
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    if(loginsuccess == 0 || loginsuccess == 2){
                        //System.out.println("Debug: I am here inside if close\n" +loginsuccess);
                        System.exit(0);
                    }
                   // System.out.println("Debug: I am here outside if close\n"+loginsuccess);  
                }
            });

        //Username label constructor
        label = new JLabel("Username");
        label.setBounds(100, 8, 70, 20);
        panel.add(label);

        //Username TextField constructor
        username = new JTextField();
        username.setBounds(100, 27, 193, 28);
        panel.add(username);

        //Password Label constructor
        password1 = new JLabel("Password");
        password1.setBounds(100, 55, 70, 20);
        panel.add(password1);

        //Password TextField
        Password = new JPasswordField();
        Password.setBounds(100, 75, 193, 28);
        panel.add(Password);

        //Button constructor .Add the ActionListener to JButton object to
        //listen to button clicks and handle the event in 
        // method :actionPerformed(ActionEvent e) 
        
        button = new JButton("Login");
        button.setBounds(100, 110, 90, 25);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.addActionListener(this);

        panel.add(button);
        setAlwaysOnTop(true);
        setVisible(true);
        // System.out.println("Debug: I am here in the EMS login\n\\n");  
    }

    private void DisplayUmenu() throws Exception
    {
        //Declaring variables
        int choice;  

        //Creating an infinite while loop so control is returned back to the EMS main menu 
        //after the appropriate function is called based on a choice made by the user
        while(true) {
            System.out.println("\n----------------------------------------------------");
            System.out.println("-----             MAIN MENU                    -----");
            System.out.println("----------------------------------------------------");

            //Creating menu
            System.out.println("\n-  Press 1 to Add Expense                       -");                
            System.out.println("-  Press 2 to Remove Expense                    -");
            System.out.println("-  Press 3 to Update Expense                    -");
            System.out.println("-  Press 4 to Display Expense Table             -");
            System.out.println("-  Press 5 to Display Yearly/Monthly Expense    -");
            System.out.println("-  Press 6 to Display an Expense");
            System.out.println("-  Press 7 to Quit\n \n ");
            System.out.println("----------------------------------------------------");
            System.out.println("----------------------------------------------------");
            System.out.println("----------------------------------------------------");

            //Asking the user to make choice
            System.out.println("Make your Choice");
            Scanner sc = new Scanner(System.in);  

            //Checking for an invalid input choice
            if(sc.hasNextInt()) {
                choice = sc.nextInt();

                //This variable is sent as a argument to Update_RemoveRecord
                //if its value is 0, it will remove a record
                //if its value is 1, it will update a record.
                int edit = 0;

                //Creating a switch case branch based on the choice made by the user
                switch (choice) {
                        //First case adds the record to the user's expense table
                    case 1:
                        //System.out.println("CALLING AddExpense() ");
                        ExpenseCalculater.AddExpense() ;
                        break;

                        //Second case removes a record from the expense table
                    case 2:
                        //System.out.println("Calling RemoveExpense() ");
                        ExpenseCalculater.Update_RemoveExpense(edit);
                        break;

                        //Third case updates a record in the expense table
                    case 3:
                       // System.out.println("Calling UpdateAnExpense()");
                        edit=1;
                        ExpenseCalculater.Update_RemoveExpense(edit);
                        break;

                        //Fourth case displays the expense table
                    case 4:
                       // System.out.println("Calling DisplayTable()");
                        ExpenseCalculater.DisplayTable();
                        break;

                        //Fifth case calculates the total amount of expenses and displays it
                    case 5:
                        //System.out.println("Calling CalculateExpense()");
                        ExpenseCalculater.CalculateExpense();
                        break;

                        //Sixth case outputs the following statement 
                    case 6:
                        //System.out.println("Calling CompareExpenses() will be implemented in release 2 as an extension of this project");
                        ExpenseCalculater.DisplayAnExpense();
                        break;

                        //Seventh case quits the program
                    case 7:
                        System.out.println("Exiting the EMS.\n\n");
                        System.exit(0);

                        //A default case which displays the message: invalid choice made by the user
                    default:
                        System.out.println("Invalid Choice!!! Please enter a Valid Menu Choice.\n\n");
                }
            }

            else {
                System.out.println("Invalid Choice!!!  Please enter a Valid integer for Menu Choice. \n \n");
            }
        }
    }

     public static void main(String[] args) throws Exception{
          try  
        {  
            //create instance of the CreateLoginForm  
          EMSLogin myuser = new EMSLogin();  
         
           //Wait to handle the data in the GUI thread to handle the multithreading situation with Main Thread;
           do{
              //System.out.println("login inside do while" +loginsuccess);    
              System.out.println("            ");
                }
            while(loginsuccess==0);
           
           // System.out.println("login outside do while" +loginsuccess);
            if(loginsuccess == 1){
           //Clear the console
            System.out.println("            ");
            System.out.print('\u000C');
            System.out.println("\n WELCOME TO THE EXPENSE MANAGEMENT SYSTEM (EMS) \n");
            myuser.DisplayUmenu();
        }
        }  
        catch(Exception e)  
        {    
            //handle exception  
            JOptionPane.showMessageDialog(null, e.getMessage()); 
                 
        }    
         
          //System.out.println("I am here in main\n\\n");  
          //System.out.println("login " +loginsuccess);  
                 
        }

    /*
         * Authentication: This method will be invoked when we click on the Login button. It helps authenticate 
         * the data entered in the username and password fields.
         * Imlementing an action event listener class with conditional statement
         * JOptionPane is another component of Java GUI, it comes in handy when displaying either a warning, 
         * success, or error message on the screen, as a dialog box.
         */
        @Override
        public void actionPerformed(ActionEvent e)  {
            String Username = username.getText();
            String Password1 = Password.getText();
       
            if (Username.equals("admin") && Password1.equals("admin123"))
                   {  
                     
                  JOptionPane.showMessageDialog(null, "Login Successful, You are the  "+ Username.toUpperCase() + "!" ,"Login",JOptionPane.PLAIN_MESSAGE);
                  setVisible(false);
                  loginsuccess = 1;
         
               }
               
               else if (Username.equals("user") && Password1.equals("user123"))
                   {  
                     
                  JOptionPane.showMessageDialog(null, "Login Successful!","Login",JOptionPane.PLAIN_MESSAGE);
                   setVisible(false);
                   loginsuccess = 1;
                }
               
            else{
                 int result =  JOptionPane.showConfirmDialog(null, "Username or password mismatch. Please try again ","Login alert!",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                 
                  if(result == JOptionPane.YES_OPTION){
                      //Reset Username and Password field.
                      username.setText("");
                      Password.setText("");
                  }
                  else if (result == JOptionPane.NO_OPTION){    
                     setVisible(false);
                     dispose();
                     loginsuccess = 2;
                     System.exit(0);
                  }
                  else if (result == JOptionPane.CLOSED_OPTION) {
                   //System.out.println("Window closed without Selecting!");
                   loginsuccess = 2;
                   System.exit(0);
                   }
               
        }
    }
}