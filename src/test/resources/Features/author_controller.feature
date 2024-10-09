Feature: author endpoints allow crud operations

  Scenario Outline: user can add an Author

    Given the following Author nto:
      | Isni |    Name      |  LastName       | Birth       |
      | test |  authorName  | authorLastName  | 09/15/1991  |
    And the author service is set to simulate <service_outcome>

    When the client makes a request to add the author providing the nto

    Then the service add operation has been called

    And the api responses is <api_response>

    Examples:
     | service_outcome | api_response |
     |       OK        |     201      |
     |    CONFLICT     |     409      |