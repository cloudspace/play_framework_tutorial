This is your new Play 2.0 application
=====================================

This file will be packaged with your application, when using `play dist`.





Things that we need notes on

models
- decorators to make a model communicate with the database
  - @Entity, @id
  - models are much closer to the database here than in rails
  - adding a finder object to the model
- database setup
  - in memory, file based, postgre
- validations in models don't work if you have multiple forms
- parsing json doesn't work in java

controllers
- making a controller action save and update
- optional parameters in routes
  - note changes from play 2.0 to 2.1
- reading request parameters
  - in a form and outside of a form
- passing variables to views
- our controller based validation system
- reading routes on namespaced controllers
- setting up the form object in the controller
  - it is static
  - can only be filled in the render line
- setting and reading form errors
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
- no else if
- variable definition and scope
- commenting
- how to create new html input templates

