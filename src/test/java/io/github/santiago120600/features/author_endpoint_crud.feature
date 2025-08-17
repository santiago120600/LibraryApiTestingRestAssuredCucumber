@AuthorRegression
Feature: Author CRUD Operations
  As an API user
  I want to perform CRUD operations on authors
  So that I can manage author records effectively

  @AddAuthor
  Scenario Outline: Successfully create a new author with valid data
    Given I have the following valid "author" data:
      | first_name   | last_name   |
      | <first_name> | <last_name> |
    When I send a "POST" request to "/authors"
    Then the response status code should be 201
    And the response should contain the "author" details including:
      | first_name   | last_name   |
      | <first_name> | <last_name> |
    And validate the response against "author-success-schema" JSON schema

    Examples:
      | first_name | last_name |
      | Herman     | Melville  |

  @GetAuthorById
  Scenario Outline: Successfully retrieve a author by its ID
    Given a "author" with ID <id> exists in the system
    When I send a "GET" request to "/authors/<id>"
    Then the response status code should be 200
    And the response should contain the "author" details including:
      | first_name   | last_name   |
      | <first_name> | <last_name> |
    And validate the response against "author-success-schema" JSON schema

    Examples:
      | id | first_name | last_name |
      |  1 | Herman     | Melville  |

  @UpdateAuthor
  Scenario Outline: Successfully update an existing author
    Given I have the following valid "author" data:
      | first_name   | last_name   |
      | <first_name> | <last_name> |
    When I send a "PUT" request to "/authors/<id>"
    Then the response status code should be 200
    And the response should contain the "author" details including:
      | first_name   | last_name   |
      | <first_name> | <last_name> |
    And validate the response against "author-success-schema" JSON schema

    Examples:
      | id | first_name | last_name |
      |  1 | Herman     | Melville  |

  @DeleteAuthor
  Scenario Outline: Successfully delete an existing author
    Given a "author" with ID <id> exists in the system
    When I send a "DELETE" request to "/authors/<id>"
    Then the response status code should be 200

    Examples:
      | id |
      |  1 |
