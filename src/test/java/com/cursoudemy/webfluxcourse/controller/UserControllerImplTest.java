package com.cursoudemy.webfluxcourse.controller;

import com.cursoudemy.webfluxcourse.entity.User;
import com.cursoudemy.webfluxcourse.mapper.UserMapper;
import com.cursoudemy.webfluxcourse.model.request.UserRequest;
import com.cursoudemy.webfluxcourse.model.response.UserResponse;
import com.cursoudemy.webfluxcourse.service.UserService;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    public static final String ID = "123456";
    public static final String NAME = "Valdir";
    public static final String EMAIL = "valdir@email.com";
    public static final String PASSWORD = "123";
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper mapper;

    @MockBean
    private MongoClient mongoClient;


    @Test
    @DisplayName("Test endpoint save with success")
    void testSaveWithSuccess() {
        final UserRequest request = new UserRequest(NAME, EMAIL, PASSWORD);

        Mockito.when(userService.save(ArgumentMatchers.any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(userService, Mockito.times(1)).save(ArgumentMatchers.any(UserRequest.class));
    }

    @Test
    @DisplayName("Test endpoint save with bad request")
    void testSaveWithBadRequest() {
        final UserRequest request = new UserRequest(NAME.concat(" "), EMAIL, PASSWORD);

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                .jsonPath("$.errors[0].message").isEqualTo("field cannot have blank spaces at the beginning or at end");

    }

    @Test
    @DisplayName("Test endpoint findById with success")
    void testfindByIdWithSuccess() {

        final UserResponse response = new UserResponse(ID, NAME, EMAIL, PASSWORD);

        Mockito.when(userService.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(User.builder().build()));
        Mockito.when(mapper.toResponse(ArgumentMatchers.any(User.class))).thenReturn(response);

        webTestClient.get().uri("/users/" + ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(ID)
                .jsonPath("$.name").isEqualTo("Valdir")
                .jsonPath("$.email").isEqualTo("valdir@email.com")
                .jsonPath("$.password").isEqualTo("123");

        Mockito.verify(userService).findById(ArgumentMatchers.anyString());
        Mockito.verify(mapper).toResponse(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Test endpoint findByAll with success")
    void testFindAllWithSuccess() {

        final UserResponse response = new UserResponse(ID, NAME, EMAIL, PASSWORD);

        Mockito.when(userService.findAll()).thenReturn(Flux.just(User.builder().build()));
        Mockito.when(mapper.toResponse(ArgumentMatchers.any(User.class))).thenReturn(response);

        webTestClient.get().uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(ID)
                .jsonPath("$.[0].name").isEqualTo("Valdir")
                .jsonPath("$.[0].email").isEqualTo("valdir@email.com")
                .jsonPath("$.[0].password").isEqualTo("123");

        Mockito.verify(userService).findAll();
        Mockito.verify(mapper).toResponse(ArgumentMatchers.any(User.class));

    }

    @Test
    @DisplayName("Test endpoint update with success")
    void update() {
        final UserResponse response = new UserResponse(ID, NAME, EMAIL, PASSWORD);
        final UserRequest request = new UserRequest(NAME, EMAIL, PASSWORD);

        Mockito.when(userService.update(ArgumentMatchers.anyString(), ArgumentMatchers.any(UserRequest.class)))
                .thenReturn(Mono.just(User.builder().build()));
        Mockito.when(mapper.toResponse(ArgumentMatchers.any(User.class))).thenReturn(response);

        webTestClient.patch().uri("/users/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(ID)
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.email").isEqualTo(EMAIL)
                .jsonPath("$.password").isEqualTo(PASSWORD);

        Mockito.verify(userService).update(ArgumentMatchers.anyString(), ArgumentMatchers.any(UserRequest.class));
        Mockito.verify(mapper).toResponse(ArgumentMatchers.any(User.class));


    }

    @Test
    void delete() {
    }
}