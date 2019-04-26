/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opheliasoasis;

import javafx.util.Pair;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * A serializable list of all reservations and base rates.
 *
 * @author roanm
 */
public class Records {

    private String db_loc;
    private ArrayList<Reservation> res_db;
    private ArrayList<Integer> rate_db;

    public Records() {
        this("");
    }
    
    public ArrayList<Reservation> getResDB() {
        return res_db;
    }

    public Records(String db_loc) {
        this.db_loc = db_loc;
        // Only read_in database if needed by operation.
        this.res_db = null;
        this.rate_db = null;
    }

    /**
     * Read in database only if not already in memory.
     */
    private void fetch_db() {
        if (this.res_db == null || this.rate_db == null) {
            read_db();
        }
    }

    public List<Pair<Integer, Reservation>> lookup(LocalDate date_in) {

        fetch_db();

        List<Pair<Integer, Reservation>> result = new ArrayList<>();
        for (int i = 0; i < res_db.size(); i++) {
            Reservation res = res_db.get(i);
            if (date_in.isEqual(res.getDateIn())
                    || date_in.isEqual(res.getDateOut())
                    || (date_in.isAfter(res.getDateIn())
                        && date_in.isBefore(res.getDateOut()))) {
               result.add(new Pair(i, res));
            }
        }

        return result;
    }

    public Reservation edit_reservation(int res_id,
                                        Reservation.ResType res_type,
                                        LocalDate date_in, LocalDate date_out,
                                        String name,
                                        CreditCard cc, String email) {
    }

    public Reservation create_reservation(Reservation.ResType res_type,
                                          LocalDate date_in, LocalDate date_out,
                                          String name,
                                          CreditCard cc, String email) {

        fetch_db();

        // TODO: Update for real constructor.
        res_db.add(new Reservation());

        write_db();

        return res_db.get(res_db.size());
    }

    public Reservation checkin_reservation(int res_id) {
    }

    public void change_baseRate(LocalDate date, float new_rate) {
    }

    public void backup_records() {
    }

    private void read_db() {
        Pair<ArrayList<Reservation>, ArrayList<Integer>> dbs;

        try (FileInputStream file = new FileInputStream(db_loc);
             ObjectInputStream in = new ObjectInputStream(file)) {

            dbs = (Pair<ArrayList<Reservation>, ArrayList<Integer>>) in.readObject();

            this.res_db = dbs.getKey();
            this.rate_db = dbs.getValue();

        } catch (IOException | ClassNotFoundException e) {
            out.println("Database file is unreadable or not found. Working on empty database.");
            this.res_db = new ArrayList<>();
            this.rate_db = new ArrayList<>();
        }
    }

    private void write_db() {

        if (this.res_db == null || this.rate_db == null) {
            out.println("Writing blank structures to database.");
            this.res_db = new ArrayList<>();
            this.rate_db = new ArrayList<>();
        }

        try (FileOutputStream file = new FileOutputStream(db_loc);
             ObjectOutputStream out = new ObjectOutputStream(file)) {

            out.writeObject(new Pair<>(res_db, rate_db));

        } catch (IOException e) {
            out.println("Unable to write to database file.");
            e.printStackTrace();
        }
    }

}
