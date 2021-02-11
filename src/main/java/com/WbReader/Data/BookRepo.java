package com.WbReader.Data;

import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {

    List<Book> findByUser(User user);
}
