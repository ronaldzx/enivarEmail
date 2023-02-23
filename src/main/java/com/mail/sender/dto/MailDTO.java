package com.mail.sender.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.InputStreamSource;

import java.util.Base64;

@Getter
@Setter
public class MailDTO {
    private String to;
    private String subject;
    private String text;
    private String fileName;
    private String file;
}
