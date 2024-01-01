package com.hw.email.controller;

import com.hw.email.entity.Draft;
import com.hw.email.entity.Email;
import com.hw.email.exception.UnauthorizedException;
import com.hw.email.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Email", description = "Email management APIs")
@RestController
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(
            summary = "Retrieve emails from inbox by user id",
            description = "Get a email list of inbox by user id. The response is a list of email."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Email.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/email/inbox/{userId}")
    public ResponseEntity<List<Email>> getInbox(@RequestAttribute("userId") String loginUser, @PathVariable String userId) {
        verifyUser(loginUser, userId);
        List<Email> emailList = emailService.getInboxByUser(userId);
        return ResponseEntity.ok(emailList);
    }

    @Operation(
            summary = "Retrieve an email by user id and email id",
            description = "Get an email object by specifying its id. The response is an email object with title, content, recipient, sender."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Email.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/email/{userId}/{emailId}")
    public ResponseEntity<Email> getEmail(@RequestAttribute("userId") String loginUser, @PathVariable String userId, @PathVariable int emailId) {
        verifyUser(loginUser, userId);
        Email email = emailService.getEmail(userId, emailId);
        return ResponseEntity.ok(email);
    }

    @Operation(
            summary = "Retrieve a draft by user id and email id",
            description = "Get a draft object by specifying its id. The response is a draft object with title, content, recipient, sender."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Draft.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/draft/{userId}/{draftId}")
    public ResponseEntity<Draft> getDraft(@RequestAttribute("userId") String loginUser, @PathVariable String userId, @PathVariable int draftId) {
        verifyUser(loginUser, userId);
        Draft draft = emailService.getDraft(userId, draftId);
        return ResponseEntity.ok(draft);
    }

    @Operation(
            summary = "Create a draft",
            description = "Create a draft object by specifying its title, content, recipient, sender."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Integer.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping("/draft/{userId}")
    public ResponseEntity<Integer> createDraft(@RequestAttribute("userId") String loginUser, @PathVariable String userId, @RequestBody Draft draft) {
        verifyUser(loginUser, userId);
        int draftId = emailService.createDraft(userId, draft);
        return ResponseEntity.ok(draftId);
    }

    @Operation(
            summary = "Update a draft",
            description = "Update a draft object by specifying its title, content, recipient."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Integer.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PutMapping("/draft/{userId}/{draftId}")
    public ResponseEntity<Integer> updateDraft(@RequestAttribute("userId") String loginUser, @PathVariable String userId, @PathVariable int draftId, @RequestBody Draft draft) {
        verifyUser(loginUser, userId);
        int updatedDraftId = emailService.updateDraft(userId, draftId, draft);
        return ResponseEntity.ok(updatedDraftId);
    }

    @Operation(
            summary = "Send an email from draft box",
            description = "Send an email from a draft in draft box"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Integer.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping("/email/delivery/{userId}/{draftId}")
    public ResponseEntity<List<Integer>> sendEmail(@RequestAttribute("userId") String loginUser, @PathVariable String userId, @PathVariable int draftId) {
        verifyUser(loginUser, userId);
        List<Integer> emailIdList = emailService.sendEmail(userId, draftId);
        return ResponseEntity.ok(emailIdList);
    }

    @Operation(
            summary = "Send an email",
            description = "Send an email by specifying its title, content, recipient, sender."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Integer.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping("/email/delivery/{userId}")
    public ResponseEntity<Integer> sendEmail(@RequestAttribute("userId") String loginUser, @PathVariable String userId, @RequestBody Email email) {
        verifyUser(loginUser, userId);
        int emailId = emailService.sendEmail(userId, email);
        return ResponseEntity.ok(emailId);
    }

    private void verifyUser(String loginUser, String userId) {
        if (!userId.equals(loginUser)) {
            throw new UnauthorizedException("Not authorized to access resource");
        }
    }

}
