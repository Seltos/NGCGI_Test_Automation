Feature: Jira Automation

Scenario: Atlassian_login
  Given I open application in "Chrome" browser
  When I Navigate to the url "https://vivek-enquero.atlassian.net/"
  When I type text "vivek.verma@enquero.com" into UserName Field
  And I click Submit Button
  When I type text Password into Password Field
  And I click Submit Button
  Then It Navigates to the Homepage


  Scenario: Create Story in Jira
    Given I open the Grid in Jira Software
    When I click on Enquero link
    Then I see create Button and Enter the Details
    And I create the Story with the Type "Task"
    Then I add Summary to the Story as "New customer"
    Then I add Description to the Story as "Login into App with Customer Credentials"
    And I assign Priority as "High"
    Then I set DueDate as "8/May/20"
    And I assign Story to the Person "Automatic"
    Then Story is Created

  Scenario: Create Bug in Jira
    Given I open the Grid in Jira Software
    When I click on Enquero link
    Then I see create Button and Enter the Details
    And I create the Story with the Type "Bug"
    Then I add Summary to the Story as "Create an Instance of Partner"
    Then I add Description to the Story as "Login into App with Customer Credentials"
    And I assign Priority as "High"
    Then I set DueDate as "8/May/20"
    And I assign Story to the Person "Automatic"
    Then Bug is Created

  Scenario: Create Ticket on Failures
    Given I Fail the TestCase

  Scenario: Logout From the Application
    Then I logout from application
    And I close web browser



 # 5/May/20
     #And I story related to Environoment "Test"



















