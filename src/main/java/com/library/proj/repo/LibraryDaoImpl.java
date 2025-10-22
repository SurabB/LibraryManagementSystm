package com.library.proj.repo;

import com.library.proj.service.Book;
import com.library.proj.service.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Repository
public class LibraryDaoImpl implements LibraryDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    RowMapper<Book> rowMapper=(res,row)->{
        Book book=new Book();
        book.setBookName(res.getString("bookName"));
        book.setBookId(res.getInt("bookId"));
        book.setAuthorName(res.getString("authorName"));
        book.setAvailability(res.getBoolean("availability"));
        book.setRentalPrice(res.getInt("rentalPrice"));
        book.setUserId(res.getInt("userId"));
        return book;

    };
    @Override
    public Book addBook(Book book) {
        String query="insert into Book(bookId,bookName,authorName,availability,rentalPrice) values(?,?,?,?,?)";
        Object[] args={book.getBookId(),book.getBookName(),book.getAuthorName(),book.isAvailability(),book.getRentalPrice()};
        jdbcTemplate.update(query,args);
        return book;
    }

    @Override
    public boolean removeBook(int bookId) {

        String checkQuery = "SELECT availability FROM Book WHERE bookId = ?";
        try {
            Boolean isAvailable = jdbcTemplate.queryForObject(checkQuery, Boolean.class, bookId);

            if (isAvailable == null) {

                throw new RuntimeException("Book with ID " + bookId + " does not exist.");
            }

            if (!isAvailable) {
                throw new RuntimeException("Cannot delete. Book is currently rented.");
            }
            String deleteQuery = "DELETE FROM Book WHERE bookId = ?";
            return jdbcTemplate.update(deleteQuery, bookId)>0;

        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Book with ID " + bookId + " does not exist.");
        }
    }


    @Override
    public List<Book> addBooks(List<Book> books) {
        String query="insert into Book values(?,?,?,?,?)";
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1,books.get(i).getBookId());
                ps.setInt(2,books.get(i).getRentalPrice());
                ps.setString(3,books.get(i).getBookName());
                ps.setString(4,books.get(i).getAuthorName());
                ps.setBoolean(5,books.get(i).isAvailability());

            }

            @Override
            public int getBatchSize() {
                return books.size();
            }
        });
        return books;
    }

    @Override
    public int removeBooks(List<Integer> ids) {
        String query="delete from Book where bookId=?";
        return jdbcTemplate.update(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1,ids.get(i));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });

    }

    @Override
    public List<Book> getAllBooks() {
        String query="select *from Book";
        List<Book> books = jdbcTemplate.query(query, rowMapper);
        return books;


    }

    @Override
    public Book getBookById(int id) {
        String query="select *from Book where bookId=?";
        Book book = jdbcTemplate.queryForObject(query,new Object[]{id}, rowMapper);
        return book;
    }

    @Override
    public List<Book> getBooksByName(String name) {
        String query="select *from Book where bookName=?";
        List<Book> books = jdbcTemplate.query(query, rowMapper);
        return books;

    }

    @Override
    public List<Book> getBookByAuthorName(String name) {
        String query="select *from Book where authorName=?";
        List<Book> books = jdbcTemplate.query(query, rowMapper,name);
        return books;

    }

    @Override
    public boolean updateBookById(Book book) {
        String query="Update Book set bookName=?,authorName=?,availability=?,rentalPrice=?,userId=? where id=?";
        Object[] args={book.getBookName(),book.getAuthorName(),book.isAvailability(),book.getRentalPrice(),book.getUserId(),book.getBookId()};
       return  jdbcTemplate.update(query, args)>0;


    }

    @Override
    public int updateAvailabilityStatusById(int id, boolean availability) {
        String query="Update Book set availability=? where bookId=?";
        return jdbcTemplate.update(query, availability,id);
    }

    @Override
    public User findUserById(String id) {
        int ide = Integer.parseInt(id);
        String query="select *from User where userId=?";
        User user1 = jdbcTemplate.queryForObject(query, new Object[]{id}, (res, row) -> {
            User user = new User();
            user.setUserId(ide);
            user.setPassword(res.getString("password"));
            user.setRole(res.getString("role"));

            return user;

        });
        return  user1;
    }

    @Override
    public int updateRentalPrice(int bookId,int price) {
       String query="update Book set rentalPrice=? where bookId=?";
       return jdbcTemplate.update(query,price,bookId);
    }



    @Override
    public List<Book> viewBooksByRentalStatus(boolean availability) {
        String query="select *from Book where availability=?";
        List<Book> books = jdbcTemplate.query(query, rowMapper, availability);
        return books;
    }

    @Override
    public boolean rentBook(String bookName, String authorName,int userId) {

        String query1="Update User set bookNo=bookNo+1 where userId=?";
        String query2 = "SELECT * FROM Book WHERE bookName=? AND authorName=? AND availability=? LIMIT 1";
        Object[] args = {bookName, authorName, true};
        try {
            Book book = jdbcTemplate.queryForObject(query2, rowMapper, args);
            book.setUserId(userId);
            book.setAvailability(false);
            boolean con1 = updateBookById(book);

            boolean con2 = jdbcTemplate.update(query1, userId)>0;
            return con1&&con2;
        }
        catch(Exception e){
            return false;
        }



    }

    @Override
    public boolean returnBook(String bookName, String authorName, int userId) {
        String query="update Book set userId=?,availability=? where bookName=? and authorName=? and userId=?";
        String query1="Update User set bookNo=bookNo-1 where userId=?";

        Object[] args={null,true,bookName,authorName,userId};

        boolean con1 = jdbcTemplate.update(query, args) > 0;
        boolean con2 = jdbcTemplate.update(query1, userId)>0;
        return con1&&con2;

    }
    public boolean register(User user){
        String query="Insert into User(userId,password,role,bookNo) values(?,?,?,?)";


        Object[] args={user.getUserId(),user.getPassword(),user.getRole(),user.getBookNo()};
      return  jdbcTemplate.update(query,args)>0;

    }


}
