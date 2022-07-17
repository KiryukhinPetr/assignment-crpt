package com.crpt.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class Product {
    @NotNull
    String name;
    @NotNull
    @Length(min = 13, max = 13, message = "should be a string with 13 symbols")
    String code;
}
