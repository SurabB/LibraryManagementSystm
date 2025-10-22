package com.library.proj.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Book {
    int bookId;
    String authorName;
    int rentalPrice;
    boolean availability;
    String bookName;
    int userId;

}
