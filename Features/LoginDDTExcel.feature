Feature: Login Data Driven with Excel

  @dataDrivenExcel
  Scenario Outline: Login Data Driven Excel
    Given User launches browser
    And opens URL "http://localhost/opencart/upload/"
    When User navigates to MyAccount menu
    And clicks on Login link
    Then check User is navigated to MyAccount page by passing Email and Password with excel row "<row_index>"

    Examples:
      |row_index|
      |1|
      |2|
      |3|
