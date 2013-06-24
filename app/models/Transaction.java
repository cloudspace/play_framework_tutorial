package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;
import play.db.ebean.Model.Finder; //fix to allow queries to run on views

/**
 * The process of checking out and checking in a book
 */
@Entity
public class Transaction extends Model {
    /**
     * Database accessor
     */
    public static Finder<Long, Transaction> find = new Finder<Long, Transaction>(Long.class, Transaction.class);

    @Id
    public Long id;

    @ManyToOne
    public Patron patron;
    @ManyToOne
    public Book book;

    public Date checkoutAt;
    public Date checkinAt;

    public int daysBeforeLate;

    /**
     * Default constructor sets checkout date to today and checkout period to two weeks
     */
    public Transaction()  {
      checkoutAt = new Date();
      daysBeforeLate = 14;
    }

    /**
     * Whether the patron is allowed to checkout a book
     * They must both have the same library
     */
    public boolean checkoutPermitted()  {
        if(patron == null || book == null)  {
            return false;
        } else {
            return patron.library == book.library;
        }
    }

    /**
     * The transaction has not yet been started or is complete
     */
    public boolean checkedIn()  {
        return checkoutAt != null && checkinAt != null;
    }

    /**
     * The book has been checked out but not yet checked back in
     */
    public boolean checkedOut()  {
        return checkoutAt != null && checkinAt == null;
    }

    /**
     * The book has been checked out and has not been return in the proper timeframe
     * Books are no longer overdue as soon as they are checked in
     */
    public boolean overdue()  {
        if(checkedOut())  {
            Calendar c = Calendar.getInstance();
            c.setTime(checkoutAt);    
            c.add(Calendar.DATE, daysBeforeLate);

            Date today = new Date();

            return(today.getTime() > c.getTime().getTime());
        }
        return false;
    }
}
