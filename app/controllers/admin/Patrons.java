package controllers.admin;

import java.util.List;
import play.mvc.*;
import play.data.*;
import models.*;
import views.html.admin.patrons.*;

/**
 * CRUD for patrons admin
 */
public class Patrons extends Controller {

    final static Form<Patron> patronForm = form(Patron.class);

    public static Result index()  {
        List<Patron> patrons = Patron.find.all();

        return ok(index.render(patrons));
    }

    public static Result show(Long id)  {
        Patron patron = Patron.find.byId(id);

        //return ok(show.render(patron));
        return ok("Not implemented yet");
    }

    public static Result newRecord()  {
        Patron patron = new Patron();
        patronForm.fill(patron);

        return ok(form.render(patronForm, patron));
    }

    public static Result edit(Long id)  {
        Patron patron = Patron.find.byId(id);

        patronForm.fill(patron);
        
        return ok(form.render(patronForm, patron));
    }

    public static Result create()  {
        Form<Patron> filledForm = patronForm.bindFromRequest();

        if(filledForm.hasErrors())  {
            return badRequest(filledForm.errorsAsJson());
        } else {
            Patron patron = filledForm.get();
            patron.save();
            return ok("Not implemented yet");
        }
    }

    public static Result update(Long id)  {
        return ok("Not implemented yet");
    }

    public static Result destroy(Long id)  {
        return ok("Not implemented yet");
    }
}
