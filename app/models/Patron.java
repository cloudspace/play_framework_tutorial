package models;

import java.util.List;
import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;
import play.db.ebean.Model.Finder; //fix to allow queries to run on views

/**
 * A patron of a library
 * they check books from that library in and out
 */
@Entity
public class Patron extends Model {
    /**
     * Database accessor
     */
    public static Finder<Long, Book> find = new Finder<Long, Book>(Long.class, Book.class);

    // -----------------------------------
    // PROPERTIES
    // -----------------------------------

    @Id
    public Long id;

    public String name;

    @OneToMany(mappedBy="patron")
    public List<Transaction> transactions;

    @ManyToOne
    public Library library;

    public Patron() {}
}
