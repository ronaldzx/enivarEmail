package com.mail.sender.service;

import com.mail.sender.dto.MailDTO;

import javax.mail.MessagingException;
import java.io.IOException;

public interface MailSenderService {
    void send (MailDTO mailDTO);

    void sendWithAttach(MailDTO mailDTO) throws MessagingException, IOException;
}
