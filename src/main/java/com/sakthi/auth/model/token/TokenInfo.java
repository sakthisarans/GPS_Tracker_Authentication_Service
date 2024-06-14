package com.sakthi.auth.model.token;

import com.sakthi.auth.model.signin.LoginDevice;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter@Setter@Builder@Document(collection = "userToken")
public class TokenInfo {
    @Id
    private String id;
    private String message;
    private boolean isUserCreated;
    private String userCollectionId;
    private String userEmail;
    private String Token;
    private Date dateOfCreation;
    private boolean isActive;
    private Date dateOfDeactivation;
    private LoginDevice loginDevice;
}
