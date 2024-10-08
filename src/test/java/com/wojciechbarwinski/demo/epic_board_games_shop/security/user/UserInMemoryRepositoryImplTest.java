package com.wojciechbarwinski.demo.epic_board_games_shop.security.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserInMemoryRepositoryImplTest {

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserInMemoryRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserInMemoryRepositoryImpl(encoder);
    }

    @Test
    void shouldContainInitialData() {
        //given
        String username = "owner@mail.com";

        //when
        Optional<UserEntity> userFromDB = userRepository.findByUsername(username);

        //then
        assertTrue(userFromDB.isPresent());
        assertEquals(username, userFromDB.get().getUsername());
    }

    @Test
    void shouldFindUserByUsername() {
        //given
        String username = "owner@mail.com";

        //when
        Optional<UserEntity> userFromDB = userRepository.findByUsername(username);

        //then
        assertTrue(userFromDB.isPresent());
        assertEquals(username, userFromDB.get().getUsername());
    }

    @Test
    void shouldReturnEmptyOptionalBecauseThereIsNotUserWithGivenUsername() {
        //given
        String username = "manager@mail.com";

        //when
        Optional<UserEntity> userFromDB = userRepository.findByUsername(username);

        //then
        assertTrue(userFromDB.isEmpty());
    }
}