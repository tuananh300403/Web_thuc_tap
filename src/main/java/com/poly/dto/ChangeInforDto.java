package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeInforDto {

    private String name;

    private String password;

    private String avatar;

    private String roles;

    private String phone;

    private String email;

    private String address;

    private Date birthday;

    private boolean gender;

}
