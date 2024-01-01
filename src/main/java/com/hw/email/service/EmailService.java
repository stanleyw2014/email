package com.hw.email.service;

import com.hw.email.dao.EmailRepository;
import com.hw.email.entity.Draft;
import com.hw.email.entity.Email;
import com.hw.email.util.EmailIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmailService {

    private final EmailRepository emailRepository;

    @Autowired
    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }


    Logger logger = LoggerFactory.getLogger(EmailService.class);

    public List<Email> getInboxByUser(String userId) {
        logger.debug("getting ");
        return new ArrayList<>(emailRepository.getUserEmailMap(userId).values());
    }

    public Email getEmail(String userId, int emailId) {
        return emailRepository.getEmail(userId, emailId);
    }

    public Draft getDraft(String userId, int draftId) {
        return emailRepository.getDraft(userId, draftId);
    }

    public int createDraft(String userId, Draft draft) {
        int draftId = emailRepository.getNextDraftIdByUser(userId);
        Draft toCreate = new Draft(draftId, draft.getTitle(), draft.getRecipientList(), userId, draft.getContent(), System.currentTimeMillis());
        emailRepository.addDraft(userId, toCreate);
        return draftId;
    }

    public int updateDraft(String userId, int draftId, Draft draft) {
        Draft curentDraft = emailRepository.getDraft(userId, draftId);
        if (!draft.getRecipientList().isEmpty()) {
            curentDraft.setRecipientList(draft.getRecipientList());
        }
        if (null != draft.getContent() && !draft.getContent().isEmpty()) {
            curentDraft.setContent(draft.getContent());
        }
        if (null != draft.getTitle() && !draft.getTitle().isEmpty()) {
            curentDraft.setTitle(draft.getTitle());
        }
        return curentDraft.getId();
    }

    public List<Integer> sendEmail(String userId, int draftId) {
        List<Integer> result = new ArrayList<>();
        Draft draft = emailRepository.getDraft(userId, draftId);
        for (String r : draft.getRecipientList()) {
            Email email = convertDraftToEmail(draft);
            emailRepository.addEmail(r, email);
            result.add(email.getId());
        }
        return result;
    }

    public int sendEmail(String userId, Email email) {
        email.setId(EmailIdGenerator.getEmailId());
        email.setSender(userId);
        for (String r : email.getRecipientList()) {
            emailRepository.addEmail(r, email);
        }
        return email.getId();
    }

    private Email convertDraftToEmail(Draft draft) {
        return new Email(EmailIdGenerator.getEmailId(), draft.getTitle(), draft.getRecipientList(), draft.getSender(), draft.getContent(), draft.getTimeCreated(), System.currentTimeMillis());
    }


}
