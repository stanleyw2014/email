package com.hw.email;

import com.hw.email.entity.Draft;
import com.hw.email.entity.Email;
import com.hw.email.entity.User;
import com.hw.email.provider.StorageProvider;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApiFunctionalTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;



    private String user1Account = "user1@example.com";

    private String user2Account = "user2@example.com";

    private String user3Account = "user3@example.com";

    @Test
    @Order(1)
    public void testGetToken() throws InterruptedException {

        String token = getToken();
        assertNotNull(token);
    }

    private String getToken() {
        User user = new User(user1Account, "123");
        String token = this.restTemplate.postForObject("http://localhost:" + port + "/login", user, String.class);
        return token;
    }
    @Test
    @Order(2)
    public void testGetInbox() {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ParameterizedTypeReference<List<Email>> reference = new ParameterizedTypeReference<List<Email>>() {};
        ResponseEntity<List<Email>> responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/email/inbox/" + user1Account, HttpMethod.GET, entity, reference);
        List<Email> emailList = responseEntity.getBody();
        Email email = emailList.get(0);
        assertEquals("title 0", email.getTitle());
        assertEquals(user1Account, email.getRecipientList().get(0));
        assertEquals("email content 0", email.getContent());
    }

    @Test
    @Order(3)
    public void testGetInboxNoToken() {
        ResponseEntity<String> responseEntity =  this.restTemplate.getForEntity("http://localhost:" + port + "/email/inbox/" + user1Account, String.class);
        assertEquals(HttpStatusCode.valueOf(401), responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    public void testGetEmail () {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Email> responseEntity =  this.restTemplate.exchange("http://localhost:" + port + "/email/" + user1Account + "/1", HttpMethod.GET, entity, Email.class);
        Email email = responseEntity.getBody();
        assertEquals("title 0", email.getTitle());
        assertEquals(user1Account, email.getRecipientList().get(0));
        assertEquals("email content 0", email.getContent());

    }

    @Test
    @Order(5)
    public void testGetDraft() {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Email> responseEntity =  this.restTemplate.exchange("http://localhost:" + port + "/draft/" + user1Account + "/1", HttpMethod.GET, entity, Email.class);
        Email email = responseEntity.getBody();
        assertEquals("title 0", email.getTitle());
        assertEquals(user2Account, email.getRecipientList().get(0));
        assertEquals("draft content 0", email.getContent());
    }

    @Test
    @Order(6)
    public void testCreateDraft() {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        Draft draft = new Draft(0 , "draft test title", Collections.singletonList(user2Account), user1Account, "draft test content", System.currentTimeMillis());
        HttpEntity<Draft> entity = new HttpEntity<>(draft, headers);
        ResponseEntity<Integer> responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/draft/" + user1Account, HttpMethod.POST, entity, Integer.class);
        Draft savedDraft = StorageProvider.getDraft(user1Account, responseEntity.getBody());
        assertEquals(draft.getTitle(), savedDraft.getTitle());
        assertEquals(draft.getContent(), savedDraft.getContent());
        assertEquals(draft.getRecipientList().get(0), savedDraft.getRecipientList().get(0));
    }

    @Test
    @Order(7)
    public void testUpdateDraft() {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        Draft draft = new Draft(0 , "update draft test title", Collections.singletonList(user3Account), user1Account, "update draft test content", System.currentTimeMillis());
        HttpEntity<Draft> entity = new HttpEntity<>(draft, headers);
        ResponseEntity<Integer> responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/draft/" + user1Account + "/1", HttpMethod.PUT, entity, Integer.class);
        Draft updatdDraft = StorageProvider.getDraft(user1Account, responseEntity.getBody());
        assertEquals(draft.getTitle(), updatdDraft.getTitle());
        assertEquals(draft.getContent(), updatdDraft.getContent());
        assertEquals(draft.getRecipientList().get(0), updatdDraft.getRecipientList().get(0));
    }
    @Test
    @Order(8)
    public void testSendEmailFromDraft() {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ParameterizedTypeReference<List<Integer>> reference = new ParameterizedTypeReference<List<Integer>>() {};
        ResponseEntity<List<Integer>> responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/email/delivery/" + user1Account + "/2", HttpMethod.POST, entity, reference);
        Email sentEmail = StorageProvider.getEmail(user2Account, responseEntity.getBody().get(0));
        assertEquals("title 3", sentEmail.getTitle());
        assertEquals(user1Account, sentEmail.getSender());
        assertEquals(user2Account, sentEmail.getRecipientList().get(0));
        assertEquals("draft content 3", sentEmail.getContent());
    }

    @Test
    @Order(9)
    public void testSendEmail() {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        Email email = new Email(0 , "test send email title",  Collections.singletonList(user3Account), user1Account, "test send email content", System.currentTimeMillis(), System.currentTimeMillis());
        HttpEntity<Email> entity = new HttpEntity<>(email, headers);
        ResponseEntity<Integer> responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/email/delivery/" + user1Account, HttpMethod.POST, entity, Integer.class);
        Email sentEmail = StorageProvider.getEmail(user3Account, responseEntity.getBody());
        assertEquals(email.getTitle(), sentEmail.getTitle());
        assertEquals(email.getContent(), sentEmail.getContent());
        assertEquals(email.getRecipientList().get(0), sentEmail.getRecipientList().get(0));
        assertEquals(email.getSender(), sentEmail.getSender());
    }
}

