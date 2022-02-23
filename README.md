# investment
Monolith backend service

## Services
This project has the Loader and the backend API service
- In future, we will separate them

## Rules
### Restrict data returned from the API
- For any database models that contains information that maybe sensitive or dangerous to be exposed, they should return a client-friendly wrapper class. These classes must end with 'Client.java'.

### Status Code Returned by API
- 401 Unauthorised and 403 Forbidden - only to be returned by the SpringBoot authentication framework.
- The logic in our APIs can return other status code such as:
    - 422 UNPROCESSABLE_ENTITY
    - 400 BAD_REQUEST
    - 500 INTERNAL_SERVER_ERROR
- When to throw ResponseStatusException:
    - When there is an exception error raised by the code (e.g. NullPointer, SQL Exception)
    - Or, the error is highly important and we'd need the stack trace to debug.
    - These are usually 500 INTERNAL_SERVER_ERROR