package com.WbReader.Data;

import org.springframework.data.repository.CrudRepository;

public interface BookRepo extends CrudRepository<Book, Long> {
}
