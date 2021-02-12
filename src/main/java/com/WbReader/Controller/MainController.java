package com.WbReader.Controller;


import com.WbReader.CustomExeptions.BookNotFoundException;
import com.WbReader.CustomExeptions.CustomException;
import com.WbReader.CustomExeptions.UserNotFoundException;
import com.WbReader.Data.Book;
import com.WbReader.Data.User;
import com.WbReader.Services.BookService;
import com.WbReader.Services.UserService;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
//@Scope("session")
public class MainController {

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @Autowired
    Logger LOGGER;

    @GetMapping ("/account")
    public String catalog (Model model, Principal principal) throws CustomException {
        User user = fromPrincipal(principal);
//        model.addAttribute("bookList", bookService.getAllBooksByUsername(user.getUsername()));
        model.addAttribute("bookList", user.getBooks());
        LOGGER.debug("Просмотр каталога пользователя {}", user.getUsername());
//        userService.deleteUser((long) 2);
        return "account";
    }

    @GetMapping("/chooseBook/{id}")
    public String chooseBook (@PathVariable Long id, Principal principal) throws CustomException {
        User user = fromPrincipal(principal);
        Book book = bookService.getBookById(id);
        bookService.setCurrentBook(user.getUsername(), book);
        LOGGER.debug("Пользователь {} выбрал книгу: {}", user.getUsername(), book.getTitle() );
        return "redirect:/page/1";
    }

    @GetMapping("/page/{pageNumber}")
    public String page(@PathVariable int pageNumber, Model model, Principal principal) throws CustomException, IOException, XmlException {

        User user = fromPrincipal(principal);
        Book book = bookService.getCurrentBook(user.getUsername());
        if (book != null) {
            List<String> linelist = Arrays.asList(book.getPage(pageNumber - 1).split("\\n"));
            model.addAttribute("lines", linelist);
            model.addAttribute("pageNumber", pageNumber);
            model.addAttribute("pageListSize", book.getPageList().size());
            model.addAttribute("title", book.getTitle());
            List<String> authorslist = Arrays.asList(book.getAuthor().split("\\n"));
            model.addAttribute("authors", authorslist);
            LOGGER.trace("Пользователь {} страница: {} книга id {}", user.getUsername(), pageNumber, book.getId());
            return "page";
        } else {
            throw new BookNotFoundException("Не выбрана текущая книга");
        }
    }

    @GetMapping("/content")
    public String content (Model model, Principal principal) throws IOException, XmlException, CustomException {

        User user = fromPrincipal(principal);
        Book book = bookService.getCurrentBook(user.getUsername());
        if (book != null) {
           model.addAttribute("contentMap", book.getContent());
           LOGGER.debug("Пользователь {} просмотр оглавления. книга id {}", user.getUsername(), book.getId());
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
    public String uploadFile (@RequestParam("file") MultipartFile file, Principal principal, Model model) throws IOException, XmlException, CustomException {
        User user = fromPrincipal(principal);
        bookService.addBook(file, user);
        Book book = bookService.getCurrentBook(user.getUsername());
        if (book != null) {
            LOGGER.info("Пользователь {}, загружена книга: {} {}, файл {}", user.getUsername(), book.getTitle(),
                    book.getAuthor().replaceAll("\\n"," "),
                    Paths.get(book.getUrl()).getFileName().toString());
        }
        return "redirect:/account";
    }

//    MULTIFILE UPLOAD
//    @PostMapping ("/upload")
//    public String uploadFile (@RequestParam("files") List<MultipartFile> files, Model model) throws IOException, XmlException {
//
//        System.err.println("upload");
//        if (files == null) {
//            System.err.println("null");
//        }
//
//        for (MultipartFile file : files) {
//            System.err.println("check");
//            bookService.addBook(file);
//        }
//        Book book = bookService.getCurrentBook();
//        if (book != null) {
//            LOGGER.info("Загружена книга: {} {}, файл {}", book.getTitle(),
//                    book.getAuthor().replaceAll("\\n"," "),
//                    Paths.get(book.getUrl()).getFileName().toString());
//        }
//        return "redirect:/account";
//    }

    @PostMapping ("/delete/{id}")
    public String deletBook (@PathVariable Long id, Principal principal) throws IOException, CustomException {
        User user = fromPrincipal(principal);
        bookService.deleteBook(id, user.getUsername());
        LOGGER.info("Пользователь {}, удалена книга: id: {}", user.getUsername(), id);
        return "redirect:/account";
    }

    User fromPrincipal (Principal principal) throws CustomException {
        return userService.findByUserName(principal.getName()).orElseThrow(new UserNotFoundException("User not found"));
    }
}
