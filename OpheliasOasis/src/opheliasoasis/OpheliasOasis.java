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


/**
 * Main class and CLI operator.
 *
 * @author Computer
 */
public class OpheliasOasis {
Records records = new Records();// not sure if this is the correct way to go about this
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        OpheliasOasis OO = new OpheliasOasis(); //needs load logic in constuctor

        System.out.println("Software Engineering");

        OO.parse();

        //TODO: Exit logic

    }

    public OpheliasOasis() {
        //TODO: Load logic
    }

    private void parse() {
        String cmd;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Ophelia's Oasis Reservation System.\n"
                          +"Type a command to begin, or type help for more information");

        while(true){
            cmd = scanner.nextLine();
            if (cmd.toLowerCase().contains("help")||cmd.toLowerCase().contains("?")){
                System.out.println("Command List:\n"
                        + "Reservation Create: Create a new reservation\n"
                        + "Reservation Edit: Edit an already existing reservation\n"
                        + "Reservation Lookup: Look up reservation information by date\n"
                        + "Reservation Cancel: Cancel a reservation\n"
                        + "Reservation Check In: Check in a reservation\n"
                        + "Make Daily Occupancy: Generate the daily occupancy report\n"
                        + "Make Daily Arrivals: Generate the daily arrivals report\n"
                        + "Make Monthly Occupancy: Generate the monthly occupancy report\n"
                        + "Make Expected Income: Generate the expected income report\n"
                        + "Make Incentive: Generate the incentive discount report\n"
                        + "Make Bill: Generate a bill\n"
                        + "Make Email: Generate a reminder email\n"
                        + "Change Base Rate: Change the base rate for a particular day\n"
                        + "Exit: Save and exit the reservation system\n");
            }
            else if(cmd.toLowerCase().contains("res")){
                if (cmd.toLowerCase().contains("cre")){
                   res_Create(false);
                }
                else if(cmd.toLowerCase().contains("edi")){
                    edit_reservation();
                }
                else if(cmd.toLowerCase().contains("look")){
                    res_Checkin();
                }
                else if(cmd.toLowerCase().contains("can")){
                    res_Cancel();
                }
                else if(cmd.toLowerCase().contains("che")){
                    res_Checkin();
                }
            }
            else if(cmd.toLowerCase().contains("make")){
                if(cmd.toLowerCase().contains("daily oc")){
                    mk_DailyOccupancy();
                }
                else if(cmd.toLowerCase().contains("daily arr")){
                    mk_DailyArrival();
                }
                else if(cmd.toLowerCase().contains("month")){
                     mk_MonthlyOccupancy();
                }
                else if(cmd.toLowerCase().contains("expe")){
                    mk_ExpectedIncome();
                }
                else if(cmd.toLowerCase().contains("ince")){
                    mk_IncentivesDiscount();
                }
                else if(cmd.toLowerCase().contains("bill")){
                    mk_Bill();
                }
                else if(cmd.toLowerCase().contains("email")){
                    mk_Email();
                }
            }
            else if(cmd.toLowerCase().contains("change")){
                changeBaseRate();
            }
            else if(cmd.toLowerCase().contains("exit")){
                break;
            }
            else
                System.out.println("Not a valid command. Enter 'help' or '?' for a list of valid commands");

        }

    }

    private void res_Create(boolean isChanged) {
        String temp, name, email, cardHolder;
        int expMonth, expYear, CSV;
        long CardNumber;
        float avgOcc;
        long daysbetween, daysInAdvance;
        LocalDate date_in, date_out, currentDay, runningDay;
        Reservation.ResType res_type;
        List<Pair<Integer, Reservation>> IncentiveCheck;

        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("MM-dd-yy");
        currentDay = LocalDate.now();

        Scanner scanner = new Scanner(System.in);
        avgOcc=0;
        email = "N/A";
        cardHolder = "N/A";
        expMonth =0;
        expYear = 0;
        CardNumber = 0;
        CSV =0;

        while (true) {                                                           //get reservation type
            System.out.print("What type of reservation do you wish to create?\n");
            temp = scanner.nextLine();
            //check user input for substrings and cast into known type
            if (temp.toLowerCase().contains("six") || temp.toLowerCase().contains("60")) {  //look for some form of "sixty" or "60"
                res_type = Reservation.ResType.sixty_day;
                daysInAdvance=60;
                break;
            } else if (temp.toLowerCase().contains("pre")) {                                   //look for some form or "prepaid"
                res_type = Reservation.ResType.prepaid;
                daysInAdvance=90;
                break;
            } else if (temp.toLowerCase().contains("conv") || temp.toLowerCase().contains("trad")) { //look for some form of "conventional" or "traditional"
                res_type = Reservation.ResType.conventional;
                daysInAdvance=0;
                break;
            } else if (temp.toLowerCase().contains("incen")) {                                   //look for some form of "incentive"
                res_type = Reservation.ResType.incentive;
                daysInAdvance=30;
                break;
            } else if (temp.toLowerCase().contains("quit") || temp.toLowerCase().contains("exit") || temp.toLowerCase().contains("stop")) { //Stop creating a reservation
                System.out.print("Exiting Create Reservation\n");
                return;
            } else if (temp.toLowerCase().contains("?") || temp.toLowerCase().contains("help")) {//user help
                System.out.print("Valid reservation types are: sixty day, prepaid, conventional, incentive\n"
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
        System.out.print("What is this the check in date for this reservation? In mm-dd-yy format, ie 01-01-19\n");
        if(res_type!= Reservation.ResType.incentive){
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
                       System.out.print("Invalid date, try again. Date must be in mm-dd-yy format\n");
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
                            System.out.print("This check in date is not less than "+daysInAdvance+" days in advance. A "+res_type+" reservation must be made less than "+daysInAdvance+" days from today.\n"
                                             + "Please enter a valid check in day\n");
                            break reEnterDate;
                        }
                        else
                            break;
                    }
                    catch (Exception e) {
                       System.out.print("Invalid date, try again. Date must be in mm-dd-yy format\n");
                   }
                }
        }
//Get Check out date
        System.out.print("What is this the check out date for this reservation? In mm-dd-yy format, ie 01-01-19\n");
        while (true) {
                    reEnterDate:
                    try {
                        date_out = LocalDate.from(DTF.parse(scanner.nextLine()));
                        daysbetween= ChronoUnit.DAYS.between(date_in, date_out);
                        if (daysbetween <= 0) {
                            System.out.print("This check in date is on or before the check out date of "+date_out.format(DTF)+"\n"
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

            case sixty_day:
              System.out.print("What is the email address for this reservation?\n");
              while(true){
                  temp = scanner.nextLine();
                  if (temp.contains("@")&&(temp.contains(".com")||temp.contains(".edu")||temp.contains(".gov")||temp.contains(".org"))){
                  email=temp;
                  break;
                  }
                  else
                      System.out.print("This is not a valid email address, please try again\n");
              }

              break;
            case incentive:
                 daysbetween= ChronoUnit.DAYS.between(date_in, date_out);
                 runningDay=date_in;
                 for(int i = 0; i<daysbetween; i++){
                    IncentiveCheck=records.lookup(runningDay);
                    avgOcc = avgOcc+IncentiveCheck.size();
                    runningDay.plusDays(1);
                 }
                 avgOcc=avgOcc/(daysbetween*45);
                 if(avgOcc<=60){
                   break;
                 }
                 else{
                     System.out.println("The average expected occupancy for the length of this stay is over 60% this is not a valid incentive reservation.\n"
                             + "This reservation will be deleted.");
                     return;
                 }
            case prepaid:
                System.out.println("What is the credit card holders name?");
                cardHolder = scanner.nextLine();

                System.out.print("What is the card number? Please only enter the numbers, no spaces or dashes.\n");
                while (true){
                    temp = scanner.nextLine();
                    // TODO
                    if (temp.matches("[0-9]+")&& temp.length()==16){
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
                        System.out.println("This is not a valid CSV number, please try again.");
                }

                System.out.println("What is the expiration month? Please enter the month as a two digit number as it is displayed on the card.");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9]+") && temp.length()==2 && Integer.parseInt(temp)<= 12){
                        expMonth = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a valid month, please try again.");
                }

                System.out.println("What is the expiration year? Please enter the month as a two digit number as it is displayed on the card.");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9]+") && temp.length()==2){
                        expYear = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a valid year, please try again.");
                }

                break;
            case conventional:
                System.out.println("What is the credit card holders name?");
                cardHolder = scanner.nextLine();

                System.out.print("What is the card number? Please only enter the numbers, no spaces or dashes.\n");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9]+")&& temp.length()==16){
                        CardNumber = Long.parseLong(temp);
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
                        System.out.println("This is not a valid CSV number, please try again.");
                }

                System.out.println("What is the expiration month? Please enter the month as a two digit number as it is displayed on the card.");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9]+") && temp.length()==2 && Integer.parseInt(temp)<= 12){
                        expMonth = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a valid month, please try again.");
                }

                System.out.println("What is the expiration year? Please enter the month as a two digit number as it is displayed on the card.");
                while (true){
                    temp = scanner.nextLine();
                    if (temp.matches("[0-9]+") && temp.length()==2){
                        expYear = Integer.parseInt(temp);
                        break;
                    }
                    else
                        System.out.println("This is not a valid year, please try again.");
                }

                break;

        }
        CreditCard creditCard =  new CreditCard(cardHolder, expMonth, expYear, CardNumber, CSV);

        records.create_reservation(res_type, date_in, date_out, name, creditCard, email);
        System.out.println("Reservation created successfully!");
    }

    private void edit_reservation() {
        String param, temp, name, email, cardHolder;
        int expMonth, expYear, CSV, res_id;
        long daysbetween, daysInAdvance, CardNumber;
        LocalDate date_in, date_out, currentDay, date;
        Scanner scanner = new Scanner(System.in);

        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("MM-dd-yy");
        currentDay = LocalDate.now();

        System.out.print("Enter the day the reservation was made\n");
        date = LocalDate.from(DTF.parse(scanner.nextLine()));
        System.out.print("Enter the name of the guest\n");
        name = scanner.nextLine();
        res_id = choose_single(records.lookup(date), name);
        Reservation reservation = records.getResDB().get(res_id);

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
            daysInAdvance = 0;
        else if (reservation.getResType() == Reservation.ResType.prepaid)
            daysInAdvance = 90;
        else if (reservation.getResType() == Reservation.ResType.sixty_day)
            daysInAdvance = 60;
        else if (reservation.getResType() == Reservation.ResType.incentive)
            daysInAdvance = 30;
        else
            daysInAdvance = 0;

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
                            System.out.print("What is the new card number? Please only enter the numbers, no spaces or dashes.\n");
                            while (true){
                                temp = scanner.nextLine();
                                if (temp.matches("[0-9]+")&& temp.length()==16){
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
                                    System.out.println("This is not a valid CSV number, please try again.");
                            }
                        } else if (temp.toLowerCase().contains("expiration") && temp.toLowerCase().contains("month")) {
                            System.out.println("What is the expiration month? Please enter the month as a two digit number as it is displayed on the card.");
                            while (true){
                                temp = scanner.nextLine();
                                if (temp.matches("[0-9]+") && temp.length()==2 && Integer.parseInt(temp)<= 12){
                                    expMonth = Integer.parseInt(temp);
                                    break;
                                }
                                else
                                    System.out.println("This is not a valid month, please try again.");
                            }
                        } else if (temp.toLowerCase().contains("expiration") && temp.toLowerCase().contains("year")) {
                            System.out.println("What is the expiration year? Please enter the month as a two digit number as it is displayed on the card.");
                            while (true){
                                temp = scanner.nextLine();
                                if (temp.matches("[0-9]+") && temp.length()==2){
                                    expYear = Integer.parseInt(temp);
                                    break;
                                }
                                else
                                    System.out.println("This is not a valid year, please try again.");
                            }
                        } else {
                            System.out.println("That is not a valid credit card parameter, please try again");
                        }
                        break;
                    } else {
                        System.out.print("Changing credit card info is not valid for prepaid reservations\n");
                    }
                }
                break;
            } else if (param.toLowerCase().contains("type") || param.toLowerCase().contains("kind")){
                reservation.setCancledStatus(true);
                res_Create(true);
            } else if (param.toLowerCase().contains("quit") || param.toLowerCase().contains("exit") || param.toLowerCase().contains("stop")) { //Stop editing a reservation
                System.out.print("Exiting Create Reservation\n");
                return;
            } else if (param.toLowerCase().contains("?") || param.toLowerCase().contains("help")) {//user help
                System.out.print("Valid changes to the reservation include:\n"
                +"Reservation name\n"
                +"Reservation email (for 60 day reservations only)\n"
                +"Reservation check in date\n"
                +"Reservation check out date\n"
                +"Credit card information (for conventional, incentive and 60 day reservations only)\n"
                +"Reservation type (Will require you to reenter all your information)\n");
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

        records.edit_reservation(res_id, reservation.getResType(),date_in, date_out, name, card, email);
    }

    /**
     * Prompt a query and return a list of matching reservations.
     * @param date_in
     * @return
     */
    private List lookup(LocalDate date_in) {
        // TODO
        return null;
    }

    /**
     * Interactively lookup reservations and specify until one reservation is found.
     */
    private int choose_single(List<Pair<Integer, Reservation>> listFromLookup, String currentCustomer) {
        int temp=0;
        for(Pair<Integer,Reservation> single : listFromLookup){
            Reservation res =single.getValue();
            if(res.getName().contains(currentCustomer)){
                temp = single.getKey();
                break;
            }
            else if(listFromLookup.indexOf(single)== listFromLookup.size() -1){
                System.out.println("No reservation found for the customer "+currentCustomer);
                return 0;
            }
        }
        return temp;
    }

    private void res_Cancel() {
        Scanner scanner = new Scanner(System.in);
        int res_id;
        String cancel;
        LocalDate date;
        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("MM-dd-yy");
        // TODO Leave this for the constructor to do

        System.out.print("Enter the day the reservation was made. Date must be in dd-mm-yy format\n");
        date = LocalDate.from(DTF.parse(scanner.nextLine()));
        System.out.print("Enter the name of the guest\n");
        String name = scanner.nextLine();
        res_id = choose_single(records.lookup(date), name);
        Reservation reservation = records.getResDB().get(res_id);
        System.out.print("Are you sure you want to cancel your reservation? No refund will be paid unless you cancel" +
                "before 3 days before your expected arrival date and made a conventional or incentive reservation\n");
        cancel = scanner.nextLine();
        if (cancel.toLowerCase().contains("yes")){
            reservation.setCancledStatus(Boolean.TRUE);
            System.out.print("Reservation has been cancelled\n");
        } else
            System.out.print("Reservation has not been cancelled\n");

    }

    private void res_Checkin() {
        Scanner scanner = new Scanner(System.in);
        String name;
        LocalDate current_date;
        current_date = LocalDate.now();
        //Get customer name
        System.out.print("What is the name on this reservation?\n");
        name = scanner.nextLine();
        List<Pair<Integer, Reservation>>listFromLookup;
        Records rec_obj = new Records();
        listFromLookup =rec_obj.lookup(current_date); // calling lookup of Records class
        int ResID = choose_single(listFromLookup, name); // choose_single of this class only
        Reservation res_obj = new Reservation();
        res_obj.check_in(ResID); // check_in of Res class called and ResId(of the current reservation obtained form lookup) is passed

    }

    private void mk_DailyOccupancy() {
        LocalDate date = LocalDate.now();
        Reports report = new Reports(records);
        report.mk_daily_occupancy(date);

    }

    private void mk_DailyArrival() {
        LocalDate date = LocalDate.now();
        Reports report = new Reports(records);
        report.mk_daily_arrivals(date);
    }

    private void mk_MonthlyOccupancy() {
        LocalDate date = LocalDate.now();
        Reports report = new Reports(records);
        report.mk_expected_occupancy(date);
    }

    private void mk_ExpectedIncome() {
        LocalDate date = LocalDate.now();
        Reports report = new Reports(records);
        report.mk_expected_income(date);
    }

    private void mk_IncentivesDiscount() {
        LocalDate date = LocalDate.now();
        Reports report = new Reports(records);
        report.mk_incentives(date);
    }

    private void mk_Bill() {
        LocalDate date = LocalDate.now();
        Reports report = new Reports(records);
        int res_id;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the guest\n");
        String name = scanner.nextLine();
        res_id = choose_single(records.lookup(date), name);
        Reservation reservation = records.getResDB().get(res_id);
        report.mk_bill(reservation);
    }

    private void mk_Email() {
        Reports report = new Reports(records);
        report.send_reminders();
    }

    private void changeBaseRate() {
        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("MM-dd-yy");
        Scanner scanner = new Scanner(System.in);
        System.out.println("What date do you want this new base rate to start? Date must be in dd-mm-yy format");
        LocalDate date = LocalDate.from(DTF.parse(scanner.nextLine()));
        System.out.println("What is the new base rate?");
        String baseRate = scanner.nextLine();
        records.change_baseRate(date, Float.parseFloat(baseRate));
    }
    private void penaltyCheck(){
        List<Pair<Integer, Reservation>> currentDateReservations;
        LocalDate currentDate= LocalDate.now();
      currentDateReservations = lookup(currentDate);
      for(Pair<Integer, Reservation> single: currentDateReservations){
          Reservation res =single.getValue();
          if(res.getDateIn()== currentDate.minusDays(1) && res.getCheckedInStatus()== false){
           mk_Bill(); // Mk_bill called to include the penalty charges in that customer's bill        
          }  
      }      
    }

    private void exit() {
    }
}
