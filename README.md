# order_book

### Developer notes

java version: 17
maven version: 3.9.9

How to run the application:
`mvn spring-boot:run`

How to run tests:
`mvn test`

### Assumptions:
- Management of tickers is not included, tickers here is a fixed list of enums
- There is no check for possible max quantity for sale or buy
- All the orderBook history is stored in memory
- All times are handled as local time, no timezone logic
- Only one currency("USD") is supported, therefore no currency conversion is needed

### API endpoints
- POST /order-book
Request example:
```
{
  "ticker": "SAVE",
  "quantity": 10,
  "price": 100,
  "side": "BUY",
  "currency": "USD"
}
```
Response:
```
200 OK
400 Bad Request - if mandatory fields are missing or invalid
500 Internal Server Error
```

- GET /order-book/{id}
Response:
```
200 OK
{
  "id": 9901f46f-240b-4d65-be6b-fe9b8e1a7a9e,
  "ticker": "SAVE",
  "quantity": 10,
  "price": 100,
  "side": "BUY",
  "currency": "USD"
}
400 Bad Request - if the id is not a valid UUID
404 Not Found
500 Internal Server Error
```

- GET /order-book/summary?ticker=SAVE&date=2021-01-01&side=BUY
Response:
```
{
  "ticker": "SAVE",
  "date": "2021-01-01",
  "side": "BUY",
  "averagePrice": 1000,
  "maxPrice": 1000,
  "minPrice": 1000,
  "currency": "USD"
}
```

To improve:
- Unit tests could be cleaner, with less duplication
- Add more validation of requests
- Error handling and response messages
- Make ticker and side enums
- Price should be its own object, with number and currency
- Currency should be handled as standard ISO 4217 currency code

Nice to think about:
- Generate API documentation
- Authentication and authorization
- Rate limiting
- Caching
- Logging
- Separate the DB entity from the domain model

