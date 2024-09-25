package com.Peaqock.CognitoApp.Contollers;

import com.Peaqock.CognitoApp.Dtos.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class privateController {

    @GetMapping("/private")
    public ResponseEntity<MessageDto> privateMessage(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(new MessageDto("Hello " + jwt.getClaim("name")));
    }
}
