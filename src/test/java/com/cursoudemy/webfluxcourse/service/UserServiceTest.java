package com.cursoudemy.webfluxcourse.service;

import com.cursoudemy.webfluxcourse.entity.User;
import com.cursoudemy.webfluxcourse.mapper.UserMapper;
import com.cursoudemy.webfluxcourse.model.request.UserRequest;
import com.cursoudemy.webfluxcourse.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService userService;

    @Test
    void save() {
        UserRequest request = new UserRequest("Valdir", "valdir@mail.com", "123");
        User entity = User.builder().build();

        Mockito.when(mapper.toEntity(ArgumentMatchers.any(UserRequest.class))).thenReturn(entity);
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = userService.save(request);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class) //user -> user instanceof User || ser -> user != null || Objects::nonNull
                .expectComplete()
                .verify();

        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class)); // Verifica quantas vezes o metodo save foi chamado

    }

    @Test
    void testFindById() {
        Mockito.when(userRepository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(
                User.builder()
                        .id("1234")
                        .build()
        ));

        Mono<User> result = userService.findById("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class) //user -> user instanceof User || ser -> user != null
                .expectComplete()
                .verify();

        Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyString());
    }


    @Test
    void testFindAll() {
        Mockito.when(userRepository.findAll()).thenReturn(Flux.just(User.builder().build()));

        Flux<User> result = userService.findAll();

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class) //user -> user instanceof User || ser -> user != null
                .expectComplete()
                .verify();

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }
}