package models;

import java.util.List;
import java.util.Date;
import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;
import play.db.ebean.Model.Finder; //fix to allow queries to run on views

/**
 * A library book
 * It belongs to a library and can be checked in and out
 */
@Entity
public class Book extends Model {
    /**
     * Database accessor
     */
    public static Finder<Long, Book> find = new Finder<Long, Book>(Long.class, Book.class);


    // -----------------------------------
    // PROPERTIES
    // -----------------------------------

    @Id
    public Long id;
    public Long getId()  { return id; }

    public String name;
    public String getName() { return name; }
    
    @Column(columnDefinition = "TEXT")
    public String description;

    /**
     * The library_id column is automatically generated using this decorator
     */
    @ManyToOne
    public Library library;

    @OneToMany(mappedBy="book")
    public List<Transaction> transactions;

    /**
     * Play requires a constructor that takes no arguments
     * You can set default values here
     */
    public Book()  {
        name = "Unknown";
        description = "";
    }

    /**
     * You can also create your own constructors for convenience
     */
    public Book(String name, String description)  {
        this.name = name;
        this.description = description;
    }

    /**
     * Checks the book in
     */
    public boolean checkIn()
    {
        // Fail if the book is already checked in
        if (this.checkedIn()) return false;

        // Fail if there is no associated transactions
        if (this.transactions.size() == 0) return false;

        // Check the book in
        Transaction lastTransaction = this.transactions.get(this.transactions.size() - 1);
        lastTransaction.checkinAt = new Date();

        lastTransaction.save();
        
        return true;
    }

    /**
     * Checks the book out
     */
    public boolean checkOut(Patron patron)
    {
        // Fail if the book is not checked in
        if (!this.checkedIn()) return false;

        // Check the book in
        Transaction transaction = new Transaction();
        transaction.book = this;
        transaction.patron = patron;
        transaction.save();
        
        return true;
    }


    /**
     * Test if the book is checked in by looking at it's transactions
     */
    public boolean checkedIn()  {
        for(Transaction t : transactions)  {
            if(t.checkedOut())  {
                return false;
            }
        }
        return true;
    }

    /**
     * Test if the book is checked out
     */
    public boolean checkedOut()  {
        return !checkedIn();
    }

    /**
     * Status of the book
     * At some point, add overdue to this information
     */
    public String status()  {
        if(checkedOut())  {
            return "Checked Out";
        } else {
            return "Checked In";
        }
    }
}
