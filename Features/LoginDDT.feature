Feature: Login Data Driven

  @dataDriven
  Scenario Outline: Login Data Driven
    Given User launches browser
    And opens URL "http://localhost/opencart/upload/"
    When User navigates to MyAccount menu
    And clicks on Login link
    And User types in Email "<email>" and Password "<password>"
    And clicks on Login button
    Then User is navigated to MyAccount page

    Examples: 
      | email                     | password |
      | ad@gmail.com              | ad1234   |
      | pavanoltraining@gmail.com | test@123 |
