Feature: A simple payload that can kick off the camel routes

  Background:
    * url baseURL+'/test'
    * configure headers = read('classpath:headers.js')
    * def payload = read('payload.json')

  Scenario: Create and Retrieve a menu by Id

    And request payload
    When method post
    Then status 200

    And request payload
    When method get
    Then status 200

    And request payload
    When method post
    Then status 200

    And request payload
    When method get
    Then status 200

    And request payload
    When method post
    Then status 200

    And request payload
    When method get
    Then status 200


