package org.example.libraries.Repo;

import org.example.libraries.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryClass extends JpaRepository<Book, Integer> {
}