package com.WbReader.Services;

import com.WbReader.CustomExeptions.BookNotFoundException;
import com.WbReader.CustomExeptions.CustomException;
import com.WbReader.Data.Book;
import com.WbReader.Repository.BookRepo;
import com.WbReader.Data.User;
import com.WbReader.Repository.UserRepo;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
//@Scope("session")
public class BookService {

    ConcurrentHashMap<String, Book> currentBookMap = new ConcurrentHashMap<>();

    @Autowired
    BookRepo bookRepo;

    @Value("${app.filepath}")
    String uploadFileDir;

    @PostConstruct
    void checkUploadDir() throws IOException {
        Path path = Paths.get(uploadFileDir + "/tmp");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Files.walkFileTree(path, new DeleteTmpFileVisitor());
    }

    public boolean addBook(MultipartFile userFile, User user) throws IOException, XmlException {
        boolean result = false;
        Book book = new Book();

        if (userFile != null && userFile.getOriginalFilename() != null && !userFile.getOriginalFilename().isEmpty()) {
            String tmpFileName = UUID.randomUUID().toString() + "." + userFile.getOriginalFilename();
            String tmpFilePath = uploadFileDir + "/tmp/" + tmpFileName;
            Path tmpFile = Paths.get(tmpFilePath);
            userFile.transferTo(Files.createFile(tmpFile));
            BookParser bookParser = null;
                bookParser = new BookParser(book, tmpFilePath);
            if (bookParser != null) {
                bookParser.loadBookMetaFromXml();
                bookParser.loadBookContentAndPageListFromXml();
                String url = uploadFileDir + "/" + tmpFileName;
                book.setUrl(url);
                book.setUser(user);
                bookRepo.save(book);
                Files.copy(tmpFile, Paths.get(url));
                Files.delete(tmpFile);
                currentBookMap.put(user.getUsername(), book);
            }
            result = true;
        }
        return result;
    }

    public boolean deleteBook(Long id, String username) throws IOException, CustomException {
        boolean result = false;
        Book book = bookRepo.findById(id).orElseThrow(new BookNotFoundException("Ошибка при удалении книги id: " + id));
        if (book != null) {
            if (currentBookMap.get(username) != null) {
                currentBookMap.remove(username);
            }
            String url = book.getUrl();
            Files.delete(Paths.get(url));
            bookRepo.deleteById(id);
            result = true;
        }
        return result;
    }

    public Book getBookById(Long id) throws CustomException {
        return bookRepo.findById(id).orElseThrow(new BookNotFoundException("Ошибка при поиске книги id: " + id));
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        for (Book book : bookRepo.findAll()) {
            bookList.add(book);
        }
        return bookList;
    }

//    public List<Book> getAllBooksByUsername (String username) {
//        User user = userRepo.findByUsername(username);
//        return bookRepo.findByUser(user);
//    }

    public Book getCurrentBook(String username) {
        return currentBookMap.get(username);
    }

    public void setCurrentBook(String username, Book currentBook) {
        currentBookMap.put(username, currentBook);
    }


}
