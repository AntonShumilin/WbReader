package com.WbReader.Controller;


import com.WbReader.Data.Book;
import com.WbReader.Services.BookService;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    BookService bookService;

    @Autowired
    Logger LOGGER;

    @GetMapping ("/")
    public String catalog (Model model) {
        model.addAttribute("bookList", bookService.getAllBooks());
        LOGGER.debug("Просмотр каталога");
        return "catalog";
    }

    @GetMapping("/chooseBook/{id}")
    public String chooseBook (@PathVariable Long id) throws BookNotFoundException {
        Book book = bookService.getBookById(id);
        bookService.setCurrentBook(book);
        LOGGER.debug("Выбор книги: {}", book.getTitle());
        return "redirect:/page/1";
    }

    @GetMapping("/page/{pageNumber}")
    public String page(@PathVariable int pageNumber, Model model) throws BookNotFoundException, IOException, XmlException {

        Book book = bookService.getCurrentBook();
        if (book != null) {
            List<String> linelist = Arrays.asList(book.getPage(pageNumber - 1).split("\\n"));
            model.addAttribute("lines", linelist);
            model.addAttribute("pageNumber", pageNumber);
            model.addAttribute("pageListSize", book.getPageList().size());
            model.addAttribute("title", book.getTitle());
            List<String> authorslist = Arrays.asList(book.getAuthor().split("\\n"));
            model.addAttribute("authors", authorslist);
            LOGGER.trace("Страница: {} книга id {}", pageNumber, book.getId());
            return "page";
        } else {
            throw new BookNotFoundException("Не выбрана текущая книга");
        }
    }

    @GetMapping("/content")
    public String content (Model model) throws IOException, XmlException, BookNotFoundException {
        Book book = bookService.getCurrentBook();
        if (book != null) {
           model.addAttribute("contentMap", book.getContent());
           LOGGER.debug("Просмотр оглавления. книга id {}", book.getId());
           return "content";
        } else {
            throw new BookNotFoundException("Не выбрана текущая книга");
        }
    }

    @GetMapping("/upload")
    public String uploadPage () {
        return "upload";
    }

    @PostMapping ("/upload")
    public String uploadFile (@RequestParam("file") MultipartFile file, Model model) throws IOException, XmlException {
        bookService.addBook(file);
        Book book = bookService.getCurrentBook();
        if (book != null) {
            LOGGER.info("Загружена книга: {} {}, файл {}", book.getTitle(),
                    book.getAuthor().replaceAll("\\n"," "),
                    Paths.get(book.getUrl()).getFileName().toString());
        }
        return "redirect:/";
    }

    @PostMapping ("/delete/{id}")
    public String deletBook (@PathVariable Long id, Model model) throws IOException, BookNotFoundException {
        bookService.deleteBook(id);
        LOGGER.info("Удалена книга: id: {}", id);
        return "redirect:/";
    }
}
