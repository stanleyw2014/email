package com.hw.email.provider;

import com.hw.email.entity.Draft;
import com.hw.email.entity.Email;
import com.hw.email.entity.User;
import com.hw.email.exception.ObjectNotFoundException;
import com.hw.email.util.EmailIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class StorageProvider {
    private static Logger logger = LoggerFactory.getLogger(StorageProvider.class);
    private static User user1Account = new User("user1@example.com", "123");
    private static User user2Account = new User("user2@example.com", "456");
    private static User user3Account = new User("user3@example.com", "789");

    private static Map<String, Map<Integer, Email>> emailMap = new HashMap<>();
    private static Map<String, Map<Integer, Draft>> draftMap = new HashMap<>();

    private static Map<String, User> userMap = new HashMap<>();


    static {
        logger.info("init storage");
        userMap.put(user1Account.getUserId(), user1Account);
        userMap.put(user2Account.getUserId(), user2Account);
        userMap.put(user3Account.getUserId(), user3Account);
        List<Email> emailList1 = new ArrayList<>();
        List<Email> emailList2 = new ArrayList<>();
        List<Email> emailList3 = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            switch (i % 3) {
                case 0 ->
                        emailList1.add(new Email(EmailIdGenerator.getEmailId(), "title " + i, Collections.singletonList(user1Account.getUserId()), user2Account.getUserId(), "email content " + i, System.currentTimeMillis(), System.currentTimeMillis()));
                case 1 ->
                        emailList2.add(new Email(EmailIdGenerator.getEmailId(), "title " + i, Collections.singletonList(user2Account.getUserId()), user3Account.getUserId(), "email content " + i, System.currentTimeMillis(), System.currentTimeMillis()));
                case 2 ->
                        emailList3.add(new Email(EmailIdGenerator.getEmailId(), "title " + i, Collections.singletonList(user3Account.getUserId()), user1Account.getUserId(), "email content " + i, System.currentTimeMillis(), System.currentTimeMillis()));
            }
        }
        Map<Integer, Email> userEmailMap1 = new HashMap<>();
        Map<Integer, Email> userEmailMap2 = new HashMap<>();
        Map<Integer, Email> userEmailMap3 = new HashMap<>();
        for (int i = 0; i < emailList1.size(); i++) {
            userEmailMap1.put(emailList1.get(i).getId(), emailList1.get(i));
            userEmailMap2.put(emailList2.get(i).getId(), emailList2.get(i));
            userEmailMap3.put(emailList3.get(i).getId(), emailList3.get(i));
        }
        emailMap.put(user1Account.getUserId(), userEmailMap1);
        emailMap.put(user2Account.getUserId(), userEmailMap2);
        emailMap.put(user3Account.getUserId(), userEmailMap3);

        List<Draft> draftList1 = new ArrayList<>();
        List<Draft> draftList2 = new ArrayList<>();
        List<Draft> draftList3 = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            switch (i % 3) {
                case 0 ->
                        draftList1.add(new Draft(getNextDraftIdByUser(user1Account.getUserId()), "title " + i, Collections.singletonList(user2Account.getUserId()), user1Account.getUserId(), "draft content " + i, System.currentTimeMillis()));
                case 1 ->
                        draftList2.add(new Draft(getNextDraftIdByUser(user2Account.getUserId()), "title " + i, Collections.singletonList(user3Account.getUserId()), user2Account.getUserId(), "draft content " + i, System.currentTimeMillis()));
                case 2 ->
                        draftList3.add(new Draft(getNextDraftIdByUser(user3Account.getUserId()), "title " + i, Collections.singletonList(user1Account.getUserId()), user3Account.getUserId(), "draft content " + i, System.currentTimeMillis()));
            }
        }
        Map<Integer, Draft> userDraftMap1 = new HashMap<>();
        Map<Integer, Draft> userDraftMap2 = new HashMap<>();
        Map<Integer, Draft> userDraftMap3 = new HashMap<>();
        for (int i = 0; i < draftList1.size(); i++) {
            userDraftMap1.put(draftList1.get(i).getId(), draftList1.get(i));
            userDraftMap2.put(draftList2.get(i).getId(), draftList2.get(i));
            userDraftMap3.put(draftList3.get(i).getId(), draftList3.get(i));
        }
        draftMap.put(user1Account.getUserId(), userDraftMap1);
        draftMap.put(user2Account.getUserId(), userDraftMap2);
        draftMap.put(user3Account.getUserId(), userDraftMap3);

    }


    public static void addEmail(String userId, Email email) {
        if (emailMap.containsKey(userId)) {
            emailMap.get(userId).put(email.getId(), email);
        } else {
            logger.warn("recipient not found");
        }
    }


    public static void addDraft(String userId, Draft draft) {
        if (draftMap.containsKey(userId)) {
            draftMap.get(userId).put(draft.getId(), draft);
        } else {
            Map<Integer, Draft> userMap = new HashMap<>();
            userMap.put(draft.getId(), draft);
            draftMap.put(userId, userMap);
        }
    }


    public static Map<Integer, Email> getUserEmailMap(String userId) {
        Map<Integer, Email> userEmailMap = emailMap.get(userId);
        if (null == userEmailMap) {
            throw new ObjectNotFoundException("user not found");
        }
        return userEmailMap;
    }


    public static Email getEmail(String userId, int emailId) {
        Map<Integer, Email> userMap = emailMap.get(userId);
        if (null == userMap) {
            throw new ObjectNotFoundException("user not found");
        }
        Email email = userMap.get(emailId);
        if (null == email) {
            throw new ObjectNotFoundException("email not found");
        }
        return email;
    }


    public static Draft getDraft(String userId, int draftId) {
        Map<Integer, Draft> userMap = draftMap.get(userId);
        if (null == userMap) {
            throw new ObjectNotFoundException("user not found");
        }
        Draft draft = userMap.get(draftId);
        if (null == draft) {
            throw new ObjectNotFoundException("draft not found");
        }
        return draft;
    }


    public static int getNextDraftIdByUser(String userId) {
        User user = userMap.get(userId);
        if (null == user) {
            throw new ObjectNotFoundException("User not found");
        }
        return user.getNextDraftId();
    }


    public static User getUser(String userId) {
        return userMap.get(userId);
    }

}
