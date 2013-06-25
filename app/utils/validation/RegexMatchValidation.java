package utils.validation;

import play.data.Form;
import java.util.regex.*;

/**
 * Validator that takes in a regular expression as an additional argument
 * It compares the form field to the regular expression and adds an error if they don't match
 */
public class RegexMatchValidation extends Validation {
    private Pattern regex;

    public RegexMatchValidation(String field, String errorMessage, Pattern regex)  {
        super(field, errorMessage);
        this.regex = regex;
    }

    public boolean isValid(Form form) {
        String value = form.data().get(field).toString();
        Matcher matcher = regex.matcher(value);
        if(!matcher.find()) {
            addFieldError();
            return false;
        }
        return true;
    }
}
