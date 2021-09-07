package com.raksit.example.loyalty.annotation;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.flywaydb.test.annotation.FlywayTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@FlywayTest
@AutoConfigureEmbeddedDatabase(provider = DOCKER)
@Retention(RUNTIME)
@Target(TYPE)
public @interface IntegrationTest {

}