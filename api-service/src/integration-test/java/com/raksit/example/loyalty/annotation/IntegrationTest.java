package com.raksit.example.loyalty.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.raksit.example.loyalty.extension.AmazonS3ClientExtension;
import com.raksit.example.loyalty.extension.PostgresExtension;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ExtendWith({AmazonS3ClientExtension.class, PostgresExtension.class})
@Retention(RUNTIME)
@Target(TYPE)
public @interface IntegrationTest {

}
