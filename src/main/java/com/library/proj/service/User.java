package com.library.proj.service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
   private  int userId;
   private   String password;
   private  String  role;
   private List<Book> booksRented;
   int bookNo;
}
