package com.sakthi.auth.model.signup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sakthi.auth.model.jwt.Role;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "userData")
public class SignupRequest {
    @Id
    private String ID;
    @Pattern(regexp = "^[a-z][a-z0-9_]{0,14}$",message = "invalid user name")
    private String userName;

    private String profilePicture;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!#$%^&?])(?!.*\\s)[a-zA-Z\\d!#$%^&?]{8,}$",message = "password must be valid")
    private String password;
    private Date dateOfCreation;
    @Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",message = "email must contain valid data")
    private String email;
    @Pattern(regexp = "^\\+[0-9]{1,19}$",message = "contact must contain valid data")
    private String contact;
    private AdditionalInfo additionalInfo;
    private Address address;
    @NotEmpty(message = "Trackers must contain data")
    private List<Tracker> trackerList;
    @DBRef
    private Set<Role> roles = new HashSet<>();
    private List<String> roleList;
    private Account accountDetail;
}
