@BookRegression
Feature: Book CRUD Operations
  As an API user
  I want to perform CRUD operations on books
  So that I can manage book records effectively

  @AddBook
  Scenario Outline: Successfully create a new book with valid data
    Given a "author" is created in the system
    And I have valid "book" data
    When I send a "POST" request to "/books"
    Then the response status code should be 201
    And the response should contain the "book" details
    And validate the response against "book-success-schema" JSON schema

  @GetBookById
  Scenario Outline: Successfully retrieve a book by its ID
    Given a "book" is created in the system
    When I send a "GET" request to "/books/<id>"
    Then the response status code should be 200
    And the response should contain the "book" details
    And validate the response against "book-success-schema" JSON schema

  @UpdateBook
  Scenario Outline: Successfully update an existing book
    Given a "book" is created in the system
    And I have valid "book" data
    When I send a "PUT" request to "/books/<id>"
    Then the response status code should be 200
    And the response should contain the "book" details
    And validate the response against "book-success-schema" JSON schema

  @DeleteBook
  Scenario Outline: Successfully delete an existing book
    Given a "book" is created in the system
    When I send a "DELETE" request to "/books/<id>"
    Then the response status code should be 200
