Things to write
- example of a form input template
- json
- note: the basic play tutorials are well written and informative.  This tutorial assumes you have an understanding of the play basics.  We are trying to cover the next few steps of play development.
- play devs are unhelpful
  - http://play.lighthouseapp.com/projects/82401/tickets/504-support-tuple-22-not-just-tuple-18-in-apidataformsscala
- documentation issues: v1 vs v2 and java vs scala

Working with Play
===
---
     
   
Getting Started
---

###Java on OS X

Play can run on both Java 1.6 and 1.7, however we have chosen to use 1.7 because it offers some nice language features.

If you have no current version of Java (OS X no longer ships with it by default), it is best to install the [1.6 JRE package from Apple](http://support.apple.com/kb/dl1572) (as it will set up all necessarly links), and then install the [1.7 JDK form Oracle](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html).

If you already have Java installed, upgrading to 1.7 is as simple as running the installer.

### Installing Play

The Play framework can be downloaded from [here](http://www.playframework.com/download).  Once extracted, the folder can be placed anywhere, however it is preferable to sit alongside Java in /usr/share/java/.  This is the same path maven, ant, and junit install to.

Once installed add the play directory to your PATH in ~/.bash_profile, or simply run

    export PATH=$PATH:/usr/share/java/play-2.X.X

To set the path for just the current terminal session.



Models
---
In play the models are much closer to the database than seen in a framework like rails.  Instead of writing the migrations and having the models built from the db, the models actually define the database and the migrations can be automatically generated from the changes.


### Decorators

The javax.persistence package offers a number of decorators to help define database interaction.

The @Entity decorator marks a class as being a Model and when play evolutions are run a database table will be generated for it.  

The @id decorator goes on a member of an @Entity class and defines the database id field.

There area number of decorators available between play and javax that are viewable in their respective documentation.

### Validations
Play offers model-level validations in the form of constraint decorators.  The full list can be seen on [this page](http://www.playframework.com/documentation/api/2.0/java/play/data/validation/Constraints.html).  Many default validations map directly to database changes.  For example, the @Required decorator sets the column in the db to not null.

These are nice for simple models, but validations in models can't be used well if you have have multiple forms to generate an object. Since you cant save the object without having it be complete, it's impossible to have multi-page step-based object creation.  To get around this all validations need to be done in the controller.


Controllers
--------------------------------
### Saving and Updating
Often in tutorials the methods save and update are used on models without much description being given.  These methods are almost identical, but misunderstanding them can lead to many issues.  Save works as expected, saving the object to the database if none exists, or updating the existing object.  Update differers in that it takes in an id parameter, sets the current object to have that id, and then calls save.

Update allows you to basically take one object and save it's contents onto an object with another id.  In most cases save is more than enough.

One gotcha is that form data pulled from the request does not necessarily include the object id.  If you don't check that exists and set it, calling save can create a duplicate of the object in the database.

### Parameters
**Optional Parameters in routes**  

  - note changes from play 2.0 to 2.1

**Reading request parameters**
Reading request parameters is much more difficult here than in other frameworks.  Reading get parameters that aren't part of a form submission is almost impossible.  Reading form parameters looks like this:

    String name = request().body().asFormUrlEncoded().get("name").toString();

If the form does not include the field, this call will throw an exception.  This can be a problem on checkboxes and selects because if the user doesn't select any options, html will not include the input in the post.

I have written some helper methods to make accessing the request easier.

    private static String requestParam(String name)  {
            return requestParamArray(name)[0];
    }

    private static String[] requestParamArray(String name)  {
        if(request().body().asFormUrlEncoded().containsKey(name)) {
            return request().body().asFormUrlEncoded().get(name);
        } else {
            return new String[0];
        }
    }

###Controller Validations
Controller based validations are preferable to model based validations due to their flexibility.  The reject method is used to add errors to the form.  This example shows an error on the name field and a general error on the form object.

    public static Result update(Long id)  {
        Form<Book> filledForm = bookForm.bindFromRequest();

        if(request().body().asFormUrlEncoded().get("name").toString().equals(""))  {
            filledForm.reject("name", "Name can't be blank.");
        }
        if(Library.find.all().size() == 0)  {
            filledForm.reject("Books cannot be created if there are no libraries in the system.");
        }

        if(filledForm.hasErrors())  {
            Book book = Book.find.byId(id); //can't pull out of the form if there are errors
            return ok(edit.render(filledForm, book));
        } else {
            Book book = filledForm.get();
            book.update(id);
            return redirect(controllers.admin.routes.Books.show(book.id));
        }
    }

After the form is processed, the hasErrors method is used to determine if the form is valid.  If hasErrors returns false, the get method will throw an exception.  This is why we pass the book object into the view along with the form object.  In the view, you cannot depend on filledForm.get().getId() to setup routes.  You have to explicitly pass an id or a book object.

**Cloudspace controller validation system**  
Reading a form object is for validations is awfully wordy and leads to some duplicated code in things like phone number and email validations.  We have written a small validation framework to standardize some of this code. 

    import utils.validation.*;

    public static Result update(Long id)  {
        Form<Book> filledForm = bookForm.bindFromRequest();
        Validator v = new Validator(filledForm);

        v.add(new RequiredValidation("name", "Name is required"));
        v.add(new LibrariesExistValidation("Template type is required"));

        filledForm = v.validate();
        ...

The Validator object collects Validation objects then runs them to determine the form errors.

A few basic validations are included as a demonstration.  Each one needs a constructor and an isValid method.

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

The constructor should generally call the Validation superclass constructor and set any additional data needed for the validation.  The isValid method should return true if valid.  If an error is detected, it should set the error message with addFieldError or addBaseError and return false.Look at the Validation class for a few more options on setting and managing errors.

###Interacting with views

**Passing variables into views**
For some reason, the play tutorials gloss over passing variables into views.  The two sticking points are no optional arguments and the scala syntax on the view.

In the controller:
    
    public static Result edit(Long id)  {
        Book book = Book.find.byId(id); 

        return ok(edit.render(bookForm.fill(book), book)
    }

In the view:

    @(bookForm: Form[models.Book], book: models.Book)

    @base("Books", "books") {
        Your content here
    }
    
Note that using models.Book is necessary because it occurs before any import models.* calls.  In scala, the variable goes before the type in variable declarations.  Rails uses a similar method to get variables onto views by copying every instance variable onto the controller onto the view instance.

**Routes with namespaced controllers**    
When you add your controllers to a package the naming conventions for accessing their respective routes isn't quite intuitive.  When play compiles your project a routes object is added to each controller package.  In documentation you may often see references to things like

    routes.controllers.application.index()

This works fine for controllers that are children of the controllers package, but when in a sub package they needed to be referenced as follows:

    controllers.my_subpackage.routes.Library.index

For consistency you can also access the base controllers with a similar style:

    controllers.routes.application.index()
  
**Form objects in controllers**  
In a controller there is generally one shared static form object for the controller class. For example, below we show a form object based off of the Book model inside the Books controller:

    public class Books extends Controller  {  
        ... 
        final static Form<Book> bookForm = form(Book.class);
        ...         
    }

Inside of an action we can take this form object and upon view rendering, pass in the form and fill it with data from a model object of the corresponding class:

    private static final Form<Book> bookForm = new Form<Book>();

    public static Result edit(Long id)  {
        Book book = Book.find.byId(id);

        if (book == null) {
            // 404 if no book was found
            return notFound("No book with that id was found!");
        } else {
            // Render edit form template
            return ok(edit.render(bookForm.fill(book), book);
        }
    }

The book object is accessible from the form by calling bookForm.get().  You should not depend on this.  If there are validation errors, calling bookForm.get() will throw an exception instead of returning the object.  In all of our form views, we pass both the form and the model instance to the view.

Note that in the play tutorials, the form object is final.  Be careful about editing the form or using fill without setting the output to a new oform.  It is easy to write a bunch of form modification code that doesn't make any changes.  In our examples, we are calling fill in the view render to avoid these issues.


**Errors**  
There are two main types of errors that need to be thrown: Form validation errors, and general HTTP errors.

The general HTTP status errors are quite simple to use, and play provides result methods for each.  Instead of retuning ok() from an action you simply return the approrpiate result.  The full list of available results can be found [here](http://www.playframework.com/documentation/api/2.0/java/play/mvc/Results.html#method_summary).  You can see an example of retuning a notFound result (HTP 404) in the previous example.

**response() and setting headers**  
In each controller action there is an available method called response().  When called it returns the current response object which will eventually be returned to the end user.  We can use this object to set any appropriate HTTP Headers.

For example, to set the HTTP headers to force a file download of a list of book titles we could do as follows

    public static Result downloadBookList(Long id)  {
        List<Books> bookList = Books.find.all();
        
        if (bookList == null) return notFound("No books found");
        
        String output = "";

        for(Book book : bookList) output += book.title + "\n";

        response().setContentType("text");
        response().setHeader("Content-Disposition", "attachment; filename=\"" + subscriberList.name + ".csv\"");
        
        return ok(output);
    }
    

Scala Views
--------------------------------
Play uses [scala](http://www.scala-lang.org/) as it's templating language.  You can also use scala for the models and controllers, but it's sometimes best used in just views so the java backend code is usable in non-play applications.  The full play scala documentation is available [here](http://www.playframework.com/documentation/api/2.1.1/scala/index.html#package).

See [this](http://www.playframework.com/documentation/2.1.1/JavaTemplates) tutorial on how to create Scala tempaltes.  It's written from a Java developers perspective and shows the transititions between the languages well.

**Creating html forms**  
There are a number of form template helpers provided by play.  These can hook in to form rendered objects and will automatically provide the correct values as well as display appropriate field errors.

See the [scala form documentation](http://www.playframework.com/documentation/2.0/ScalaFormHelpers).

You can also manually create forms in straight HTML, you just have to make sure the input names match up correctly.  Play translates underscores to periods (amongst some other things) so if you're going to manually write a form it's best to create an input helper, check the name, then write your form using the correct name.


**Tips**  

- There is no elseif in scala. You unfortunately have to nest if statements for the same result.
- Defining variables reqires you to specify a block of code in which the variable exists in.  This makes for very messy code; it is best to avoid  this if possible and handle as much as you can in the controller
- Comments can be multi-line and are use the syntax *@\* comment here \*@*





Database Setup
--------------------------------
See the play database documentation [here](http://www.playframework.com/documentation/2.0.4/JavaDatabase)

By default play only supports the h2 database format.  In the documentation linked above there are instructions for importing other drivers.  This can also be accomplished wish maven.

Database setup is defined in *application.conf*.  There are four necessary parameters: driver, url, user, pass. Here is an example postgresql config:

    db.default.driver=org.postgresql.Driver
    db.default.url="jdbc:postgresql:cloudspace_data"
    db.default.user="cloudspace"
    db.default.pass="spacecloud"

You can also define multiple databases by repeating the same code, but changing where it says 'default' to something else.

    db.users.driver=org.postgresql.Driver
    db.users.url="jdbc:postgresql:cloudspace_users"
    db.users.user="cloudspace"
    db.users.pass="spacecloud"

Once your databases are defined you must tell the ebeans ORM which models go to which database.  If you had the used the two previous databases and had your user-related models in their own package, you could do the following:

    ebean.default="models.*"
    ebean.users="models.users.*"

###Seeding the database
Play loads a class called Global.java every time it starts.  We can hook into the onStart method of that class to perform database seeding.  In this project, the file is located at app/Global.java.  This location can be changed in the application config.  The seeding system needs to check that the database is not already seeded each time runs to avoid overwriting data.  We are using a naive check on the Book model to control all of the database seeding.  A more robust check would be needed for a real project.


    public class Global extends GlobalSettings {
        
        public void onStart(Application app) {
            InitialData.insert(app);
        }
        
        static class InitialData {
            
            public static void insert(Application app) {
                //assume the database is empty if there are no books
                if(Ebean.find(Book.class).findRowCount() == 0) {
                    
                    Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial_data.yml");

                    Ebean.save(all.get("libraries"));
                    Ebean.save(all.get("books"));
                    Ebean.save(all.get("patrons"));
                    Ebean.save(all.get("transactions"));
                    
                    
                }
            }
        }
    }

In our system, we are storing the seed data in the file conf/initial_data.yml.  Ebean uses reflection to turn the !!models.Book key into an object and call it's constructor.  This version required exact spelling in the yml file to work.  Another option would be to just store the data in the yml file and manually call a constructor that takes a map of data.

    books:
    - !!models.Book
        name: "A Tale of Two Cities"
        description: "A Tale of Two Cities (1859) is a novel by Charles Dickens, set in London and Paris before and during the French Revolution. With well over 200 million copies sold, it ranks among the most famous works in the history of fictional literature.[2]"
        library: !!models.Library
            id: 1

