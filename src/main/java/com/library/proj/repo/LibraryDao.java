package com.library.proj.repo;

import com.library.proj.service.Book;
import com.library.proj.service.User;

import java.util.HashMap;
import java.util.List;

public interface LibraryDao {
     Book addBook(Book book);

     boolean removeBook(int bookId);

     List<Book> addBooks(List<Book> books);

     int removeBooks(List<Integer> ids);

     List<Book> getAllBooks();

     Book getBookById(int id);

     List<Book> getBooksByName(String name);

     List<Book> getBookByAuthorName(String name);

     boolean updateBookById(Book book);

     int updateAvailabilityStatusById(int id, boolean availability);

     User findUserById(String id);

     int updateRentalPrice(int bookId, int price);

     List<Book> viewBooksByRentalStatus(boolean availability);

     boolean rentBook(String bookName, String authorName, int userId);

     boolean returnBook(String bookName, String authorName, int userId);

}