Feature: author endpoints allow crud operations

  Scenario Outline: user can add an Author

    Given the following Author nto:
      |       Isni       |    Name      |  LastName       | Birth       |
      | 1234567890123456 |  authorName  | authorLastName  | 09/15/1991  |
    And the author service is set to simulate <service_outcome> on add author

    When the client makes a request to add the author providing the nto

    Then the service add operation has been called

    And the api responses is <api_response>

    And the response contains the author with the isni <response_author>

    Examples:
     | service_outcome | api_response | response_author   |
     |       OK        |     201      | 1234567890123456  |
     |    CONFLICT     |     409      |      empty        |


  Scenario Outline: user search an Author by isni
    Given the following Author nto:
      |       Isni       |    Name      |  LastName       | Birth       |
      | 1234567890123456 |  authorName  | authorLastName  | 09/15/1991  |
    And the author service is set to simulate <service_outcome> on find by isni

    When the client makes a request find the author by isni 1234567890123456

    Then the service find by isni operation has been called

    And the api responses is <api_response>

    And the response contains the author with the isni <response_author>

    Examples:
      |     service_outcome    | api_response | response_author   |
      |          OK            |     200      | 1234567890123456  |
      |       NOT_FOUND        |     404      |      empty        |

  Scenario: user search an Author by name
    Given the following list of authors in the system:
      | Isni             |    Name       |    LastName      | Birth       |
      | 1234567890123456 |   xyName      |    authorName    | 09/15/1991  |
      | 1234567890123457 |  authorName   |    XYlastname    | 09/15/1991  |
      | 1234567890123458 |  authorxyName | authorLastName   | 09/15/1991  |
      | 1234567890123459 |  authorName   | authorXYLastName | 09/15/1991  |

    And the author service is set to simulate OK on find by name

    When the client makes a request find the author by name xy

    Then the service find by name operation has been called

    And the api responses is 200

    And the response contains the list of authors


  Scenario Outline: user update isni for an author
    Given the following Author nto:
      |       Isni       |    Name       |    LastName      | Birth       |
      | 1234567890123456 |    name       |    lastname      | 09/15/1991  |

    And the author service is set to simulate <service_outcome> on update isni with 0987654321012345

    When the client makes a request update isni with 0987654321012345

    Then the service update isni operation has been called

    And the api responses is <api_response>

    And the response contains the value <response_value>
    Examples:
      |    service_outcome   | api_response | response_value |
      |         OK           |     200      |      1         |
      |       CONFLICT       |     409      |     null       |
      |      NOT_FOUND       |     404      |     null       |
      |     SERVER_ERROR     |     500      |     null       |

  Scenario Outline: user updates an Author

    Given the following Author nto:
      |       Isni       |    Name      |  LastName       | Birth       |
      | 1234567890123456 |  authorName  | authorLastName  | 09/15/1991  |
    And the author service is set to simulate <service_outcome> on update author with this nto:
      |       Isni       |    Name      |  LastName       | Birth       |
      | <isni_to_update> |  updateName  | updatedLastName | 01/01/1991  |

    When the client makes a request to update the author providing the nto

    Then the service update operation has been called

    And the api responses is <api_response>

    And the response contains the updated author with the isni <response_author>

    Examples:
      | service_outcome | api_response | response_author   | isni_to_update   |
      |       OK        |     200      | 1234567890123456  | 1234567890123456 |
      |    NOT_FOUND    |     404      |      empty        | 0234567890123456 |
      |  SERVER_ERROR   |     500      |      empty        | 1234567890123456 |
