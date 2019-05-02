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
            if (reservation.getValue().getCheckedInStatus() && reservation.getValue().getCancledStatus() == false){
                temp.add(reservation.getValue());
            }
        }
        temp.sort(Comparator.comparing(Reservation::getRoomNumber));
        System.out.println("Daily Occupancy Report");
        for (Reservation temp1 : temp) {
            if (temp1.getDateIn().isBefore(date)) {
                System.out.print("Room Number: " + temp1.getRoomNumber());
                System.out.print(", Name: " + temp1.getName());
                System.out.println(", Departure Date: " + temp1.getDateOut());
            } else if (temp1.getDateOut().equals(date)) {
                System.out.print("Room Number: " + temp1.getRoomNumber());
                System.out.print(", Name: *" + temp1.getName());
                System.out.println(", Departure Date: " + temp1.getDateOut());
            } else {
                System.out.print("Room Number: " + temp1.getRoomNumber());
                System.out.println(", Name: " + temp1.getName());
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
            System.out.print(", Reservation Type: " + temp1.getResType());
            System.out.print(", Room Number: " + temp1.getRoomNumber());
            System.out.println(", Departure Date: " + temp1.getDateOut());
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
        System.out.print("                     Prepaid Reservations: ");
        System.out.print(" Sixty Day Reservations: ");
        System.out.print(" Conventional Reservations: ");
        System.out.print(" Incentive Reservations: ");
        System.out.println(" Number of Reserved Rooms: ");
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
            System.out.print("                         " + prepaid);
            System.out.print("                         " + sixtyDay);
            System.out.print("                         " + conventional);
            System.out.print("                         " + incentive);
            System.out.println("                          " + numRooms);
            occRate += (numRooms/45.0);
        }
        System.out.println("Occupancy Rate: " + (occRate/30)*100 + "%");
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
        System.out.print("Date: ");
        System.out.println("           Income: ");
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
            System.out.print(date.plusDays(i));
            System.out.println("            " + df.format(dailyIncome));
            totalIncome += dailyIncome;
        }
        avgIncome = totalIncome/30.0;
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
        System.out.print("Date: ");
        System.out.println("          Incentives Discount: ");
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
                        perResLoss = 0.0;
                    dailyLoss += perResLoss;
                }
            }
            System.out.print(date.plusDays(i));
            System.out.println("                         " + df.format(dailyLoss));
            totalLoss += dailyLoss;
        }
        avgLoss = totalLoss/30.0;
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
        if (!reservation.getCancledStatus()) {
            System.out.println("Date: " + date);
            System.out.println("Customer Name: " + reservation.getName() + "\nRoom Number: " + reservation.getRoomNumber());
            System.out.println("Arrival Date: " + reservation.getDateIn() + ", Departure Date: " + reservation.getDateOut());
            long daysbetween;
            daysbetween = ChronoUnit.DAYS.between(reservation.getDateIn(), reservation.getDateOut());
            System.out.println("Number of nights stayed: " + daysbetween);
            if (reservation.getResType() == ResType.sixty_day || reservation.getResType() == ResType.prepaid) {
                System.out.println("Day reservation was paid: " + reservation.getDatePaid());
            }
            double totalCharge, resCharge, baseRate;
            baseRate = 0; //CHANGE ONCE WE IMPLEMENT GETTING THE BASERATE
            if (!reservation.getChangedStatus()) {
                if (reservation.getResType() == ResType.incentive)
                    resCharge = baseRate * 0.80;
                else if (reservation.getResType() == ResType.prepaid)
                    resCharge = baseRate * 0.75;
                else if (reservation.getResType() == ResType.sixty_day)
                    resCharge = baseRate * 0.85;
                else
                    resCharge = baseRate;
            } else {
                resCharge = baseRate * 1.10;
            }
            totalCharge = resCharge * daysbetween;
            System.out.println("Total Charge: " + df.format(totalCharge));
        } else if (reservation.getCancledStatus() && (reservation.getResType() == ResType.conventional ||
                reservation.getResType() == ResType.incentive) && ChronoUnit.DAYS.between(date, reservation.getDateIn()) <= 3){
            System.out.println("Reservation was cancelled too close to the expected arrival date. You will be billed for the first day");
            System.out.println("Date: " + date);
            System.out.println("Customer Name: " + reservation.getName());
            System.out.println("Expected arrival Date: " + reservation.getDateIn() + ", Expected departure Date: " + reservation.getDateOut());
            double totalCharge, resCharge, baseRate;
            baseRate = 0; //CHANGE ONCE WE IMPLEMENT GETTING THE BASERATE
            if (!reservation.getChangedStatus()) {
                if (reservation.getResType() == ResType.incentive)
                    resCharge = baseRate * 0.80;
                else
                    resCharge = baseRate;
            } else {
                resCharge = baseRate * 1.10;
            }
            totalCharge = resCharge;
            System.out.println("Total Charge: " + df.format(totalCharge));
        } else if (reservation.getCancledStatus() && (reservation.getResType() == ResType.prepaid || reservation.getResType() == ResType.sixty_day)) {
            System.out.println("Date: " + date);
            System.out.println("Customer Name: " + reservation.getName());
            System.out.println("Expected arrival Date: " + reservation.getDateIn() + ", Expected departure Date: " + reservation.getDateOut());
            long daysbetween;
            daysbetween = ChronoUnit.DAYS.between(reservation.getDateIn(), reservation.getDateOut());
            System.out.println("Number of nights stayed: " + daysbetween);
            if (reservation.getResType() == ResType.sixty_day || reservation.getResType() == ResType.prepaid) {
                System.out.println("Day reservation was paid: " + reservation.getDatePaid());
            }
            double totalCharge, resCharge, baseRate;
            baseRate = 0; //CHANGE ONCE WE IMPLEMENT GETTING THE BASERATE
            if (!reservation.getChangedStatus()) {
                if (reservation.getResType() == ResType.incentive)
                    resCharge = baseRate * 0.80;
                else if (reservation.getResType() == ResType.prepaid)
                    resCharge = baseRate * 0.75;
                else if (reservation.getResType() == ResType.sixty_day)
                    resCharge = baseRate * 0.85;
                else
                    resCharge = baseRate;
            } else {
                resCharge = baseRate * 1.10;
            }
            totalCharge = resCharge * daysbetween;
            System.out.println("Total Charge: " + df.format(totalCharge));
        } else if (reservation.getDateIn()== date.minusDays(1) && reservation.getCheckedInStatus()== false) {
            System.out.println("Guest did not show up on expected arrival date. They will be billed for the first day due to the no show policy");
            System.out.println("Date: " + date);
            System.out.println("Customer Name: " + reservation.getName());
            System.out.println("Expected arrival Date: " + reservation.getDateIn() + ", Expected departure Date: " + reservation.getDateOut());
            double totalCharge, resCharge, baseRate;
            baseRate = 0; //CHANGE ONCE WE IMPLEMENT GETTING THE BASERATE
            if (!reservation.getChangedStatus()) {
                if (reservation.getResType() == ResType.incentive)
                    resCharge = baseRate * 0.80;
                else if (reservation.getResType() == ResType.prepaid)
                    resCharge = baseRate * 0.75;
                else if (reservation.getResType() == ResType.sixty_day)
                    resCharge = baseRate * 0.85;
                else
                    resCharge = baseRate;
            } else {
                resCharge = baseRate * 1.10;
            }
            totalCharge = resCharge;
            System.out.println("Total Charge: " + df.format(totalCharge));
        } else {
            System.out.println("Refund was paid, no bill to print");
        }
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
            if (!reservation.getValue().getCancledStatus()) {
                if (reservation.getValue().getResType() == ResType.sixty_day) {
                    if (ChronoUnit.DAYS.between(LocalDate.now(), reservation.getValue().getDateIn()) == reminderDate) {
                        System.out.print("Dear " + reservation.getValue().getEmail() + ",\n"
                                + "Today is 45 days before your expected arrival date. You must pay "
                                + "for your reservation within 15 days, otherwise it will be cancelled\n"
                                + "Thank you for your interest in staying at Ophelias Oasis\n");
                    } else {
                        System.out.print("No emails to be sent today\n");
                    }
                } else {
                    System.out.println("No emails to be sent today");
                }
            }
        }
    }
}
