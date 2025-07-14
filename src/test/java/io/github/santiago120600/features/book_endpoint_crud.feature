@BookRegression
Feature: Book CRUD Operations
  As an API user
  I want to perform CRUD operations on books
  So that I can manage book records effectively

  @AddBook
  Scenario Outline: Successfully create a new book with valid data
    Given I have the following valid book data:
      | book_name   | isbn   | aisle   | authorId   |
      | <book_name> | <isbn> | <aisle> | <authorId> |
    When I send a "POST" request to "/book"
    Then the response status code should be 201
    And the response should contain the book details including:
      | book_name   | isbn   | aisle   | authorId   |
      | <book_name> | <isbn> | <aisle> | <authorId> |
    And validate the response against "book-success-schema" JSON schema

    Examples:
      | book_name               | isbn          | aisle | authorId |
      | The Old Man and the Sea | 9781476787855 |     1 |        1 |

  @GetBookById
  Scenario Outline: Successfully retrieve a book by its ID
    Given a book with ID <id> exists in the system
    When I send a "GET" request to "/book/<id>"
    Then the response status code should be 200
    And the response should contain the book details including:
      | book_name   | isbn   | aisle   | authorId   |
      | <book_name> | <isbn> | <aisle> | <authorId> |
    And validate the response against "book-success-schema" JSON schema

    Examples:
      | id | book_name               | isbn          | aisle | authorId |
      |  1 | The Old Man and the Sea | 9781476787855 |     2 |        1 |

  @UpdateBook
  Scenario Outline: Successfully update an existing book
    Given I have the following valid book data:
      | book_name   | isbn   | aisle   | authorId   |
      | <book_name> | <isbn> | <aisle> | <authorId> |
    When I send a "PUT" request to "/book/<id>"
    Then the response status code should be 200
    And the response should contain the book details including:
      | book_name   | isbn   | aisle   | authorId   |
      | <book_name> | <isbn> | <aisle> | <authorId> |

    Examples:
      | id | book_name               | isbn          | aisle | authorId |
      |  1 | The Old Man and the Sea | 9781476787855 |     2 |        1 |

  @DeleteBook
  Scenario Outline: Successfully delete an existing book
    Given a book with ID "<id>" exists in the system
    When I send a "DELETE" request to "/book/<id>"
    Then the response status code should be 204

    Examples:
      | id |
      |  1 |
