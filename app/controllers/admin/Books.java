package controllers.admin;

import java.util.List;
import play.mvc.*;
import play.data.*;
import models.*;
import views.html.books.*;
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
        return ok("Not implemented yet");
    }

    public static Result update()  {
        return ok("Not implemented yet");
    }

    public static Result destroy()  {
        return ok("Not implemented yet");
    }
}
