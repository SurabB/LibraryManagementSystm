package com.library.proj.controller;

import com.library.proj.repo.LibraryDaoImpl;
import com.library.proj.service.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/user")
@PreAuthorize("hasRole('User')")
public class UserController {
    @Autowired
    private LibraryDaoImpl libraryDao;
@GetMapping("/displayBooks")
 public List<Book> displayAvailableBook(){
    List<Book> books = libraryDao.viewBooksByRentalStatus(true);
    return books;
}
    @PostMapping("/rentBook")
    public String rentBook(@RequestBody Book book){
        boolean b = libraryDao.rentBook(book.getBookName(),
                book.getAuthorName(),
                book.getUserId());
        if(b){
            return "book rented successfully";
        }
        return  "book rental failed";

    }
    @PostMapping("/returnBook")
    public String returnBook(@RequestBody Book book){
        boolean b = libraryDao.returnBook(book.getBookName(),
                book.getAuthorName(),
                book.getUserId());
        if(b){
            return "book returned successfully";
        }
        return  "book returning process failed";

    }

}
