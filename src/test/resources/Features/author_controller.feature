Feature: author endpoints allow crud operations

  Scenario Outline: user can add an Author

    Given the following Author nto:
      | Isni |    Name      |  LastName       | Birth       |
      | test |  authorName  | authorLastName  | 09/15/1991  |
    And the author service is set to simulate <service_outcome> on add author

    When the client makes a request to add the author providing the nto

    Then the service add operation has been called

    And the api responses is <api_response>

    And the response contains the author with the isni <response_author>

    Examples:
     | service_outcome | api_response | response_author |
     |       OK        |     201      |      test       |
     |    CONFLICT     |     409      |      empty      |


  Scenario Outline: user search an Author by isni
    Given the following Author nto:
      | Isni |    Name      |  LastName       | Birth       |
      | abcd |  authorName  | authorLastName  | 09/15/1991  |
    And the author service is set to simulate <service_outcome> on find by isni

    When the client makes a request find the author by isni abcd

    Then the service find by isni operation has been called

    And the api responses is <api_response>

    And the response contains the author with the isni <response_author>

    Examples:
      |     service_outcome    | api_response | response_author |
      |          OK            |     200      |      abcd       |
      |       NOT_FOUND        |     404      |      empty      |