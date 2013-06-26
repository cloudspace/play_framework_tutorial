package utils.validation;

import play.data.Form;
import models.Patron;

/**
 * Checks if the patron has any overdue transactions
 * If any are overdue, returns not valid and adds the error message
 */
public class OverdueTransactionExistsValidation extends Validation  {
    private Patron patron;

    public OverdueTransactionExistsValidation(String field, String errorMessage, Patron patron)  {
        super(field, errorMessage);
        this.patron = patron;
    }

    public boolean isValid(Form form)  {
        if(patron.hasOverdueBooks())  {
            addFieldError();
            return false;
        }
        return true;
    }
}
