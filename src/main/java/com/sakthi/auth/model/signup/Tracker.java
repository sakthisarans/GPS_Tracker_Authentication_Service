package com.sakthi.auth.model.signup;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tracker {
    @Pattern(regexp = "^[a-zA-Z0-9]+-[0-9]{1,20}$",message = "Invalid Tracker ID")
    private String trackerID;
    private String VehicleNumber;
}
