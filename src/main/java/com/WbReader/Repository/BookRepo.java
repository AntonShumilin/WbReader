package com.WbReader.Repository;

import com.WbReader.Data.Book;
import com.WbReader.Data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

    List<Book> findByUser(User user);
}
