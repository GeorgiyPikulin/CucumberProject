Feature: Login With Valid Credentials

  @sanity @regression
  Scenario: Successful login with valid credentials
    Given User launches browser
    And opens URL "http://localhost/opencart/upload/"
    When User navigates to MyAccount menu
    And clicks on Login link
    And User types in Email "ad@gmail.com" and Password "ad1234"
    And clicks on Login button
    Then User is navigated to MyAccount page
