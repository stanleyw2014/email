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

/**
 * Email operations.
 */
@Component
public class EmailService {

    private final EmailRepository emailRepository;

    @Autowired
    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }


    Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * get inbox emails for specific user.
     * @param userId user id.
     * @return a list of emails.
     */
    public List<Email> getInboxByUser(String userId) {
        logger.debug("getting ");
        return new ArrayList<>(emailRepository.getUserEmailMap(userId).values());
    }

    /**
     * get single email.
     * @param userId user id.
     * @param emailId email id.
     * @return email object.
     */
    public Email getEmail(String userId, int emailId) {
        return emailRepository.getEmail(userId, emailId);
    }

    /**
     * get single draft.
     * @param userId user id.
     * @param draftId draft id.
     * @return draft object.
     */
    public Draft getDraft(String userId, int draftId) {
        return emailRepository.getDraft(userId, draftId);
    }

    /**
     * create a draft based on provide draft object.
     * @param userId user id.
     * @param draft draft object.
     * @return draft id.
     */
    public int createDraft(String userId, Draft draft) {
        int draftId = emailRepository.getNextDraftIdByUser(userId);
        Draft toCreate = new Draft(draftId, draft.getTitle(), draft.getRecipientList(), userId, draft.getContent(), System.currentTimeMillis());
        emailRepository.addDraft(userId, toCreate);
        return draftId;
    }

    /**
     * update a draft.
     * @param userId user id.
     * @param draftId draft id.
     * @param draft draft object.
     * @return draft id of updated draft.
     */
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

    /**
     * send email(s) based on a draft.
     * @param userId user id.
     * @param draftId draft id.
     * @return email id list of sent emails.
     */
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

    /**
     * send an email base on provide email content.
     * @param userId user id.
     * @param email email object.
     * @return email id of sent email.
     */
    public int sendEmail(String userId, Email email) {
        email.setId(EmailIdGenerator.getEmailId());
        email.setSender(userId);
        for (String r : email.getRecipientList()) {
            emailRepository.addEmail(r, email);
        }
        return email.getId();
    }

    /**
     * create an email object based on a draft.
     * @param draft draft object.
     * @return email object.
     */
    private Email convertDraftToEmail(Draft draft) {
        return new Email(EmailIdGenerator.getEmailId(), draft.getTitle(), draft.getRecipientList(), draft.getSender(), draft.getContent(), draft.getTimeCreated(), System.currentTimeMillis());
    }


}
