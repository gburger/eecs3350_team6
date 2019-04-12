/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opheliasoasis;

import javafx.util.Pair;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;


/**
 * A serializable list of all reservations and base rates.
 *
 * @author roanm
 */
public class Records {

    public Records(String db_loc) {
    }

    public void lookup(LocalDate date_in) {
    }

    /*public Reservation edit_reservation(int res_id,
                                        Reservation.ResType res_type,
                                        LocalDate date_in, LocalDate date_out,
                                        String name,
                                        CreditCard cc, String email) {
    }*/

    public Reservation create_reservation(Reservation.ResType res_type,
                                          LocalDate date_in, LocalDate date_out,
                                          String name,
                                          CreditCard cc, String email) {
    }*/

    /*public Reservation checkin_reservation(int res_id) {
    }*/

    public void change_baseRate(LocalDate date, float new_rate) {
    }

    public void backup_records() {
    }

}
