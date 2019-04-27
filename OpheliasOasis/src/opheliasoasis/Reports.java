/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opheliasoasis;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.util.Pair;
import opheliasoasis.Reservation.ResType;

/**
 * Interface and workhorse for all report generating tasks.
 *
 * @author roanm
 */
public class Reports {
    public DecimalFormat df = new DecimalFormat("#.00");
    public Reports(Records records) {
    }

    /**
     * Expected occupancy per day for 30 days.
     *
     * @param date
     */
    public void mk_daily_occupancy(LocalDate date /* = today */) {
        Records db = new Records();
        List<Pair<Integer, Reservation>> reservations;
        ArrayList<Reservation> temp = new ArrayList<>();
        reservations = db.lookup(date);
        for (Pair<Integer, Reservation> reservation : reservations) {
            if (reservation.getValue().getRoomNumber() != -1 && reservation.getValue().getCancledStatus() == false){
                temp.add(reservation.getValue());
            }
        }
        temp.sort(Comparator.comparing(Reservation::getRoomNumber));
        System.out.println("Daily Occupancy Report");
        for (Reservation temp1 : temp) {
            if (temp1.getDateIn().isBefore(date)) {
                System.out.print("Room Number: " + temp1.getRoomNumber());
                System.out.print(" Name: " + temp1.getName());
                System.out.println(" Departure Date: " + temp1.getDateOut());
            } else if (temp1.getDateOut().equals(date)) {
                System.out.print("Room Number: " + temp1.getRoomNumber());
                System.out.print(" Name: *" + temp1.getName());
                System.out.println(" Departure Date: " + temp1.getDateOut());
            } else {
                System.out.print("Room Number: " + temp1.getRoomNumber());
                System.out.println(" Name: " + temp1.getName());
            }
        }
    }

    /**
     * Arrivals listed by name for the day.
     *
     * @param date
     */
    public void mk_daily_arrivals(LocalDate date /* = today*/) {
        Records db = new Records();
        List<Pair<Integer, Reservation>> reservations;
        ArrayList<Reservation> temp = new ArrayList<>();
        reservations = db.lookup(date);
        for (Pair<Integer, Reservation> reservation : reservations) {
            if (reservation.getValue().getDateIn().equals(date) && reservation.getValue().getCancledStatus() == false){
                temp.add(reservation.getValue());
            }
        }
        temp.sort(Comparator.comparing(Reservation::getName));
        System.out.println("Daily Arrivals Report");
        for (Reservation temp1 : temp) {
            System.out.print("Name: " + temp1.getName());
            System.out.print(" Reservation Type: " + temp1.getResType());
            System.out.print(" Room Number: " + temp1.getRoomNumber());
            System.out.println(" Departure Date: " + temp1.getDateOut());
        }
    }

    /**
     * Expected occupancy over 30 days.
     *
     * @param date
     */
    public void mk_expected_occupancy(LocalDate date /* = today*/) {
        Records db = new Records();
        List<Pair<Integer, Reservation>> reservations;
        int numRooms, sixtyDay, conventional, prepaid, incentive;
        double occRate;
        occRate = 0;
        System.out.println("Expected Occupancy Report");
        for (int i = 0; i < 30; i++) {
            sixtyDay = 0;
            incentive = 0;
            prepaid = 0;
            conventional = 0;
            numRooms = 0;
            reservations = db.lookup(date.plusDays(i));
            for (Pair<Integer, Reservation> reservation : reservations) {
                if (reservation.getValue().getCancledStatus() == false) {
                    if (reservation.getValue().getResType() == ResType.conventional)
                        conventional++;
                    else if (reservation.getValue().getResType() == ResType.prepaid)
                        prepaid++;
                    else if (reservation.getValue().getResType() == ResType.sixty_day)
                        sixtyDay++;
                    else
                        incentive++;
                    numRooms++;
                }
            }
            System.out.print("Date: " + date.plusDays(i));
            System.out.print(" Prepaid Reservations: " + prepaid);
            System.out.print(" Sixty Day Reservations: " + sixtyDay);
            System.out.print(" Conventional Reservations: " + conventional);
            System.out.print(" Incentive Reservations: " + incentive);
            System.out.println(" Number of Resereved Rooms: " + numRooms);
            occRate += numRooms/45;
        }
        System.out.println("Occupancy Rate: " + occRate/30);
    }

    /**
     * Expected income for the next 30 days.
     * @param date
     */
    public void mk_expected_income(LocalDate date /* = today*/) {
        Records db = new Records();
        List<Pair<Integer, Reservation>> reservations;
        double baseRate, perResIncome, dailyIncome, totalIncome, avgIncome;
        totalIncome = 0;
        System.out.println("Expected Income Report");
        for (int i = 0; i < 30; i++) {
            dailyIncome = 0;
            reservations = db.lookup(date.plusDays(i));
            baseRate = 0; //CHANGE ONCE WE IMPLEMENT GETTING THE BASERATE
            for (Pair<Integer, Reservation> reservation : reservations) {
                if (reservation.getValue().getCancledStatus() == false) {
                    if (reservation.getValue().getChangedStatus() == false) {
                        if (reservation.getValue().getResType() == ResType.incentive)
                            perResIncome = baseRate*0.80;
                        else if (reservation.getValue().getResType() == ResType.prepaid)
                            perResIncome = baseRate*0.75;
                        else if (reservation.getValue().getResType() == ResType.sixty_day)
                            perResIncome = baseRate*0.85;
                        else
                            perResIncome = baseRate;
                    } else {
                        perResIncome = baseRate*1.10;
                    }
                    dailyIncome += perResIncome;
                }
            }
            System.out.print("Date: " + date.plusDays(i));
            System.out.println(" Income: " + df.format(dailyIncome));
            totalIncome += dailyIncome;
        }
        avgIncome = totalIncome/30;
        System.out.println("Total Income: " + df.format(totalIncome));
        System.out.println("Average Income: " + df.format(avgIncome));
    }

    /**
     * Expected losses from applying incentives.
     * @param date
     */
    public void mk_incentives(LocalDate date /* = today*/) {
        Records db = new Records();
        List<Pair<Integer, Reservation>> reservations;
        System.out.println("Incentive Report");
        double perResLoss, dailyLoss, totalLoss, avgLoss, baseRate;
        totalLoss = 0;
        for (int i = 0; i < 30; i++) {
            dailyLoss = 0;
            reservations = db.lookup(date.plusDays(i));
            baseRate = 0; //CHANGE ONCE WE IMPLEMENT GETTING THE BASERATE
            for (Pair<Integer, Reservation> reservation : reservations) {
                if (reservation.getValue().getCancledStatus() == false) {
                    if (reservation.getValue().getResType() == ResType.incentive)
                        perResLoss = baseRate*0.20;
                    else 
                        perResLoss = 0;
                    dailyLoss += perResLoss;
                }
            }
            System.out.print("Date: " + date.plusDays(i));
            System.out.println(" Incentives Discount: " + df.format(dailyLoss));
            totalLoss += dailyLoss;
        }
        avgLoss = totalLoss/30;
        System.out.println("Total Incentives Discount: " + df.format(totalLoss));
        System.out.println("Average Incentives Discount: " + df.format(avgLoss));
    }

    /**
     * Make a bill for the given reservation.
     *
     * @param reservation
     */
    public void mk_bill(Reservation reservation) {
        LocalDate date = LocalDate.now();
        System.out.println("Date: " + date);
        System.out.println("Customer Name: " + reservation.getName() + " Room Number: " + reservation.getRoomNumber());
        System.out.println("Arrival Date: " + reservation.getDateIn() + " Departure Date: " + reservation.getDateOut());
        long daysbetween;
        daysbetween= ChronoUnit.DAYS.between(reservation.getDateIn(), reservation.getDateOut());
        System.out.println("Number of nights stayed: " + daysbetween);
        if (reservation.getResType() == ResType.sixty_day || reservation.getResType() == ResType.prepaid) {
            System.out.println("Day reservation was paid: " + reservation.getDatePaid());
        }
        double totalCharge, resCharge, baseRate;
        baseRate = 0; //CHANGE ONCE WE IMPLEMENT GETTING THE BASERATE
        if (!reservation.getChangedStatus()) {
            if (reservation.getResType() == ResType.incentive)
                resCharge = baseRate*0.80;
            else if (reservation.getResType() == ResType.prepaid)
                resCharge = baseRate*0.75;
            else if (reservation.getResType() == ResType.sixty_day)
                resCharge = baseRate*0.85;
            else
                resCharge = baseRate;
        } else {
            resCharge = baseRate*1.10;
        }
        totalCharge = resCharge*daysbetween; 
        System.out.println("Total Charge: " + df.format(totalCharge));
    }

    /**
     * Find 60-day reservations which require reminding and send the emails.
     */
    public void send_reminders() {
        Records db = new Records();
        List<Pair<Integer, Reservation>> reservations;
        reservations = db.lookup(LocalDate.now());
        int reminderDate = 45;
        for (Pair<Integer, Reservation> reservation : reservations) {
            if (reservation.getValue().getResType() == ResType.sixty_day){
                if(ChronoUnit.DAYS.between(LocalDate.now(), reservation.getValue().getDateIn()) == reminderDate){
                    System.out.print("Dear " + reservation.getValue().getEmail() + ",\n"
                    +"Today is 45 days before your expected arrival date. You must pay "
                    +"for your reservation within 15 days, otherwise it will be cancelled\n"
                    +"Thank you for your interest in staying at Ophelias Oasis\n");
                } else {
                    System.out.print("No emails need to be sent today\n");
                }
            }
        }
    }
}
