/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opheliasoasis;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
//import java.util.Date;
import java.util.*;

/**
 * Main class and CLI operator.
 *
 * @author Computer
 */
public class OpheliasOasis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Software Engineering");
        res_Create();
    }

    public OpheliasOasis() {
    }

    private void parse() {
    }

    private static void res_Create() {
        String res_type, name, cc, email;
        LocalDate date_in, date_out, currentDay;
        long daysbetween, dayInAdvance;
        //res_type=new String("");
        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("MM-dd-yy");
        currentDay = LocalDate.now();
        Scanner scanner = new Scanner(System.in);

        while (true) {                                                           //get reservation type
            System.out.print("What type of reservation do you wish to create?\n");
            res_type = scanner.nextLine();
            //check user input for substrings and cast into known type
            if (res_type.toLowerCase().contains("six") || res_type.toLowerCase().contains("60")) {  //look for some form of "sixty" or "60"
                res_type = "sixty_day";
                
                break;
            } else if (res_type.toLowerCase().contains("pre")) {                                   //look for some form or "prepaid"
                res_type = "prepaid";
                break;
            } else if (res_type.toLowerCase().contains("conv") || res_type.toLowerCase().contains("trad")) { //look for some form of "conventional" or "traditional"
                res_type = "conventional";
                break;
            } else if (res_type.toLowerCase().contains("incen")) {                                   //look for some form of "incentive"
                res_type = "incentive";
                break;
            } else if (res_type.toLowerCase().contains("quit") || res_type.toLowerCase().contains("exit") || res_type.toLowerCase().contains("stop")) { //Stop creating a reservation
                System.out.print("Exiting Create Reservation\n");
                return;
            } else if (res_type.toLowerCase().contains("?") || res_type.toLowerCase().contains("help")) {//user help
                System.out.print("Valid reservation types are: sixty day, prepaid, convetional, incentive\n"
                        + "A Sixty Day reservation must be made 60 days in advance, it requires an email, name, check in date, and check out date\n"
                        + "A Prepaid reservation must be made 90 days in advance, it requires a name, credit card number, check in and check out data\n"
                        + "A Conventional reservation can be made at any time, it requires a name, credit card number, check in and check out date\n"
                        + "A Incentive reservation must be made less than 30 days from today and must be on days where occupancy is less than 60% "
                        + "it requires a name, credit card number, check in and check out date\n");
                continue;
            } else {
                System.out.print("A valid reservation type has not been entered.\n");
            }
        }
        System.out.print("What is the name on this reservation\n?");
        name = scanner.nextLine();
        System.out.print("What is this the check in date for this reservation? In dd-mm-yy format, ie 01-01-19\n");
        switch (res_type) {

            case "sixty_day":
                while (true) {
                    reEnterDate:
                    try {
                        date_in = LocalDate.from(DTF.parse(scanner.nextLine()));
                        daysbetween= ChronoUnit.DAYS.between(currentDay, date_in);
                        if (daysbetween < 60) {
                            System.out.print("This check in date is not 60 days in advance. A sixty day reservation must be made sixty days from today.\n"
                                             + "Please enter a valid check in day\n");
                            break reEnterDate;
                        }
                        else
                            break;
                    }
                    catch (Exception e) {
                       System.out.print("Invalid date, try again. Date must be in dd-mm-yy format\n");
                   }
                }                  
              break;
            case "incentive":
                System.out.print("this is an incentive\n");
                break;
            case "prepaid":
                    while (true) {
                    reEnterDate:
                    try {
                        date_in = LocalDate.from(DTF.parse(scanner.nextLine()));
                        daysbetween= ChronoUnit.DAYS.between(currentDay, date_in);
                        if (daysbetween < 90) {
                            System.out.print("This check in date is not 60 days in advance. A sixty day reservation must be made sixty days from today.\n"
                                             + "Please enter a valid check in day\n");
                            break reEnterDate;
                        }
                        else
                            break;
                    }
                    catch (Exception e) {
                       System.out.print("Invalid date, try again. Date must be in dd-mm-yy format\n");
                   }
                }                  
                break;
            case "conventional":
                   while (true) {
                    try {
                        date_in = LocalDate.from(DTF.parse(scanner.nextLine()));
                        break;
                    }
                    catch (Exception e) {
                       System.out.print("Invalid date, try again. Date must be in dd-mm-yy format\n");
                   }
                }                  
                break;

        }

    }

    private void res_ChangeDate() {
    }

    /**
     * Prompt a query and return a list of matching reservations.
     * @param date_in
     * @return
     */
    //private List lookup(Date date_in) {
    //}

    /**
     * Interactively lookup reservations and specify util one reservation is found.
     *
     * @param date_in
     * @return
     */
    //private int choose_single(Date date_in) {
    //}

    private void res_Cancel() {
    }

    private void res_Checkin() {
    }

    private void mk_DailyOccupancy() {
    }

    private void mk_DailyArrival() {
    }

    private void mk_MonthlyOccupancy() {
    }

    private void mk_ExpectedIncome() {
    }

    private void mk_IncentivesDiscount() {
    }

    private void mk_Bill() {
    }

    private void mk_Email() {
    }

    private void changeBaseRate() {
    }

    private void exit() {
    }
}
