@Regression
Feature: Test fuctionality of atom healthcare portal

  Background: Login to atom healthcare portal
    Given user lauch atom healthcare url
    When user enter username and password

  Scenario: Verify 
    Then schedule an appointments within one month
   