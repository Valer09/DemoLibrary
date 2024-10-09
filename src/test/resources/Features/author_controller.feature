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

  Scenario: user search an Author by name
    Given the following list of authors in the system:
      | Isni |    Name       |    LastName      | Birth       |
      | aaab |   xyName      |    authorName    | 09/15/1991  |
      | aaac |  authorName   |    XYlastname    | 09/15/1991  |
      | aaad |  authorxyName | authorLastName   | 09/15/1991  |
      | aaba |  authorName   | authorXYLastName | 09/15/1991  |

    And the author service is set to simulate OK on find by name

    When the client makes a request find the author by name xy

    Then the service find by name operation has been called

    And the api responses is 200

    And the response contains the list of authors


  Scenario Outline: user update isni for an author
    Given the following Author nto:
      | Isni |    Name       |    LastName      | Birth       |
      | aaaa |    name       |    lastname      | 09/15/1991  |

    And the author service is set to simulate <service_outcome> on update isni with bbbb

    When the client makes a request update isni with bbbb

    Then the service update isni operation has been called

    And the api responses is <api_response>

    And the response contains the value <response_value>
    Examples:
      |    service_outcome   | api_response | response_value |
      |         OK           |     200      |      1         |
      |       CONFLICT       |     409      |     null       |
      |      NOT_FOUND       |     404      |     null       |
      |     SERVER_ERROR     |     500      |     null       |