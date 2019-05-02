/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opheliasoasis;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static java.lang.System.out;

/**
 * A serializable list of all reservations and base rates.
 *
 * @author roanm
 */
public class Records {

    private String db_loc;
    private ArrayList<Reservation> res_db;
    private TreeMap<LocalDate, Float> rate_db;
    // To be an absolute path to a mapped network drive on a production deployment.
    private final String backup_loc = "backup.db";

    public Records() {
        this("oo.db");
   }

    public ArrayList<Reservation> getResDB() {
        return res_db;
    }

    public Records(String db_loc) {
        this.db_loc = db_loc;
        // Only read_in database if needed by operation.
        this.res_db = null;
        this.rate_db = null;

        fetch_db();
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
        Reservation res;
        res = this.res_db.get(res_id);

        if (date_in != null && date_in != res.getDateIn()) {
            res.setDateIn(date_in);
        }
        if (date_out != null && date_out != res.getDateOut()) {
            res.setDateOut(date_out);
        }
        if (name != null && name != res.getName()) {
            res.setName(name);
        }
        if (cc != null && cc != res.getCreditCard()) {
            res.setCreditCard(cc);
        }
        if (email != null && email != res.getEmail()) {
            res.setEmail(email);
        }

        write_db();

        return res;
    }

    public Reservation create_reservation(Reservation.ResType res_type,
                                          LocalDate date_in, LocalDate date_out,
                                          String name,
                                          CreditCard cc, String email) {

        // TODO: Update for real constructor.
        res_db.add(new Reservation(res_type,date_in, date_out, name, cc, email));

        write_db();

        return res_db.get(res_db.size() - 1);
    }

    public void change_baseRate(LocalDate date, float new_rate) {

        rate_db.put(date, new_rate);

        write_db();
    }

    public Float get_baseRate(LocalDate date) {
        return rate_db.get(date);
    }

    public void backup_records() {
        write_db(backup_loc);
    }

    public void assignDailyRoomnumbers(LocalDate date_in){
        List<Integer> result = occupiedRoomnumbers(date_in);
        fetch_db();
        for (int i = 0, j =1; (i < res_db.size()& j<=45 ); i++, j++) {
            Reservation res = res_db.get(i);
            if (date_in.isEqual(res.getDateIn()) && (!(result.contains(j)))) {
               res.setRoomNumber(j);
            }
        }


    }

    // gets list of roomnumbers that has been already assigned
    public List<Integer> occupiedRoomnumbers(LocalDate date_in){
        fetch_db();

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < res_db.size(); i++) {
            Reservation res = res_db.get(i);
            if (date_in.isEqual(res.getDateIn())
                    || date_in.isEqual(res.getDateOut())
                    || (date_in.isAfter(res.getDateIn())
                        && date_in.isBefore(res.getDateOut()))) {
               result.add(res.getRoomNumber());
            }
        }
        return result;

    }

    private void read_db() {
        Pair<ArrayList<Reservation>, TreeMap<LocalDate, Float>> dbs;

        try (FileInputStream file = new FileInputStream(db_loc);
             ObjectInputStream in = new ObjectInputStream(file)) {

            dbs = (Pair<ArrayList<Reservation>, TreeMap<LocalDate, Float>>) in.readObject();

            this.res_db = dbs.getKey();
            this.rate_db = dbs.getValue();

        } catch (IOException | ClassNotFoundException e) {
            out.println("Database file is unreadable or not found. Working on empty database.");
            this.res_db = new ArrayList<>();
            this.rate_db = new TreeMap<>();
        }
    }

    private void write_db() {
        write_db(db_loc);
    }

    private void write_db(String loc) {

        if (this.res_db == null || this.rate_db == null) {
            out.println("Writing blank structures to database.");
            this.res_db = new ArrayList<>();
            this.rate_db = new TreeMap<>();
        }

        try (FileOutputStream file = new FileOutputStream(loc);
             ObjectOutputStream out = new ObjectOutputStream(file)) {

            out.writeObject(new Pair<>(res_db, rate_db));

        } catch (IOException e) {
            out.println("Unable to write to database file.");
            e.printStackTrace();
        }
    }

    public String toString() {
        return "Records@\"%s\"(\n" + this.res_db.toString() + "\n" + this.rate_db.toString() + "\n)\n";
    }

}
