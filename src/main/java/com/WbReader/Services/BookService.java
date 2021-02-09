package com.WbReader.Services;

import com.WbReader.Controller.BookNotFoundException;
import com.WbReader.Data.Book;
import com.WbReader.Data.BookRepo;
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

@Service
public class BookService {

    Book currentBook;

    @Autowired
    BookRepo bookRepo;

    @Autowired
    Logger LOGGER;

    @Value("${app.filepath}")
    String uploadFileDir;

    @PostConstruct
    void checkUploadDir() throws IOException {
        Path path = Paths.get(uploadFileDir + "/tmp");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Files.walkFileTree(path, new UploadDirFileVisitor());


    }

    public boolean addBook(MultipartFile userFile) throws IOException, XmlException {
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
                bookRepo.save(book);
                Files.copy(tmpFile, Paths.get(url));
                Files.delete(tmpFile);
                currentBook = book;
            }
            result = true;
        }
        return result;
    }

    public boolean deleteBook(Long id) throws IOException, BookNotFoundException {
        boolean result = false;
        Book book = bookRepo.findById(id).orElseThrow(new BookNotFoundException("Ошибка при удалении книги id: " + id));
        if (book != null) {
            String url = book.getUrl();
            Files.delete(Paths.get(url));
            bookRepo.deleteById(id);
            result = true;
        }
        return result;
    }

    public Book getBookById(Long id) throws BookNotFoundException {
        return bookRepo.findById(id).orElseThrow(new BookNotFoundException("Ошибка при поиске книги id: " + id));
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        for (Book book : bookRepo.findAll()) {
            bookList.add(book);
        }
        return bookList;
    }

    public Book getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(Book currentBook) {
        this.currentBook = currentBook;
    }


}
