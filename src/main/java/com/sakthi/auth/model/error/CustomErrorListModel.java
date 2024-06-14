package com.sakthi.auth.model.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Builder@Getter@Setter
public class CustomErrorListModel {
    List<String> errors;
}
