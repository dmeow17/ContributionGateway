# Market Data Contribution Gateway

## Objectives

- Allow users to store, retrieve and distribute market data
- Simple dummy spring boot project in kotlin to demonstrate basic functionalities

## What it does, i.e. what endpoints it provides

- Deliverables
    - Business users can:
        - contribute FxQuote
        - retrieve latest FxQuote of specific ccy pair
        - retrieve all FxQuotes contributed for specific ccy pair
    - Audit users can:
        - retrieve validation result (Dummy endpoint)
- Expose basic actuator endpoints (e.g. health, info, logfile, prometheus)
- Simple logging and export them into a file

## What it doesn't do

Everything not mentioned above

## Things to be improved

1. Handle multiple types of quotes
    - Now it only supports FxQuote
    - Quote itself should be extracted as interface/abstract class, and the methods in services should take that as
      arguments
        - If needed, methods themselves could have special handling for certain child classes
2. Larger data stream flood in
    - Now things are simplified and it only uses concurrent collections to keep data in memory
    - In real scenarios, we need to consider pt 1 above and use database as data store
    - Buffer/Queue would be better solution to build lock-free application
