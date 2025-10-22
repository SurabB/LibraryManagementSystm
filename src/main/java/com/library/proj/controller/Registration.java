package com.library.proj.controller;
import com.library.proj.service.MyService;
import com.library.proj.service.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class Registration {
    @Autowired
    MyService service;
    @PostMapping("/uReg")
   public String userRegistration(@RequestBody User user){
        boolean register =  service.register(user);
        if(register){
            return "registration success";

        }
        return "registration failed";

    }
    @PostMapping("/liReg")
    public ResponseEntity<Boolean> librarianRegistration(@RequestBody User user){
        boolean register =  service.register(user);
        if(register){
          return new ResponseEntity<Boolean>(register, HttpStatus.OK);

        }
           return new ResponseEntity<Boolean>(!register, HttpStatus.INTERNAL_SERVER_ERROR);


    }
}
