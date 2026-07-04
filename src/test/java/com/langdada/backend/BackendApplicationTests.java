package com.langdada.backend;

import com.langdada.backend.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BackendApplicationTests {

    @Resource
    private IUserService userService;

    @Test
    void contextLoads() {
        String admin = userService.getEncryptPassword("admin");
        String password = userService.getEncryptPassword("1234");
        System.out.println("密码：");
        System.out.println(admin);
        System.out.println(password);
    }

}
