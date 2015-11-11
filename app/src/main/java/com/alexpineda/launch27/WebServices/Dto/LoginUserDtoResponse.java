package com.alexpineda.launch27.WebServices.Dto;

/**
 * Created by alexpineda77 on 2015-11-03.
 */
public class LoginUserDtoResponse {
    public LoginUserDtoResponse(int id, String email, String single_access_token, String first_name, String last_name){
        this.id = id;
        this.email = email;
        this.single_access_token = single_access_token;
        this.first_name = first_name;
        this.last_name = last_name;
    }
    public int id;
    public String email;
    public String single_access_token;
    public String first_name;
    public String last_name;
}

/*
{"user":{
  "id":2,
  "email":"user@email.com",
  "single_access_token":"U3BMqhPkzzF0unZd1Qay",
  "first_name":"First",
  "last_name":"Last"
}}

 */
