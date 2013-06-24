<<<<<<< HEAD
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


### Decoraters

The javax.persistence package offers a number of decorators to help define database interaction.

The @Entity decorator marks a class as being a Model and when play evolutions are run a database table will be generated for it.  

The @id decorator goes on a member of an @Entity class and defines the database id field.

There area number of decorators available between play and javax that are viewable in their respective documentation.

### Validations
Play offers model-level validations in the form of constraint decorators.  The full list can be seen on [this page](http://www.playframework.com/documentation/api/2.0/java/play/data/validation/Constraints.html).

These are nice for simple models, but validations in models can't be used well if you have have multiple forms to generate an object. Since you cant save the object without having it be complete, it's impossible to have multi-page step-based object creation.  To get around this all validations need to be done in the controller.


Controllers
--------------------------------
### Saving and Updating
Often in tutorials the methods save and update are used on models without much description being give.  These methods are almost identical, but misunderstanding them can lead to many issues.  Save works as expected, saving the object to the database if none exists, or updating the existing object.  Update differers in that it takes in an id parameter, sets the current object to have that id, and then calls save.

Update allows you to basically take one object and save it's contents onto an object with another id.  In most cases save is more than enough.

### Parameters
**Optional Parameters in routes**  
=======
Things that we need notes on

models
- decorators to make a model communicate with the database
  - @Entity, @id
  - models are much closer to the database here than in rails
  - adding a finder object to the model
- database setup
  - in memory, file based, postgre
  - what to uncomment in the conf file
- validations in models don't work if you have multiple forms
- parsing json doesn't work in java
- creating and managing evolutions
  - how to stop an evolution from editing
>>>>>>> 918d50a24c397810508a035ba03425a5848f6244

  - note changes from play 2.0 to 2.1
<<<<<<< HEAD

**Reading requests parameters**  
  - in a form and outside of a form 
**Passing variables to views**  

**Cloudspace request validation system**  

**Routes with namespaced controllers**    
When you add your controllers to a package the naming conventions for accessing their respective routes isn't quite intuitive.  When play compiles your project a routes object is added to each controller package.  In documentation you may often see references to things like

    routes.controllers.application.index()

This works fine for controllers that are children of the controllers package, but when in a sub package they needed to be referenced as follows:

    controllers.my_subpackage.routes.Library.index

For consistancy you can also access the base controllers with a similar style:

    controllers.routes.application.index()
  

  
**Form objects in controllers**  
In a controller there is generally one shared static form object for the controller class. For example, below we show a form object based off of the Book model inside the Books controller:

    public class Books extends Controller  {  
        ... 
        final static Form<Book> bookForm = form(Book.class);
        ...         
    }

Inside of an action we can take this form object and upon view rendering, pass in the form and fill it with data from a model object of the corresponding class:

    public static Result edit(Long id)  {
        Book book = Book.find.byId(id);

        if (book == null) {
            // 404 if no book was found
            return notFound("No book with that id was found!");
        } else {
            // Render edit form template
            return ok(edit.render(campaignForm.fill(campaign), book);
        }
    }


**Errors**  
There are two main types of errors that need to be thrown: Form validation errors, and general HTTP errors.

The general HTTP status errors are quite simple to use, and play provides result methods for each.  Instead of retuning ok() from an action you simply return the approrpiate result.  The full list of available results can be found [here](http://www.playframework.com/documentation/api/2.0/java/play/mvc/Results.html#method_summary).  You can see an example of retuning a notFount result (HTP 404) in the previous example.

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

By default play only supports the h2 database format.  In the documentation linked above there are instructions for importing other drivers.  This can also be accompliched wish maven.

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

=======
- reading request parameters
  - in a form and outside of a form
- passing variables to views
- our controller based validation system
- reading routes on namespaced controllers
- setting up the form object in the controller
  - it is static
  - can only be filled in the render line
- setting and reading form errors
  - pulling data out of a form object will throw an exception if there are errors on the form
- response codes
  - explicitly setting 200, 201, 404, etc
- file downloads
  - setting header, mime type etc

views using scala
- escape character
- differences in syntax
  - <- operator
  - square brackets instead of angle brackets
  - using single quotes for symbols
  - passing code blocks to enumerators (ruby style)
  - no else if
- variable definition and scope
- commenting
- how to create new html input templates
>>>>>>> 918d50a24c397810508a035ba03425a5848f6244

