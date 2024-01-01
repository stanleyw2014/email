package com.hw.email.service;

import com.hw.email.entity.Draft;
import com.hw.email.entity.Email;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmailServiceTest {
    String user1AccountName = "user1@example.com";
    String user2AccountName = "user2@example.com";

    String user3AccountName = "user3@example.com";
    @Autowired
    EmailService emailService;

    @Test
    void testGetEmail() {
        Email email = emailService.getEmail(user1AccountName, 1);
        assertEquals("title 0", email.getTitle());
        assertEquals(user1AccountName, email.getRecipientList().get(0));
        assertEquals(user2AccountName, email.getSender());
        assertEquals("email content 0", email.getContent());
    }
    @Test
    void testGetInboxByUser() {
        List<Email> emailList = emailService.getInboxByUser(user1AccountName);
        assertEquals(5, emailList.size());
        assertEquals(user1AccountName, emailList.get(0).getRecipientList().get(0));
    }

    @Test
    void testCreateDraft() {
        Draft draft = new Draft(0, "title test", Collections.singletonList(user2AccountName), user1AccountName, "content test", System.currentTimeMillis());
        int draftId = emailService.createDraft(user1AccountName, draft);
        Draft draftCreated = emailService.getDraft(user1AccountName, draftId);
        assertEquals(draft.getTitle(), draftCreated.getTitle());
        assertEquals(draft.getContent(), draftCreated.getContent());
    }

    @Test
    void testSendEmail() {
        Email email = new Email(0, "email title test", Collections.singletonList(user2AccountName), user3AccountName, "email content test", System.currentTimeMillis(), System.currentTimeMillis());
        int emailId = emailService.sendEmail(user3AccountName, email);
        Email emailReceived = emailService.getEmail(user2AccountName, emailId);
        assertEquals(email.getTitle(), emailReceived.getTitle());
        assertEquals(email.getContent(), emailReceived.getContent());
    }

}