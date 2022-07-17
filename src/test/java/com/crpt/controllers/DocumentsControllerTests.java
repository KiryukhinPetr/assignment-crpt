package com.crpt.controllers;

import com.crpt.model.Document;
import com.crpt.model.Product;
import com.crpt.utils.JsonResourceObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DocumentsController.class)
class DocumentsControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private JsonResourceObjectMapper<Document> documentDataLoader;
    private JsonResourceObjectMapper<ResponseExceptionHandler.ErrorResult> errorResultDataLoader;

    private static final String DOCUMENTS_VALIDATION_URL = "/documents/validation";
    private static final String PRODUCT_NAME = "milk";
    private static final String VALID_PRODUCT_CODE = RandomStringUtils.random(13);

    private static final String ERROR_MESSAGE = "should be a string with %s symbols";

    private static final String VALID_SELLER = RandomStringUtils.random(9);
    private static final String VALID_CUSTOMER = RandomStringUtils.random(9);

    @BeforeEach
    public void setup() {
        documentDataLoader = new JsonResourceObjectMapper<>(new TypeToken<Document>() {
        });
        errorResultDataLoader = new JsonResourceObjectMapper<>(new TypeToken<ResponseExceptionHandler.ErrorResult>() {
        });
    }

    @Test
    void whenValidInput_thenReturns200() throws Exception {
        Product validProduct = createProduct(PRODUCT_NAME, VALID_PRODUCT_CODE);
        Document validDocument = createDocument(VALID_SELLER, VALID_CUSTOMER, Arrays.asList(validProduct));
        mvc.perform(post(DOCUMENTS_VALIDATION_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(validDocument))).andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("testDataForValidationTests")
    void whenInvalidValues_thenReturns400AndErrorResult(String requestDataPath, String expectedErrorResultDataPath) throws Exception {
        Document document = documentDataLoader.loadTestJson(requestDataPath);
        ResponseExceptionHandler.ErrorResult expectedErrorResult  = errorResultDataLoader.loadTestJson(expectedErrorResultDataPath);

        MvcResult mvcResult = mvc.perform(post(DOCUMENTS_VALIDATION_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(document))).andExpect(status().isBadRequest()).andReturn();

        String actualResponseBody =
                mvcResult.getResponse().getContentAsString();
        String expectedResponseBody =
                objectMapper.writeValueAsString(expectedErrorResult);
        assertThat(actualResponseBody)
                .isEqualToIgnoringWhitespace(expectedResponseBody);
    }


    private Document createDocument(String seller, String customer, List<Product> products) {
        Document document = new Document();
        document.setCustomer(customer);
        document.setSeller(seller);
        document.setProducts(products);
        return document;
    }

    private Product createProduct(String name, String code) {
        Product product = new Product();
        product.setName(name);
        product.setCode(code);
        return product;
    }

    private static Stream<Arguments> testDataForValidationTests() {
        return Stream.of(
                Arguments.of("test-data/seller_greater_than_nine_symbols/request.json", "test-data/seller_greater_than_nine_symbols/response.json"),
                Arguments.of("test-data/seller_less_than_nine_symbols/request.json", "test-data/seller_less_than_nine_symbols/response.json"),
                Arguments.of("test-data/customer_less_than_nine_symbols/request.json", "test-data/customer_less_than_nine_symbols/response.json"),
                Arguments.of("test-data/customer_greater_than_nine_symbols/request.json", "test-data/customer_greater_than_nine_symbols/response.json"),
                Arguments.of("test-data/product_code_greater_than_thirteen_symbols/request.json", "test-data/product_code_greater_than_thirteen_symbols/response.json"),
                Arguments.of("test-data/product_code_less_than_thirteen_symbols/request.json", "test-data/product_code_less_than_thirteen_symbols/response.json"),
                Arguments.of("test-data/products_is_empty/request.json", "test-data/products_is_empty/response.json"),
                Arguments.of("test-data/products_is_missed/request.json", "test-data/products_is_missed/response.json")
        );
    }
}
