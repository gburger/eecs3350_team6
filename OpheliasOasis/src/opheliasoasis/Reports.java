/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opheliasoasis;

import java.util.Date;

/**
 * Interface and workhorse for all report generating tasks.
 *
 * @author roanm
 */
public class Reports {

    public Reports(Records records) {
    }

    /**
     * Expected occupancy per day for 30 days.
     *
     * @param date
     */
    public void mk_daily_occupancy(Date date /* = today */) {
    }

    /**
     * Arrivals listed by name for the day.
     *
     * @param date
     */
    public void mk_daily_arrivals(Date date /* = today*/) {
    }

    /**
     * Expected occupancy over 30 days.
     *
     * @param date
     */
    public void mk_expected_occupancy(Date date /* = today*/) {
    }

    /**
     * Expected income for the next 30 days.
     */
    public void mk_expected_income(Date date /* = today*/) {
    }

    /**
     * Expected losses from applying incentives.
     */
    public void mk_incentives(Date date /* = today*/) {
    }

    /**
     * Make a bill for the given reservation.
     *
     * @param reservation
     */
    public void mk_bill(Reservation reservation) {
    }

    /**
     * Find 60-day reservations which require reminding and send the emails.
     */
    public void send_reminders() {
    }

    private void print_physical() {
    }

    private void send_email() {
    }
}
