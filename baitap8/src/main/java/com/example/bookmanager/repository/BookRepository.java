package com.example.bookmanager.repository;

import com.example.bookmanager.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);

    @Query("SELECT DISTINCT b.category FROM Book b")
    List<String> findDistinctCategory();

    @Query("SELECT b FROM Book b " +
           "WHERE (:keyword = '' OR LOWER(b.title) LIKE CONCAT('%', LOWER(:keyword), '%') " +
           "OR LOWER(b.author) LIKE CONCAT('%', LOWER(:keyword), '%')) " +
           "AND (:category = '' OR LOWER(b.category) = LOWER(:category))")
    Page<Book> search(@Param("keyword") String keyword, @Param("category") String category, Pageable pageable);

}