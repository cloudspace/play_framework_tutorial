package controllers;

import java.util.List;
import java.util.ArrayList;
import play.mvc.*;
import play.data.*;
import models.*;
import views.html.*;
import com.avaje.ebean.Expr;

public class Transactions extends Controller {

    final static Form<Transaction> transactionForm = form(Transaction.class);

    /**
     * List all of the bobadRequests
     * include information about the library and checked in/out status, overdue
     */
    public static Result index()  {
        Library firstLibrary = Library.find.all().get(0);
        return libraryIndex(firstLibrary.id);
    }

    public static Result libraryIndex(Long id)  {
        Library library = Library.find.byId(id);

        List<Book> loanedBookList = Book.find
                                           .fetch("library").fetch("transactions")
                                           .where().eq("library.id",library.id)
                                           .where().isNull("transactions.checkinAt")
                                           .findList();

        // This is not the best way to do this, but ebeans is weird
        List<Book> allBooks = Book.find
                                       .fetch("library").fetch("transactions")
                                       .where().eq("library.id",library.id).findList();

        List<Book> availableBookList = new ArrayList<Book>();

        for (Book book : allBooks)
        {
            if (!loanedBookList.contains(book))
                availableBookList.add(book);
        }
        
        List<Patron> patrons = Patron.find.where().eq("library.id",library.id).findList();

        return ok(transactions.render(library, patrons, loanedBookList, availableBookList));
    }

    /**
     * Form to checkout a bug
     */
    public static Result checkout(Long bookId)  {
        return badRequest("Not implemented yet");
    }

    /**
     * save the checkout to the db
     * validate on:
     * - can't checkout a bobadRequest twice
     * - bobadRequest and patron must be attached to the same library
     * - checkoutAt must be set
     */
    public static Result saveCheckout(Long bookId)  {
        Book book = Book.find.byId(bookId);

        if(!request().body().asFormUrlEncoded().containsKey("patron.id")) return badRequest("No patron selected");
        String patronId = request().body().asFormUrlEncoded().get("patron.id")[0];
        Patron patron = Patron.find.byId(Long.parseLong(patronId, 10));

        book.checkOut(patron);


        return redirect(controllers.routes.Transactions.libraryIndex(book.library.id));
    }

    /**
     * Form to checkin a bobadRequest
     */
    public static Result checkin(Long id)  {
        return badRequest("Not implemented yet");
    }

    /**
     * save the checkin to the db
     * validate on:
     * - checkinAt must be set
     */
    public static Result saveCheckin(Long bookId)  {
        Book book = Book.find.byId(bookId);
        book.checkIn();

        return redirect(controllers.routes.Transactions.libraryIndex(book.library.id));
    }
}
