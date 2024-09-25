package com.Peaqock.CognitoApp.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CognitoTokenResponseDto(String id_token) {}
