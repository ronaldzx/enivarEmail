package com.mail.sender.service.impl;

import com.mail.sender.service.MailSenderService;
import com.mail.sender.dto.MailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Base64;
import java.util.UUID;

import static com.mail.sender.config.Constants.FILE_PATH;

@Service
public class MailSenderServiceImpl implements MailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public void send(MailDTO mailDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getTo());
        message.setSubject(mailDTO.getSubject());
        message.setText(mailDTO.getText());
        javaMailSender.send(message);
    }

    @Override
    public void sendWithAttach(MailDTO mailDTO) throws MessagingException, IOException {
        /**
         * Se obtiene la extensión del archivo y el nombre por separados para luego
         * añadirle el randomUUID de Java
         */
        String extension = mailDTO.getFileName().substring(mailDTO.getFileName().lastIndexOf('.'));
        String fileNameWithoutExtension = mailDTO.getFileName().substring(0, mailDTO.getFileName().lastIndexOf('.'));
        String finalFileName = generarFileName(fileNameWithoutExtension, extension);
        /**
         * Con el nuevo nombre de fichero se crea el fichero en la ruta especificada
         */
        try {
            crearArchivoEnDisco(finalFileName, mailDTO.getFile());
        }catch (IOException e){
            throw new IOException(e);
        }
        /**
         * Una vez creado el fichero se procede a estructurar el email y luego adjuntar
         * el fichero buscandolo en la ruta guardada
         */
        MimeMessage message = javaMailSender.createMimeMessage();
        BodyPart text = new MimeBodyPart();
        text.setText(mailDTO.getText());
        BodyPart attach = new MimeBodyPart();
        attach.setDataHandler(new DataHandler(new FileDataSource(FILE_PATH + finalFileName)));
        attach.setFileName(mailDTO.getFileName());
        MimeMultipart multiParte = new MimeMultipart();
        multiParte.addBodyPart(text);
        multiParte.addBodyPart(attach);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailDTO.getTo()));
        message.setSubject(mailDTO.getSubject());
        message.setContent(multiParte);
        javaMailSender.send(message);
    }

    /**
     * Se intenta crear archivo en disco
     * @param fileName es la ruta enviada desde el cliente
     */
    private void crearArchivoEnDisco(String fileName, String file) throws IOException {
        File fileToSave = new File(FILE_PATH + fileName);
        byte[] bytes = Base64.getDecoder().decode(file);
        FileOutputStream fos = new FileOutputStream(fileToSave);
        fos.write(bytes);
    }

    /**
     * Generar nombre aleatorio para guardarlo y posteriormente buscarlo y enviarlo por correo
     * @param fileName es el nombre del archivo que envía el cliente
     * @return el nombre del archivo mas un numero aleatorio
     */
    private String generarFileName(String fileName, String extension){
        return fileName + "-" + UUID.randomUUID() + extension;
    }
}
