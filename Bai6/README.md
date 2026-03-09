# Book Manager

Simple Spring Boot application demonstrating CRUD operations on a `Book` entity.

## Structure

- `model/Book.java` — JPA entity.
- `repository/BookRepository.java` — Spring Data JPA repository.
- `controller/BookController.java` — MVC controller with list, create, edit, delete and search functionality.
- `resources/templates` — Thymeleaf views (`list.html` and `form.html`).

## Running

```sh
./mvnw spring-boot:run
```

Open http://localhost:8080/books in a browser.

## Added features

- Search box on list page filters books by title (case-insensitive).
- Basic Spring Security integration:
  * in-memory users `admin` (ROLE_ADMIN) and `user` (ROLE_USER)
  * only authenticated users may view the book list
  * only ADMIN can add/edit/delete records
  * regular USER accounts **cannot** even submit the form or perform POST
    requests; security rules block write operations at the URL level
  * users are redirected to `/books` immediately after successful login
  * Thymeleaf security dialect used to show/hide UI elements
- Thorough Javadoc comments added to all Java classes.
