package com.example.bookmanager.repository;

import com.example.bookmanager.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
/**
 * Spring Data JPA repository for {@link com.example.bookmanager.model.Book}
 * entities.  Extending {@link JpaRepository} provides standard CRUD operations
 * and pagination support out of the box.
 *
 * <p>Additional query methods may be declared here to support custom search
 * requirements.</p>
 */
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Look up all books whose title contains the supplied text, ignoring case.
     * Useful for simple search functionality.
     *
     * @param title fragment to match within the book title
     * @return list of matching books (possibly empty)
     */
    List<Book> findByTitleContainingIgnoreCase(String title);
}