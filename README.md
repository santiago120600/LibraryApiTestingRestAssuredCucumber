# Library API Testing

Automated tests for Library API using Cucumber and RestAssured.

## How to Run

```sh
docker-compose up --build
```

### Proxy UI
visit `http://localhost:8081/` and enter the password defined in `.env` file

### Run locally
`mvn test -Dtest="io.github.santiago120600.runners.BookTestRunner" -Denv="mock" -Dconfig="local" -Dproxy="false"`