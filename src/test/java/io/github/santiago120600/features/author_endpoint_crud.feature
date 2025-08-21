@AuthorRegression
Feature: Author CRUD Operations
  As an API user
  I want to perform CRUD operations on authors
  So that I can manage author records effectively

  @AddAuthor
  Scenario Outline: Successfully create a new author with valid data
    Given I have valid "author" data
    When I send a "POST" request to "/authors"
    Then the response status code should be 201
    And the response should contain the "author" details
    And validate the response against "author-success-schema" JSON schema

   @GetAuthorById
  Scenario Outline: Successfully retrieve an author by its ID
    Given a "author" is created in the system
    When I send a "GET" request to "/authors/<id>"
    Then the response status code should be 200
    And the response should contain the "author" details
    And validate the response against "author-success-schema" JSON schema

  @UpdateAuthor
  Scenario Outline: Successfully update an existing author
    Given a "author" is created in the system
    And I have valid "author" data
    When I send a "PUT" request to "/authors/<id>"
    Then the response status code should be 200
    And the response should contain the "author" details
    And validate the response against "author-success-schema" JSON schema

  @DeleteBook
  Scenario Outline: Successfully delete an existing author
    Given a "author" is created in the system
    When I send a "DELETE" request to "/authors/<id>"
    Then the response status code should be 200
