package com.library.proj.service;

import com.library.proj.repo.LibraryDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyService {
    @Autowired
   private PasswordEncoder encode;
    @Autowired
    private LibraryDaoImpl libraryDao;
    public boolean register(User user){
        user.setPassword(encode.encode(user.getPassword()));
        boolean register = libraryDao.register(user);
        return  register;
    }

}
