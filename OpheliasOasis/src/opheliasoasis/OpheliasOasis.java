/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opheliasoasis;

import java.util.Date;
import java.util.List;

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
    }

    public OpheliasOasis() {
    }

    private void parse() {
    }

    private void res_Create() {
    }

    private void res_ChangeDate() {
    }

    /**
     * Prompt a query and return a list of matching reservations.
     * @param cc
     * @param date_made
     * @param date_out
     * @param date_in
     * @param name
     * @param room_number
     * @param email
     * @param rate
     * @param type
     * @param is_canceled
     * @return
     */
    private List lookup(CreditCard cc,
                        Date date_made,
                        Date date_out,
                        Date date_in,
                        String name,
                        int room_number,
                        String email,
                        float rate,
                        Reservation.ResType type,
                        boolean is_canceled) {
    }

    /**
     * Interactively lookup reservations and specify util one reservation is found.
     *
     * @param cc
     * @param date_made
     * @param date_out
     * @param date_in
     * @param name
     * @param room_number
     * @param email
     * @param rate
     * @param type
     * @param is_canceled
     * @return
     */
    private int choose_single(CreditCard cc,
                       Date date_made,
                       Date date_out,
                       Date date_in,
                       String name,
                       int room_number,
                       String email,
                       float rate,
                       Reservation.ResType type,
                       boolean is_canceled) {
    }

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
