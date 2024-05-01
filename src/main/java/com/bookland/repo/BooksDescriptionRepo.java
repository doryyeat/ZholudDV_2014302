package com.bookland.repo;

import com.bookland.model.BooksDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksDescriptionRepo extends JpaRepository<BooksDescription, Long> {
}
