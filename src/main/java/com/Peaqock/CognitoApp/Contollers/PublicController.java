package com.Peaqock.CognitoApp.Contollers;


import com.Peaqock.CognitoApp.Dtos.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @GetMapping("/public")
    public ResponseEntity<MessageDto> publicMessage(){
        return ResponseEntity.ok(new MessageDto("How are you ?"));
    }
}
