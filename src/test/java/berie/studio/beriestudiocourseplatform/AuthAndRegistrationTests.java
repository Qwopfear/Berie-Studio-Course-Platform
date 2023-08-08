package berie.studio.beriestudiocourseplatform;

import berie.studio.beriestudiocourseplatform.entity.user.Role;
import berie.studio.beriestudiocourseplatform.entity.user.User;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@TestPropertySource(locations= "classpath:application-test.yaml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthAndRegistrationTests {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void shouldCreateNewUserAndGetUserByIdUsingTokenGeneratedAfterRegistration() {

        User userToRegistration = User.builder()
                .name("Diana")
                .email("db@gmail.com")
                .password("123123123")
                .phoneNumber("123-345-678")
                .role(Role.USER)
                .build();


        ResponseEntity<String> createResponse =
                restTemplate.postForEntity(
                        "/api/v1/auth/register",
                        userToRegistration
                        ,String.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI locationOfNewUser = createResponse.getHeaders().getLocation();
        DocumentContext documentContext1 = JsonPath.parse(createResponse.getBody());

        String id = documentContext1.read("$.id");
        String token = documentContext1.read("$.token");

        assertThat(id).isNotBlank();
        assertThat(token).isNotBlank();




        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> getCreatedUser = restTemplate
                .exchange(locationOfNewUser, HttpMethod.GET, entity, String.class);

        assertThat(getCreatedUser.getStatusCode()).isEqualTo(HttpStatus.OK);


        DocumentContext documentContext2 = JsonPath.parse(getCreatedUser.getBody());

        String name = documentContext2.read("$.name");
        assertThat(name).isEqualTo(userToRegistration.getName());

    }


}
