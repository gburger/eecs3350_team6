/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opheliasoasis;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;


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
        String res_type, temp, name, email, cardHolder;
        int expMonth, expYear, CardNumber, CSV;
        long daysbetween, daysInAdvance;
        LocalDate date_in, date_out, currentDay;

        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("MM-dd-yy");
        currentDay = LocalDate.now();
        Scanner scanner = new Scanner(System.in);
        email = "N/A";
        cardHolder = "N/A";
        expMonth =0;
        expYear = 0;
        CardNumber = 0;
        CSV =0;

        while (true) {                                                           //get reservation type
            System.out.print("What type of reservation do you wish to create?\n");
            res_type = scanner.nextLine();
            //check user input for substrings and cast into known type
            if (res_type.toLowerCase().contains("six") || res_type.toLowerCase().contains("60")) {  //look for some form of "sixty" or "60"
                res_type = "sixty_day";
                daysInAdvance=60;
                break;
            } else if (res_type.toLowerCase().contains("pre")) {                                   //look for some form or "prepaid"
                res_type = "prepaid";
                daysInAdvance=90;
                break;
            } else if (res_type.toLowerCase().contains("conv") || res_type.toLowerCase().contains("trad")) { //look for some form of "conventional" or "traditional"
                res_type = "conventional";
                daysInAdvance=999;
                break;
            } else if (res_type.toLowerCase().contains("incen")) {                                   //look for some form of "incentive"
                res_type = "incentive";
                daysInAdvance=30;
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

//Get Res Name
        System.out.print("What is the name on this reservation?\n");
        name = scanner.nextLine();
 //Get Check in Date
        System.out.print("What is this the check in date for this reservation? In dd-mm-yy format, ie 01-01-19\n");
        if(res_type!="incentive"){
            while (true) {
                    reEnterDate:
                    try {
                        date_in = LocalDate.from(DTF.parse(scanner.nextLine()));
                        daysbetween= ChronoUnit.DAYS.between(currentDay, date_in);
                        if (daysbetween < daysInAdvance) {
                            System.out.print("This check in date is not "+daysInAdvance+" days in advance. A "+res_type+" reservation must be made "+daysInAdvance+" days from today.\n"
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
        }
        else{
            while (true) {
                //THIS NEEDS ADDITIONAL LOGIC TO CHECK IF VALID OCCUPANCY FOR RESESERVATION
                    reEnterDate:
                    try {
                        date_in = LocalDate.from(DTF.parse(scanner.nextLine()));
                        daysbetween= ChronoUnit.DAYS.between(currentDay, date_in);
                        if (daysbetween > daysInAdvance) {
                            System.out.print("This check in date is not less than "+daysInAdvance+" days in advance. A "+res_type+" reservation must be made less than"+daysInAdvance+" days from today.\n"
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
        }
//Get Check out date
        System.out.print("What is this the check out date for this reservation? In dd-mm-yy format, ie 01-01-19\n");
        while (true) {
                    reEnterDate:
                    try {
                        date_out = LocalDate.from(DTF.parse(scanner.nextLine()));
                        daysbetween= ChronoUnit.DAYS.between(date_in, date_out);
                        if (daysbetween <= 0) {
                            System.out.print("This check in date is on or before the check out date of"+date_out.format(DTF)+"\n"
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
 //Gather Reservation specific information
        switch (res_type) {

            case "sixty_day":
              System.out.print("What is the email address for this reservation?");
              while(true){
                  temp = scanner.nextLine();
                  if (temp.contains("@")&&(temp.contains(".com")||temp.contains(".edu")||temp.contains(".gov")||temp.contains(".org"))){
                  email=temp;
                  break;
                  }
                  else
                      System.out.print("This is not a valid email address, please try again");
              }

              break;
            case "incentive":
                //no addtional needed
                break;
            case "prepaid":
                System.out.println("What is the credit card holders name?");
                cardHolder = scanner.nextLine();

                System.out.print("What is the card number?\n Please only enter the numbers, no spaces or dashes.");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9}+")&& temp.length()==16){
                        CardNumber = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a valid credit card number, please try again");
                }

                System.out.println("What is the CSV number?");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9]+")&& temp.length()>=3 && temp.length() <= 4){
                        CSV = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a vaild CSV number, please try again.");
                }

                System.out.println("What is the experation month? \nPlease enther the month as a two digit number as it is displayed on the card.");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9]+") && temp.length()==2 && Integer.parseInt(temp)<= 12){
                        expMonth = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a vaild month, please try again.");
                }

                System.out.println("What is the experation year? \nPlease enther the month as a two digit number as it is displayed on the card.");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9]+") && temp.length()==2){
                        expYear = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a vaild year, please try again.");
                }

                break;
            case "conventional":
                System.out.println("What is the credit card holders name?");
                cardHolder = scanner.nextLine();

                System.out.print("What is the card number?\n Please only enter the numbers, no spaces or dashes.");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9}+")&& temp.length()==16){
                        CardNumber = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a valid credit card number, please try again");
                }

                System.out.println("What is the CSV number?");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9]+")&& temp.length()>=3 && temp.length() <= 4){
                        CSV = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a vaild CSV number, please try again.");
                }

                System.out.println("What is the experation month? \nPlease enther the month as a two digit number as it is displayed on the card.");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9]+") && temp.length()==2 && Integer.parseInt(temp)<= 12){
                        expMonth = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a vaild month, please try again.");
                }

                System.out.println("What is the experation year? \nPlease enther the month as a two digit number as it is displayed on the card.");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9]+") && temp.length()==2){
                        expYear = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a vaild year, please try again.");
                }

                break;

        }
        CreditCard creditCard =  new CreditCard(cardHolder, expMonth, expYear, CardNumber, CSV);
        //Create New Reservation Here
    }

    private void edit_reservation() {
        String param, temp, name, email, cardHolder;
        int expMonth, expYear, CardNumber, CSV, res_id;
        long daysbetween, daysInAdvance;
        LocalDate date_in, date_out, currentDay, date;
        Records record = new Records();

        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("MM-dd-yy");
        currentDay = LocalDate.now();
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the day the reservation was made\n");
        date = LocalDate.from(DTF.parse(scanner.nextLine()));
        res_id = choose_single(lookup(date));
        Reservation reservation = record.getResDB().get(res_id);
        
        // Initialize variables with current res info
        email = reservation.getEmail();
        CreditCard card = reservation.getCreditCard();
        cardHolder = card.getCardHolder();
        expMonth = card.getExpMonth();
        expYear = card.getExpYear();
        CardNumber = card.getCardNumber();
        CSV = card.getCSV();
        name = reservation.getName();
        date_in = reservation.getDateIn();
        date_out = reservation.getDateOut();
        
        // set the days in advanced parameter
        if (reservation.getResType() == Reservation.ResType.conventional)
            daysInAdvance = 999;
        else if (reservation.getResType() == Reservation.ResType.prepaid)
            daysInAdvance = 90;
        else if (reservation.getResType() == Reservation.ResType.sixty_day)
            daysInAdvance = 60;
        else if (reservation.getResType() == Reservation.ResType.incentive)
            daysInAdvance = 30;
        else
            daysInAdvance = 999;
               
        while (true) {
            System.out.print("What would you like to change in the reservation?\n");
            param = scanner.nextLine();
            //check user input for substrings and cast into known type
            if (param.toLowerCase().contains("name")) {  //gets new name
                System.out.print("What is the new name?\n");
                name = scanner.nextLine();
                break;
            } else if (param.toLowerCase().contains("email")) { //change email
                while (true) {
                    if (reservation.getResType() == Reservation.ResType.sixty_day) {
                        while (true) {
                            System.out.print("What is the new email?\n");
                            temp = scanner.nextLine();
                            if (temp.contains("@")&&(temp.contains(".com")||temp.contains(".edu")||temp.contains(".gov")||temp.contains(".org"))) {
                                email = temp;
                                break;
                            }
                            else
                                System.out.print("Not a valid email, please try again\n");
                        }
                        break;
                    } else {
                        System.out.print("Can only change email for 60 day in advance reservations\n");
                    }
                }
                break;
            } else if (param.toLowerCase().contains("date") && (param.toLowerCase().contains("in") || param.toLowerCase().contains("arrival"))) { //change the arrival date
                System.out.print("What is the new arrival date? In dd-mm-yy format, ie 01-01-19\n");
                while (true) {
                    reEnterDate:
                    try {
                        date_in = LocalDate.from(DTF.parse(scanner.nextLine()));
                        daysbetween= ChronoUnit.DAYS.between(currentDay, date_in);
                        if (daysbetween < daysInAdvance) {
                            System.out.print("This check in date is not "+daysInAdvance+" days in advance. A "+reservation.getResType()+" reservation must be made "+daysInAdvance+" days from today.\n"
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
            } else if (param.toLowerCase().contains("date") && (param.toLowerCase().contains("out") || param.toLowerCase().contains("departure"))) { //change the departure date
                System.out.print("What is this the new check out date for this reservation? In dd-mm-yy format, ie 01-01-19\n");
                while (true) {
                    reEnterDate:
                    try {
                        date_out = LocalDate.from(DTF.parse(scanner.nextLine()));
                        daysbetween= ChronoUnit.DAYS.between(date_in, date_out);
                        if (daysbetween <= 0) {
                            System.out.print("This check out date is on or before the check in date of"+date_in.format(DTF)+"\n"
                                             + "Please enter a valid check out day\n");
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
            } else if (param.toLowerCase().contains("card")) { //change the credit card info
                while (true) {
                    if (reservation.getResType() == Reservation.ResType.prepaid) {
                        System.out.print("What information do you want to change?\n");
                        temp = scanner.nextLine();
                        if (temp.toLowerCase().contains("name") || temp.toLowerCase().contains("card holder")) {
                            System.out.println("What is the new credit card holders name?");
                            cardHolder = scanner.nextLine();
                        } else if (temp.toLowerCase().contains("number")) {
                            System.out.print("What is the new card number?\n Please only enter the numbers, no spaces or dashes.");
                            while (true){
                                temp = scanner.nextLine();
                                if (temp.matches("[0-9}+")&& temp.length()==16){
                                    CardNumber = Integer.parseInt(temp);
                                    break;
                                }
                                else
                                    System.out.println("This is not a valid credit card number, please try again");
                            }
                        } else if (temp.toLowerCase().contains("csv")) {
                            System.out.println("What is the new CSV number?");
                            while (true){
                                temp = scanner.nextLine();
                                if (temp.matches("[0-9]+")&& temp.length()>=3 && temp.length() <= 4){
                                    CSV = Integer.parseInt(temp);
                                    break;
                                }
                                else
                                    System.out.println("This is not a vaild CSV number, please try again.");
                            }
                        } else if (temp.toLowerCase().contains("experation") && temp.toLowerCase().contains("month")) {
                            System.out.println("What is the experation month? \nPlease enther the month as a two digit number as it is displayed on the card.");
                            while (true){
                                temp = scanner.nextLine();
                                if (temp.matches("[0-9]+") && temp.length()==2 && Integer.parseInt(temp)<= 12){
                                    expMonth = Integer.parseInt(temp);
                                    break;
                                }
                                else
                                    System.out.println("This is not a vaild month, please try again.");
                            }
                        } else if (temp.toLowerCase().contains("experation") && temp.toLowerCase().contains("year")) {
                            System.out.println("What is the experation year? \nPlease enther the month as a two digit number as it is displayed on the card.");
                            while (true){
                                temp = scanner.nextLine();
                                if (temp.matches("[0-9]+") && temp.length()==2){
                                    expYear = Integer.parseInt(temp);
                                    break;
                                }
                                else
                                    System.out.println("This is not a vaild year, please try again.");
                            }
                        } else {
                            System.out.println("That is not a valid credit card parameter, please try again\n");
                        }
                        break;
                    } else {
                       System.out.print("Changing credit card info is not valid for prepaid reservations\n"); 
                    }
                }
                break;
            } else if (param.toLowerCase().contains("quit") || param.toLowerCase().contains("exit") || param.toLowerCase().contains("stop")) { //Stop editing a reservation
                System.out.print("Exiting Create Reservation\n");
                return;
            } else if (param.toLowerCase().contains("?") || param.toLowerCase().contains("help")) {//user help
                System.out.print("Valid changes to the reservation include:\n"
                +"Reservation name\n"
                +"Reservation email (for 60 day reservations only)\n"
                +"Reservation check in date\n"
                +"Reservation check out date\n"
                +"Credit card information (for conventional, incentive and 60 day reservations only)\n");
            } else {
                System.out.print("A valid reservation change has not been entered.\n");
            }
        }
        // sets all the information again, if something changed, it will be updated, otherwise it gets set as the same thing as before
        card.setCSV(CSV);
        card.setCardHolder(cardHolder);
        card.setCardNumber(CardNumber);
        card.setExpMonth(expMonth);
        card.setExpYear(expYear);
        record.edit_reservation(res_id, reservation.getResType(),date_in, date_out, name, card, email);
    }

    /**
     * Prompt a query and return a list of matching reservations.
     * @param date_in
     * @return
     */
    private List lookup(LocalDate date_in) {
        List l = null;
        return l;
    }

    /**
     * Interactively lookup reservations and specify util one reservation is found.
     *
     * @param date_in
     * @return
     */
    private int choose_single(List<Pair<Integer, Reservation>> reservations) {
        int res_id = 0;
        return res_id;
    }

    private void res_Cancel() {
        Scanner scanner = new Scanner(System.in);
        int res_id;
        String cancel;
        LocalDate date;
        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("MM-dd-yy");
        Records record = new Records();
        
        System.out.print("Enter the day the reservation was made\n");
        date = LocalDate.from(DTF.parse(scanner.nextLine()));
        res_id = choose_single(lookup(date));
        Reservation reservation = record.getResDB().get(res_id);
        System.out.print("Are you sure you want to cancel your reservation?\n");
        cancel = scanner.nextLine();
        if (cancel.toLowerCase().contains("yes"))
            reservation.setCancledStatus(Boolean.TRUE);
        else
            System.out.print("Reservation not canceled\n");
        
    }

    private void res_Checkin() {
    }

    private void mk_DailyOccupancy() {
        LocalDate date = LocalDate.now();
        Records record = new Records();
        Reports report = new Reports(record);
        report.mk_daily_occupancy(date);
        
    }

    private void mk_DailyArrival() {
        LocalDate date = LocalDate.now();
        Records record = new Records();
        Reports report = new Reports(record);
        report.mk_daily_arrivals(date);
    }

    private void mk_MonthlyOccupancy() {
        LocalDate date = LocalDate.now();
        Records record = new Records();
        Reports report = new Reports(record);
        report.mk_expected_occupancy(date);
    }

    private void mk_ExpectedIncome() {
        LocalDate date = LocalDate.now();
        Records record = new Records();
        Reports report = new Reports(record);
        report.mk_expected_income(date);
    }

    private void mk_IncentivesDiscount() {
        LocalDate date = LocalDate.now();
        Records record = new Records();
        Reports report = new Reports(record);
        report.mk_incentives(date);
    }

    private void mk_Bill() {
        LocalDate date = LocalDate.now();
        Records record = new Records();
        Reports report = new Reports(record);
        int res_id;
        res_id = choose_single(lookup(date));
        Reservation reservation = record.getResDB().get(res_id);
        report.mk_bill(reservation);        
    }

    private void mk_Email() {
    }

    private void changeBaseRate() {
    }

    private void exit() {
    }
}
