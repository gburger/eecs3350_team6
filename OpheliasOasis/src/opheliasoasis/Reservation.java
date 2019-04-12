/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opheliasoasis;

import java.time.LocalDate;

/**
 * Represents a single reservation and valid operations on it.
 *
 * @author roanm
 */
public class Reservation {

    private final LocalDate date_in;
    private final LocalDate date_out;

    public LocalDate getDate_in() {
        return date_in;
    }

    public LocalDate getDate_out() {
        return date_out;
    }

    public enum ResType {
        prepaid, sixty_day, conventional, incentive
    };

    public Reservation() {
        this.date_in = date_in;
        this.date_out = date_out;
    }

    public void change_date(LocalDate date_in, LocalDate date_out) {
    }

    public void cancel() {
    }

    public void check_in(int room_num) {
    }

//    public get( ) {} // Placeholder for all getters

}
