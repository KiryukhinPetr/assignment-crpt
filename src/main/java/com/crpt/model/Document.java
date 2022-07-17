package com.crpt.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Document {
    @NotNull
    @Length(min = 9, max = 9, message = "should be a string with 9 symbols")
    String seller;
    @NotNull
    @Length(min = 9, max = 9, message = "should be a string with 9 symbols")
    String customer;
    @NotEmpty
    @Valid
    List<Product> products;
}
