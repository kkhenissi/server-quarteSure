package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@ActiveProfiles("test")
@DataJpaTest
class UserDaoTest {
    @Autowired
    private  UserDao userDao;
    @BeforeEach
    public void setup() {
        userDao.save(User.builder()
                        .email("moimeme@yahoo.fr")
                        .firstName("moimeme")
                        .lastName("khenissi")
                        .phoneNumber("1452145212")
                .build());
        userDao.save(User.builder()
                .email("toimeme@yahoo.fr")
                .firstName("toimeme")
                .lastName("khenissi")
                .phoneNumber("745862562")
                .build());

    }
@Test
    public void shouldByEmail(){
        String givenEmail = "moimeme@yahoo.fr";
       User findedUser = userDao.findByEmail(givenEmail);
    assertThat(findedUser).isInstanceOf(User.class);

    }
}