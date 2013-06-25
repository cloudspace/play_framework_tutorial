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
        List<Transaction> transactionList = Transaction.find.all();
        return ok(transactions.render(transactionList));
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
        return badRequest("Not implemented yet");
    }
}
