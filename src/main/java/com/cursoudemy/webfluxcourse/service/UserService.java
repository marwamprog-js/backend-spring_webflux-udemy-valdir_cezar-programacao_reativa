package com.cursoudemy.webfluxcourse.service;

import com.cursoudemy.webfluxcourse.entity.User;
import com.cursoudemy.webfluxcourse.mapper.UserMapper;
import com.cursoudemy.webfluxcourse.model.request.UserRequest;
import com.cursoudemy.webfluxcourse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public Mono<User> save(final UserRequest request) {
       return userRepository.save(mapper.toEntity(request));
    }

}
