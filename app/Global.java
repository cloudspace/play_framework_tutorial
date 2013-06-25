import play.*;
import play.libs.*;
import java.util.*;
import com.avaje.ebean.*;
import models.*;

public class Global extends GlobalSettings {
    
    public void onStart(Application app) {
        InitialData.insert(app);
    }
    
    static class InitialData {
        
        public static void insert(Application app) {
            //assume the database is empty if there are no books
            if(Ebean.find(Book.class).findRowCount() == 0) {
                
                Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial_data.yml");

                // This order is arbitrary for now (JH 5-9-2013)
                Ebean.save(all.get("books"));
                Ebean.save(all.get("patrons"));
                Ebean.save(all.get("transactions"));
                
            }
        }
    }
}
