package utils.validation;

import play.data.Form;
import java.util.regex.*;

/**
 * Convenience validation so you don't have to include an email regex all over the code
 */
public class EmailFormatValidation extends RegexMatchValidation  {

    /**
     * Setup the superclass validation with the email regex
     * Note that this is just a demonstration
     * It does not include a good quality email regex
     */
    public EmailFormatValidation(String field, String errorMessage)  {
        super(field, errorMessage, Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"));
    }
}
