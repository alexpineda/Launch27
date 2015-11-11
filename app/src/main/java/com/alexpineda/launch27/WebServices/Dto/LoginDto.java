package com.alexpineda.launch27.WebServices.Dto;

/**
 * Created by alexpineda77 on 2015-11-03.
 */
public class LoginDto {
    public LoginDto(String email, String password){
        this.email = email;
        this.password = password;
    }
    public String email;
    public String password;
}