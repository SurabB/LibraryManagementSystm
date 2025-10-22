package com.library.proj.controller;

import com.library.proj.repo.LibraryDaoImpl;
import com.library.proj.service.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/librarian")
@PreAuthorize("hasRole('Librarian')")
public class LibrarianController {
    @Autowired
    private LibraryDaoImpl libraryDao;
@PostMapping("/addBook")
   public String addBook(@RequestBody Book book){
        Book book1 = libraryDao.addBook(book);
        if(book1!=null){
            return "book added successfully";
        }
        return "book failed to add";

    }
    @PostMapping("/removeBook/{bookId}")
    public String removeBook(@PathVariable int bookId){
        boolean res = libraryDao.removeBook(bookId);
        if(res){
            return "book removed successfully";
        }
        return "book failed to remove";

    }
    @GetMapping("displayAvailableBooks")
    public List<Book> displayAvailableBooks(){
        List<Book> books = libraryDao.viewBooksByRentalStatus(true);
        return books;
    }
    @GetMapping("displayRentedBooks")
    public List<Book> displayRentedBooks(){
        List<Book> books = libraryDao.viewBooksByRentalStatus(false);
        return books;
    }
}
