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

    public enum ResType {
        prepaid, sixty_day, conventional, incentive
    };

    public Reservation(ResType res_type, LocalDate date_in, LocalDate date_out,
                       String name, CreditCard cc, String email) {
    }

    public void change_date(LocalDate date_in, LocalDate date_out) {
    }

    public void cancel() {
    }

    public void check_in(int room_num) {
    }

//    public get( ) {} // Placeholder for all getters

}
