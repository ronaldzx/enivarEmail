package com.mail.sender.controller;

import com.mail.sender.service.MailSenderService;
import com.mail.sender.dto.MailDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class MailSenderController {

    @Autowired
    private MailSenderService mailSenderService;

    @PostMapping("/email")
    public ResponseEntity<Void> enviarMail(@RequestBody MailDTO mailDTO){
        mailSenderService.send(mailDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation(value = "Enviar Email con archivo adjunto", notes = "Se necesita enviar el archivo en base64")
    @PostMapping("/email/attach")
    public ResponseEntity<Void> enviarMailWithAttach(@RequestBody MailDTO mailDTO) throws MessagingException, IOException {
        mailSenderService.sendWithAttach(mailDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
