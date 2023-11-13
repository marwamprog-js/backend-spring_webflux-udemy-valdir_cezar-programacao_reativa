package com.cursoudemy.webfluxcourse.controller.impl;

import com.cursoudemy.webfluxcourse.controller.UserController;
import com.cursoudemy.webfluxcourse.mapper.UserMapper;
import com.cursoudemy.webfluxcourse.model.request.UserRequest;
import com.cursoudemy.webfluxcourse.model.response.UserResponse;
import com.cursoudemy.webfluxcourse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @Override
    public ResponseEntity<Mono<Void>> save(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.save(request).then());
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> findById(String id) {
        return ResponseEntity.ok().body(
                userService.findById(id).map(mapper::toResponse) //obj -> mapper.toResponse(obj)
        );
    }

    @Override
    public ResponseEntity<Flux<UserResponse>> findAll() {
        return ResponseEntity.ok().body(
                userService.findAll().map(mapper::toResponse)
        );
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> update(String id, UserRequest request) {
        return ResponseEntity.ok().body(
                userService.update(id, request).map(mapper::toResponse)
        );
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        return ResponseEntity.ok().body(
                userService.delete(id).then()
        );
    }
}
