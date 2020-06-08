$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("login.feature");
formatter.feature({
  "line": 1,
  "name": "Jira Automation",
  "description": "",
  "id": "jira-automation",
  "keyword": "Feature"
});
formatter.before({
  "duration": 146643252,
  "status": "passed"
});
formatter.scenario({
  "line": 3,
  "name": "Atlassian_login",
  "description": "",
  "id": "jira-automation;atlassian-login",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 4,
  "name": "I open application in \"Chrome\" browser",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "I Navigate to the url \"https://vivek-enquero.atlassian.net/\"",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "I type text \"vivek.verma@enquero.com\" into UserName Field",
  "keyword": "When "
});
formatter.step({
  "line": 7,
  "name": "I click Submit Button",
  "keyword": "And "
});
formatter.step({
  "line": 8,
  "name": "I type text Password into Password Field",
  "keyword": "When "
});
formatter.step({
  "line": 9,
  "name": "I click Submit Button",
  "keyword": "And "
});
formatter.step({
  "line": 10,
  "name": "It Navigates to the Homepage",
  "keyword": "Then "
});
formatter.match({
  "arguments": [
    {
      "val": "Chrome",
      "offset": 23
    }
  ],
  "location": "browser.i_open_application_in_browser(String)"
});
formatter.result({
  "duration": 16918169525,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "https://vivek-enquero.atlassian.net/",
      "offset": 23
    }
  ],
  "location": "browser.iNavigateToTheUrl(String)"
});
formatter.result({
  "duration": 7730986217,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "vivek.verma@enquero.com",
      "offset": 13
    }
  ],
  "location": "browser.iTypeTextIntoUserNameField(String)"
});
formatter.result({
  "duration": 1731577861,
  "status": "passed"
});
formatter.match({
  "location": "browser.iClickSubmitButton()"
});
formatter.result({
  "duration": 500033280,
  "status": "passed"
});
formatter.match({
  "location": "browser.iTypeTextPasswordIntoPasswordField()"
});
formatter.result({
  "duration": 1016436865,
  "status": "passed"
});
formatter.match({
  "location": "browser.iClickSubmitButton()"
});
formatter.result({
  "duration": 313105403,
  "status": "passed"
});
formatter.match({
  "location": "browser.itNavigatesToTheHomepage()"
});
formatter.result({
  "duration": 57921843,
  "status": "passed"
});
formatter.after({
  "duration": 2428580,
  "status": "passed"
});
formatter.before({
  "duration": 240070,
  "status": "passed"
});
formatter.scenario({
  "line": 12,
  "name": "Create Story in Jira",
  "description": "",
  "id": "jira-automation;create-story-in-jira",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 13,
  "name": "I open the Grid in Jira Software",
  "keyword": "Given "
});
formatter.step({
  "line": 14,
  "name": "I click on Enquero link",
  "keyword": "When "
});
formatter.step({
  "line": 15,
  "name": "I see create Button and Enter the Details",
  "keyword": "Then "
});
formatter.step({
  "line": 16,
  "name": "I create the Story with the Type \"Task\"",
  "keyword": "And "
});
formatter.step({
  "line": 17,
  "name": "I add Summary to the Story as \"New customer\"",
  "keyword": "Then "
});
formatter.step({
  "line": 18,
  "name": "I add Description to the Story as \"Login into App with Customer Credentials\"",
  "keyword": "Then "
});
formatter.step({
  "line": 19,
  "name": "I assign Priority as \"High\"",
  "keyword": "And "
});
formatter.step({
  "line": 20,
  "name": "I set DueDate as \"8/May/20\"",
  "keyword": "Then "
});
formatter.step({
  "line": 21,
  "name": "I assign Story to the Person \"Automatic\"",
  "keyword": "And "
});
formatter.step({
  "line": 22,
  "name": "Story is Created",
  "keyword": "Then "
});
formatter.match({
  "location": "browser.iOpenTheGridInJiraSoftware()"
});
formatter.result({
  "duration": 684371,
  "status": "passed"
});
formatter.match({
  "location": "browser.iClickOnEnqueroLink()"
});
formatter.result({
  "duration": 155875,
  "status": "passed"
});
formatter.match({
  "location": "browser.iSeeCreateButtonAndEnterTheDetails()"
});
formatter.result({
  "duration": 1094540,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Task",
      "offset": 34
    }
  ],
  "location": "browser.iCreateTheStoryWithTheType(String)"
});
formatter.result({
  "duration": 23535653598,
  "error_message": "org.openqa.selenium.StaleElementReferenceException: stale element reference: element is not attached to the page document\n  (Session info: chrome\u003d83.0.4103.61)\nFor documentation on this error, please visit: https://www.seleniumhq.org/exceptions/stale_element_reference.html\nBuild info: version: \u00273.141.59\u0027, revision: \u0027e82be7d358\u0027, time: \u00272018-11-14T08:17:03\u0027\nSystem info: host: \u0027LAPTOP-OSEHH6NR\u0027, ip: \u0027192.168.1.11\u0027, os.name: \u0027Windows 10\u0027, os.arch: \u0027amd64\u0027, os.version: \u002710.0\u0027, java.version: \u00271.8.0_241\u0027\nDriver info: org.openqa.selenium.chrome.ChromeDriver\nCapabilities {acceptInsecureCerts: false, browserName: chrome, browserVersion: 83.0.4103.61, chrome: {chromedriverVersion: 83.0.4103.39 (ccbf011cb2d2b..., userDataDir: C:\\Users\\MALASH~1\\AppData\\L...}, goog:chromeOptions: {debuggerAddress: localhost:63274}, javascriptEnabled: true, networkConnectionEnabled: false, pageLoadStrategy: normal, platform: WINDOWS, platformName: WINDOWS, proxy: Proxy(), setWindowRect: true, strictFileInteractability: false, timeouts: {implicit: 0, pageLoad: 300000, script: 30000}, unhandledPromptBehavior: dismiss and notify, webauthn:virtualAuthenticators: true}\nSession ID: 87d6d3f4a3558d66458121c812105bf1\r\n\tat sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)\r\n\tat sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)\r\n\tat sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)\r\n\tat java.lang.reflect.Constructor.newInstance(Constructor.java:423)\r\n\tat org.openqa.selenium.remote.http.W3CHttpResponseCodec.createException(W3CHttpResponseCodec.java:187)\r\n\tat org.openqa.selenium.remote.http.W3CHttpResponseCodec.decode(W3CHttpResponseCodec.java:122)\r\n\tat org.openqa.selenium.remote.http.W3CHttpResponseCodec.decode(W3CHttpResponseCodec.java:49)\r\n\tat org.openqa.selenium.remote.HttpCommandExecutor.execute(HttpCommandExecutor.java:158)\r\n\tat org.openqa.selenium.remote.service.DriverCommandExecutor.execute(DriverCommandExecutor.java:83)\r\n\tat org.openqa.selenium.remote.RemoteWebDriver.execute(RemoteWebDriver.java:552)\r\n\tat org.openqa.selenium.remote.RemoteWebElement.execute(RemoteWebElement.java:285)\r\n\tat org.openqa.selenium.remote.RemoteWebElement.isDisplayed(RemoteWebElement.java:326)\r\n\tat org.openqa.selenium.support.ui.ExpectedConditions.elementIfVisible(ExpectedConditions.java:314)\r\n\tat org.openqa.selenium.support.ui.ExpectedConditions.access$000(ExpectedConditions.java:43)\r\n\tat org.openqa.selenium.support.ui.ExpectedConditions$10.apply(ExpectedConditions.java:300)\r\n\tat org.openqa.selenium.support.ui.ExpectedConditions$10.apply(ExpectedConditions.java:297)\r\n\tat org.openqa.selenium.support.ui.FluentWait.until(FluentWait.java:249)\r\n\tat StepDefinitions.browser.iCreateTheStoryWithTheType(browser.java:163)\r\n\tat ✽.And I create the Story with the Type \"Task\"(login.feature:16)\r\n",
  "status": "failed"
});
formatter.match({
  "arguments": [
    {
      "val": "New customer",
      "offset": 31
    }
  ],
  "location": "browser.iAddSummaryToTheStoryAs(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "arguments": [
    {
      "val": "Login into App with Customer Credentials",
      "offset": 35
    }
  ],
  "location": "browser.iAddDescriptionToTheStoryAs(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "arguments": [
    {
      "val": "High",
      "offset": 22
    }
  ],
  "location": "browser.iAssignPriorityAs(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "arguments": [
    {
      "val": "8/May/20",
      "offset": 18
    }
  ],
  "location": "browser.iSetDueDateAs(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "arguments": [
    {
      "val": "Automatic",
      "offset": 30
    }
  ],
  "location": "browser.iAssignStoryToThePerson(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "location": "browser.storyIsCreated()"
});
formatter.result({
  "status": "skipped"
});
formatter.embedding("image/png", "embedded0.png");
formatter.write("Create Story in Jira");
formatter.after({
  "duration": 11375728532,
  "status": "passed"
});
formatter.before({
  "duration": 289564,
  "status": "passed"
});
formatter.scenario({
  "line": 24,
  "name": "Create Bug in Jira",
  "description": "",
  "id": "jira-automation;create-bug-in-jira",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 25,
  "name": "I open the Grid in Jira Software",
  "keyword": "Given "
});
formatter.step({
  "line": 26,
  "name": "I click on Enquero link",
  "keyword": "When "
});
formatter.step({
  "line": 27,
  "name": "I see create Button and Enter the Details",
  "keyword": "Then "
});
formatter.step({
  "line": 28,
  "name": "I create the Story with the Type \"Bug\"",
  "keyword": "And "
});
formatter.step({
  "line": 29,
  "name": "I add Summary to the Story as \"Create an Instance of Partner\"",
  "keyword": "Then "
});
formatter.step({
  "line": 30,
  "name": "I add Description to the Story as \"Login into App with Customer Credentials\"",
  "keyword": "Then "
});
formatter.step({
  "line": 31,
  "name": "I assign Priority as \"High\"",
  "keyword": "And "
});
formatter.step({
  "line": 32,
  "name": "I set DueDate as \"8/May/20\"",
  "keyword": "Then "
});
formatter.step({
  "line": 33,
  "name": "I assign Story to the Person \"Automatic\"",
  "keyword": "And "
});
formatter.step({
  "line": 34,
  "name": "Bug is Created",
  "keyword": "Then "
});
formatter.match({
  "location": "browser.iOpenTheGridInJiraSoftware()"
});
formatter.result({
  "duration": 309475,
  "status": "passed"
});
formatter.match({
  "location": "browser.iClickOnEnqueroLink()"
});
formatter.result({
  "duration": 63715,
  "status": "passed"
});
formatter.match({
  "location": "browser.iSeeCreateButtonAndEnterTheDetails()"
});
formatter.result({
  "duration": 134257,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Bug",
      "offset": 34
    }
  ],
  "location": "browser.iCreateTheStoryWithTheType(String)"
});
formatter.result({
  "duration": 6095587311,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Create an Instance of Partner",
      "offset": 31
    }
  ],
  "location": "browser.iAddSummaryToTheStoryAs(String)"
});
formatter.result({
  "duration": 11047450901,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Login into App with Customer Credentials",
      "offset": 35
    }
  ],
  "location": "browser.iAddDescriptionToTheStoryAs(String)"
});
formatter.result({
  "duration": 466757347,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "High",
      "offset": 22
    }
  ],
  "location": "browser.iAssignPriorityAs(String)"
});
formatter.result({
  "duration": 583531164,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "8/May/20",
      "offset": 18
    }
  ],
  "location": "browser.iSetDueDateAs(String)"
});
formatter.result({
  "duration": 767518462,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Automatic",
      "offset": 30
    }
  ],
  "location": "browser.iAssignStoryToThePerson(String)"
});
formatter.result({
  "duration": 3593386001,
  "status": "passed"
});
formatter.match({
  "location": "browser.bugIsCreated()"
});
formatter.result({
  "duration": 310818476,
  "status": "passed"
});
formatter.after({
  "duration": 5754296,
  "status": "passed"
});
formatter.before({
  "duration": 166115,
  "status": "passed"
});
formatter.scenario({
  "line": 36,
  "name": "Create Ticket on Failures",
  "description": "",
  "id": "jira-automation;create-ticket-on-failures",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 37,
  "name": "I Fail the TestCase",
  "keyword": "Given "
});
formatter.match({
  "location": "browser.iFailTheTestCase()"
});
formatter.result({
  "duration": 3908257,
  "error_message": "java.lang.AssertionError: expected [true] but found [false]\r\n\tat org.testng.Assert.fail(Assert.java:94)\r\n\tat org.testng.Assert.failNotEquals(Assert.java:496)\r\n\tat org.testng.Assert.assertTrue(Assert.java:42)\r\n\tat org.testng.Assert.assertTrue(Assert.java:52)\r\n\tat StepDefinitions.browser.iFailTheTestCase(browser.java:300)\r\n\tat ✽.Given I Fail the TestCase(login.feature:37)\r\n",
  "status": "failed"
});
formatter.embedding("image/png", "embedded1.png");
formatter.write("Create Ticket on Failures");
formatter.after({
  "duration": 2444197548,
  "status": "passed"
});
formatter.before({
  "duration": 163839,
  "status": "passed"
});
formatter.scenario({
  "line": 39,
  "name": "Logout From the Application",
  "description": "",
  "id": "jira-automation;logout-from-the-application",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 40,
  "name": "I logout from application",
  "keyword": "Then "
});
formatter.step({
  "line": 41,
  "name": "I close web browser",
  "keyword": "And "
});
formatter.match({
  "location": "browser.logout()"
});
formatter.result({
  "duration": 7151012307,
  "status": "passed"
});
formatter.match({
  "location": "browser.i_close_web_browser()"
});
formatter.result({
  "duration": 1905607353,
  "status": "passed"
});
formatter.after({
  "duration": 1583213,
  "status": "passed"
});
});