package com.sakthi.auth.model.signup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class Address {
    private String addresslane1;
    private String addresslane2;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}
