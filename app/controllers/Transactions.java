package controllers;

import java.util.List;
import play.mvc.*;
import play.data.*;
import models.*;
import views.html.*;

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
        // List<Transaction> openTransactionList = Transaction.find
        //                                                    .where().isNull("checkinAt").where().eq("library.id",library.id).findList();

        List<Book> loanedBookList = Book.find
                                           .fetch("library").fetch("transactions")
                                           .where().eq("library.id",library.id)
                                           .where().isNull("transactions.checkinAt")
                                           .findList();

        List<Book> availableBookList = Book.find
                                           .fetch("library").fetch("transactions").where().eq("library.id",library.id)
                                           .where().isNotNull("transactions.checkinAt")
                                           .findList();
        
        return ok(transactions.render(library, loanedBookList, availableBookList));
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
    public static Result saveCheckout(Long id)  {
        return badRequest("Not implemented yet");
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

        return redirect(controllers.routes.Transactions.index());

    }
}
