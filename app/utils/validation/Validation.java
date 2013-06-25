package utils.validation;

import play.data.Form;
import java.util.*;

/**
 * Validation base class
 * Defines interaction with the errors map
 */
public class Validation {
    /**
     * The form field to check
     */
    protected String field;
    /**
     * List of errors populated by the validate method
     */
    protected HashMap errors;

    /**
     * The error message to output
     */
    protected String errorMessage;

    /**
     * An object to validate the form against
     */
    protected Object object;

    /**
     * Basic constructor takes in the field and initializes the errors
     */
    public Validation(String field, String errorMessage)  {
        this.field = field;
        this.errors = new HashMap<String, String>();
        this.errorMessage = errorMessage;
    }

    /**
     * Advanced constructor takes in the field and initializes the errors
     * Also takes in an object to store and validate against later
     */
    public Validation(Object object)  {
        this.errors = new HashMap<String, String>();
        this.object = object;
    }

    /**
     * Basic constructor
     */
    public Validation(String errorMessage)  {
        this.errorMessage = errorMessage;
        this.errors = new HashMap<String, String>();
    }

    /**
     * Basic constructor
     */
    public Validation()  {
        this.errors = new HashMap<String, String>();
    }

    /**
     * Abstract method that shows how to write the actual validity check and errors
     * This should be overridden in child classes
     */
    public boolean isValid(Form form)  {
        errors.put("", "The validation for " + field + " has not yet been written.");
        return false;
    }
    
    /**
     * Adds an error to any field on the form
     * Lets you add errors to nonstandard fields with nonstandard messages
     */
    protected void addCustomError(String otherField, String errorMessage)  {
        errors.put(otherField, errorMessage);
    }

    /**
     * Adds an error for the main field in this validation
     */
    protected void addFieldError()  {
        errors.put(field, errorMessage);
    }

    /**
     * Adds a global error to the form
     */
    protected void addBaseError()  {
        errors.put("", errorMessage);
    }

    public String getField()  {
        return field;
    }

    public HashMap<String, String> getErrors()  {
        return errors;
    }
}
