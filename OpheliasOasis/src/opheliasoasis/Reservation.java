/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opheliasoasis;

import java.util.Date;

/**
 * Represents a single reservation and valid operations on it.
 *
 * @author roanm
 */
public class Reservation {

    public enum ResType {
        prepaid, sixty_day, conventional, incentive
    };

    public Reservation(ResType res_type, Date date_in, Date date_out,
                       String name, CreditCard cc, String email) {
    }

    public void change_date(Date date_in, Date date_out) {
    }

    public void cancel() {
    }

    public void check_in(int room_num) {
    }

//    public get( ) {} // Placeholder for all getters

}
