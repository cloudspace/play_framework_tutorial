package utils.validation;

import play.data.Form;
import java.util.*;

/**
 * Validates for a field missing from the form
 * Most likely used for select or checkbox that doesn't get posted when no options are selected
 */
public class ExistsValidation extends Validation {

    public ExistsValidation(String field, String errorMessage)  {
        super(field, errorMessage);
    }

    public boolean isValid(Form form)  {
        if(!form.data().containsKey(field))  {
            addFieldError();
            return false;
        }
        return true;
    }
}
