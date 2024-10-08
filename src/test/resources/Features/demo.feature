Feature: author endpoints allow crud operations

  Scenario: user can add an Author

    Given an author NTO

    When the client makes a request to add an author providing an author NTO

    Then the api responses ok

    And the service add operation has been called