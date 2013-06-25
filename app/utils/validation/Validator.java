package utils.validation;

import java.util.ArrayList;
import java.util.HashMap;
import play.data.Form;

/**
 * setup a validator for a form, register validations, and update the form errors
 *
 */
public class Validator {
    /**
     * The form object to add errors to
     */
    private Form form;

    /**
     * List of validations to be performed when validate() is called
     */
    private ArrayList<Validation> validations;

    /**
     * Basic constructor, take in form, initialize fields
     */
    public Validator(Form form)  {
        this.form = form;

        validations = new ArrayList<Validation>();
    }
    
    /**
     * Convenience method to add the built in RequiredValidation to the validations list
     * This is just a nicer way to write add(new RequiredValidation(...))
     */
    public void addRequiredValidation(String field, String errorMessage)  {
        validations.add(new RequiredValidation(field, errorMessage));
    }

    /**
     * Add an instantiated validation object to the validations list
     * This allows custom validations on individual forms
     */
    public void add(Validation v) {
        validations.add(v);
    }

    /**
     * Iterate over the validation objects and run their validations
     * After running all, return the form with the new error messages if any were added
     */
    public Form validate()  {
        for(Validation v : validations) {
            runValidation(v);
        }
        return form;
    }

    /**
     * Run an individual validation, if it is not valid, read the errors and add them to the form
     * Note the hack to make global errors work
     * This should do nothing if the field is valid
     */
    private void runValidation(Validation v) {
        if(!v.isValid(form)) {
            HashMap<String, String> errors = v.getErrors();
            for(String key : errors.keySet())  {
                // global form error 
                if(key.trim().equals(""))  {
                    form.reject(errors.get(key));
                } else { //field error
                    form.reject(key, errors.get(key));
                }
            }
        }
    }
}
