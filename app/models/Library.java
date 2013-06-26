package models;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;
import play.db.ebean.Model.Finder;

/**
 * A library with patrons and books
 */
@Entity
public class Library extends Model {
    /**
     * Database accessor
     */
    public static Finder<Long, Library> find = new Finder<Long, Library>(Long.class, Library.class);

    @Id
    public Long id;

    public String name;
    public String getName() { return name; }
    /**
     * The reverse relationship must be set for a has many
     * Similar to rails inverse_of association option
     */
    @OneToMany(mappedBy="library")
    public List<Book> books;


    @OneToMany(mappedBy="library")
    public List<Patron> patrons;

    /**
     * Default constructor required
     */
    public Library()  {}

    public static HashMap<String, String> selectCollection()  {
        HashMap<String, String> output = new HashMap<String, String>();

        for(Library l : Library.find.all())  {
            output.put(l.id.toString(), l.name);
        }

        return output;
    }
}
