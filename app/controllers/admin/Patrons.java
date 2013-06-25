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

        return ok(show.render(patron));
    }

    public static Result newRecord()  {
        Patron patron = new Patron();

        return ok(newRecord.render(patronForm.fill(patron), patron));
    }

    public static Result edit(Long id)  {
        Patron patron = Patron.find.byId(id);

        return ok(edit.render(patronForm.fill(patron), patron));
    }

    public static Result create()  {
        Form<Patron> filledForm = patronForm.bindFromRequest();

        if(filledForm.hasErrors())  {
            Patron patron = new Patron();
            return ok(newRecord.render(filledForm, patron));
        } else {
            Patron patron = filledForm.get();
            patron.save();
            return redirect(controllers.admin.routes.Patrons.show(patron.id));
        }
    }

    public static Result update(Long id)  {
        Form<Patron> filledForm = patronForm.bindFromRequest();

        if(filledForm.hasErrors())  {
            Patron patron = Patron.find.byId(id);
            return ok(newRecord.render(filledForm, patron));
        } else {
            Patron patron = filledForm.get();
            patron.update(id);
            return redirect(controllers.admin.routes.Patrons.show(patron.id));
        }
    }

    public static Result destroy(Long id)  {
        Patron patron = Patron.find.byId(id);

        if(patron == null)  {
            return badRequest("Patron not found");
        } else  {
            patron.delete();
            return redirect(controllers.admin.routes.Patrons.index());
        }
    }
}
