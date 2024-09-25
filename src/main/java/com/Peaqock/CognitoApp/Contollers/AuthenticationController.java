package com.Peaqock.CognitoApp.Contollers;

import com.Peaqock.CognitoApp.Dtos.CognitoTokenResponseDto;
import com.Peaqock.CognitoApp.Dtos.TokenDto;
import com.Peaqock.CognitoApp.Dtos.UrlDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@RestController
public class AuthenticationController {

    @Value("${auth.cognitoUri}")
    private String cognitoUri;

    @Value("${sprung.security.oauth2.resourceserver.jwt.clientId}")
    private String clientId;

    @Value("${sprung.security.oauth2.resourceserver.jwt.clientSecret}")
    private String clientSecret;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @GetMapping("/auth/url")
    public ResponseEntity<UrlDto> url(){
        String url = cognitoUri +
                "/oauth2/authorize?"+
                "response_type=code"+
                "&client_id"+clientId+
                "&redirect_uri:http://localhost:4200/oauth2/idresponse"+
                "&scope=email+openid+profile";
        return ResponseEntity.ok(new UrlDto(url));
    }

    //Validate code with cognito

    @GetMapping("/auth/callback")
    public ResponseEntity<TokenDto> callback(@RequestParam("code") String code) throws IOException, InterruptedException {
        String urlStr = cognitoUri + "/oauth2/token?"+
                "grant_type=authorization_code"+
                "client_id"+clientId+
                "&code"+code+
                "&redirect_uri:http://localhost:4200/oauth2/idresponse";
        String authenticateInfo = clientId+ ":" +clientSecret;
        //Now we should encode the authentication information in base 64;
        String basicAuthentication = Base64.getEncoder().encodeToString(authenticateInfo.getBytes());
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder(new URI(urlStr))
                    .header("Content-type","application/x-www-form-encoded")
                    .header("Authorization","basic " + basicAuthentication)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200){
            throw new RuntimeException("Authentication failed");
        }

        CognitoTokenResponseDto token = MAPPER.readValue(response.body(), CognitoTokenResponseDto.class);
        return ResponseEntity.ok(new TokenDto(token.id_token()));
    }
}
