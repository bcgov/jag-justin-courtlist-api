
# jag-justin-courtlist-api

[![img](https://img.shields.io/badge/Lifecycle-Experimental-339999)](https://github.com/bcgov/repomountie/blob/master/doc/lifecycle-badges.md) 
[![Maintainability](https://api.codeclimate.com/v1/badges/689bc526eb193c7602d6/maintainability)](https://codeclimate.com/github/bcgov/jag-justin-courtlist-api/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/689bc526eb193c7602d6/test_coverage)](https://codeclimate.com/github/bcgov/jag-justin-courtlist-api/test_coverage)

Ministry of Attorney General - JUSTIN Court List Data Extract

## build

```bash
mvn clean install
```

## test

```
mvn clean verify
```

### Update Version

```
mvn versions:set -DartifactId=*  -DgroupId=*
```

## Configuration

You should use environment variables to configure the jag court list api

| Environment Variable            | Type    | Description                                  | Notes                          |
| ------------------------------- | ------- | -------------------------------------------- | ------------------------------ |
| PORT                            | Integer | web application server port                  | defaulted to `8083`            |
| COURTLIST_BASEURL               | String  | ORDS service base url                        | not defaulted                  |
| COURTLIST_USERNAME              | String  | ORDS Username                                | not defaulted                  |
| COURTLIST_PASSWORD              | String  | ORDS Password                                | not defaulted                  |
| ISSUER_URI                      | String  | Keycloak Auth Url                            | not defaulted                  |  not defaulted                  |
| RESOURCE_ID                     | String  | Keycloak Resource                            | defaulted to `justin-courtlist-api` |
| PRINCIPLE_ATTRIBUTE             | String  | Using name                                   | defaulted to `preferred_username`        |

