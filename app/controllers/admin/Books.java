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

        return ok(newRecord.render(form(Book.class).fill(book), book));
    }

    public static Result edit(Long id)  {
        Book book = Book.find.byId(id);
        
        return ok(edit.render(bookForm.fill(book), book));
    }

    public static Result create()  {
        Form<Book> filledForm = bookForm.bindFromRequest();

        if(filledForm.hasErrors())  {
            //can't pull out of the form if there are errors
            Book book = new Book();
            return ok(newRecord.render(filledForm, book));
        } else {
            Book book = filledForm.get();
            book.save();
            return redirect(controllers.admin.routes.Books.show(book.id));
        }
    }

    public static Result update(Long id)  {
        Form<Book> filledForm = bookForm.bindFromRequest();

        if(filledForm.hasErrors())  {
            //can't pull out of the form if there are errors
            Book book = Book.find.byId(id);
            return ok(edit.render(filledForm, book));
        } else {
            Book book = filledForm.get();
            book.update(id);
            return redirect(controllers.admin.routes.Books.show(book.id));
        }
    }

    public static Result destroy(Long id)  {
        Book book = Book.find.byId(id);

        if(book == null)  {
            return badRequest("Book not found");
        } else  {
            book.delete();
            return redirect(controllers.admin.routes.Books.index());
        }
    }
}
