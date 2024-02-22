# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
[![Sequence Diagram2](Phase 2 Sequence Diagram)]
(https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJodPpDEZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iHus30V6EFWkGh1VMKbN+etJeIGTLXUcTkSxv2UFq7q7dUzyJ9vlyrjygSDjc7tQf3WP4VEdwGpTA1w95fjfMrVbPNTf92hHo9TZDkz1JU1M3DP17TFKCAOxN0PXHBEw19SNl1Ue9elDKD0LcKNuh6fx4yTVN0zw7NczQfNtF0AxjB0FA7UrLR9GYWtvF8TASKbHCoCiaI+BPJI3jSdIuwkHs8kov1CIZTCoknNiVVGWT0CXT0sOlF0PxgY4wB-NV1LQPdEMPV4Tx+NDs0vCBQXg996WwxTjSmF8V0xW89JkFAEBOFAjLUi18LMnVgKPYSvlEmyYOBeywwQ8LkMfDysJS0N5Ow3iExgFNk0wPMC3o4tBhkCthhgABxHlHk4+seMbZgMoEmIqreDt0i0HkZJC7MsofdglOGGqygkYKsz9TSUIZN9dJxAygpMsK72eI93i+ayTLshynScpDBq9CZWR5NLV3muV9JOUbJFGFagLWyzNt+AAqHb9NqpK7xSoaPrGs6XJbVruv+kcYxysi0wKEHJGo2jCwYoxsD0KBsH8+Av1UG6PC4hsAmah9WwSZJxJh3rJvQdMYZOsphyBhStM-RVfBu0ZqZ5SploB0I5u8haTiWvqpq+h7GSe09YrteLdtVEXnJ+o7zzKbnecAnEFQJVn2bKe7nMe-UwONGnpm11QRTFGHpH2mNXJh5k+ABlqjh5e2suIprIapl2+Dh4qi2Mcw-MndwYAAKQgadqovIwFAQUBrUa-GGa6QTYlOTqydMIXKYKHK4AgScoEqO2fYG3pfoAKwjtBWbzgvoGL73OZVUKVdVxCN0Mi0JugjS5bdfWNol7bpcS63AZQp9smNlX9s72umvzwvG7Ke3dYHsXj2ev6UHtmAAHVmSSAAJWN8aX6B3st-ubcZmBHcJ1r77B7KPby1NCpov3EZ0YBLEQRUsBgDYDRoQZwrgcYNRysnGgrUhIiTEhkdQZdXIgH8ngO6s8LrynQWMdeFk3wIJgC9G+E9HzPy0rfFOFCujgzfvlT++YgA)
