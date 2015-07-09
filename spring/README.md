# Kontrolletti Client Java Spring

This implementation of the Kontrolletti Java API utilizes Spring's `RestTemplate`
- or more exactly it's interface, called `RestOperations` -
to provide an easy access to the Kontrolletti REST API.

## Usage

```Java

KontrollettiOperations kontrollettiClient = new RestTemplateKontrollettiOperations(restTemplate, "https://kontrolletti.example.org");
String normalizedRepoUrl = kontrollettiClient.normalizeRepositoryUrl("git@github.com:example/any-repo.git");
System.out.println(normalizedRepoUrl);

```

## RestTemplate Requirements

The `RestTemplate` to use with `RestTemplateKontrollettiOperations` **should not** throw exceptions on 404 response status.
For convenience our custom `KontrollettiResponseErrorHandler` can be used.

```Java

RestTemplate restTemplate = new RestTemplate();
restTemplate.setErrorHandler(new KontrollettiResponseErrorHandler());

```