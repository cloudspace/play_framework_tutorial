package controllers.admin;

import java.util.List;
import play.mvc.*;
import play.data.*;
import models.*;
import views.html.admin.books.*;
/**
 * CRUD for books admin
 */
public class Books extends Controller {

    final static Form<Book> bookForm = form(Book.class);

    public static Result index()  {
        List<Book> books = Book.find.all();

        return ok(index.render(books));
    }

    public static Result show(Long id)  {
        Book book = Book.find.byId(id);

        return ok(show.render(book));
    }

    public static Result newRecord()  {
        Book book = new Book();
        bookForm.fill(book);

        return ok(form.render(bookForm, book));
    }

    public static Result edit(Long id)  {
        Book book = Book.find.byId(id);

        bookForm.fill(book);
        
        return ok(form.render(bookForm, book));
    }

    public static Result create()  {
        Form<Book> filledForm = bookForm.bindFromRequest();

        if(filledForm.hasErrors())  {
            return badRequest(filledForm.errorsAsJson());
        } else {
            Book book = filledForm.get();
            book.save();
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
