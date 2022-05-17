package com.fils.backend.services;

import com.fils.backend.domain.EmailVerificationToken;
import com.fils.backend.domain.Product;
import com.fils.backend.domain.User;
import com.fils.backend.repositories.EmailVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailTokenService {
    @Value("${emailtokendurationminutes}")
    Integer emailTokenDurationMinutes;

//    @Value("${passwordresettokendurationminutes}")
//    Integer passwordResetTokenDurationMinutes;

    @Autowired
    EmailVerificationTokenRepository emailTokenRepo;

    @Autowired
    UserService userService;

//    @Autowired
//    PasswordResetService passwordResetService;

    @Autowired
    private JavaMailSender mailSender;


    public String createEmailTokenForUserInDB(User user) {
        EmailVerificationToken emailToken = new EmailVerificationToken();
        String emailTokenString = UUID.randomUUID().toString();
        emailToken.setToken(emailTokenString);
        emailToken.setExpiry_date(LocalDateTime.now().plusMinutes(emailTokenDurationMinutes));
        emailToken.setUser(user);
        emailTokenRepo.save(emailToken);
        return emailTokenString;
    }

//    public String createPasswordTokenForUserInDB(User user){
//        PasswordResetToken passwordResetToken = new PasswordResetToken();
//        String passwordResetTokenString =  UUID.randomUUID().toString();
//        passwordResetToken.setToken(passwordResetTokenString);
//        passwordResetToken.setExpiry_date(LocalDateTime.now().plusMinutes(passwordResetTokenDurationMinutes));
//        passwordResetToken.setUser(user);
//        passwordResetService.savePasswordReset(passwordResetToken);
//        return passwordResetTokenString;
//    }

    public void sendVerificationEmail(User user, String emailTokenString) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "skills.overflow5@gmail.com";
        String senderName = "I am Romanian";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "I am Romanian team";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = "http://localhost:8080/verify?code=" + emailTokenString;
        System.out.println("SENT EMAIL TO : "  + user.getEmail() + "WITH TOKEN: " + emailTokenString  );

        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }
    public void sendNewsletter(User user, Product product) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "skills.overflow5@gmail.com";
        String senderName = "I am Romanian";
        String subject = "We have good news";
        String content = "Dear [[name]],<br>"
                + "We inform you that a product that you are interested in has changed its price:<br>"
                + "<strong>[[productName]]</strong> <br>"
                + "<img src=\"[[productImage]]\" height=\"200px\">"
                + "Thank you,<br>"
                + "I am Romanian team";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());
        content = content.replace("[[productName]]", product.getName());
        content = content.replace("[[productImage]]", product.getImageUrl());
        System.out.println("SENT EMAIL TO : "  + user.getEmail() + "PRODUCT CHANGED: " + product.getName() );

        helper.setText(content, true);
        mailSender.send(message);
    }

//    public boolean sendResetPassword(String email) throws MessagingException, UnsupportedEncodingException {
//        Optional<User> userByEmail = userService.getUserByEmail(email);
//
//        if(userByEmail.isPresent()){
//            String tokenForUser = createPasswordTokenForUserInDB(userByEmail.get());
//            String toAddress = email;
//            String fromAddress = "skills.overflow5@gmail.com";
//            String senderName = "Skills Overflow";
//            String subject = "Reset your password attempt + code";
//            String content = "Dear [[name]],<br>"
//                    + "Please click the link below to reset your password:<br>"
//                    + "<h3><a href=\"[[URL]]\" target=\"_self\">RESET</a></h3>"
//                    + "Thank you,<br>"
//                    + "Skills Overflow";
//
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message);
//
//            helper.setFrom(fromAddress, senderName);
//            helper.setTo(toAddress);
//            helper.setSubject(subject);
//
//            content = content.replace("[[name]]", userByEmail.get().getDisplayName());
//            String verifyURL = "http://localhost:8080/resetpassword?code=" + tokenForUser;
//            System.out.println("SENT EMAIL TO : "  + email + "WITH TOKEN: " + tokenForUser  );
//
//            content = content.replace("[[URL]]", verifyURL);
//            helper.setText(content, true);
//            mailSender.send(message);
//            return true;
//        }
//        return false;
//    }

    public boolean verify(String verificationCode) {
        Optional<EmailVerificationToken> token = emailTokenRepo.findByToken(verificationCode);

        if(token.isPresent()){
            if(token.get().getExpiry_date().isAfter(LocalDateTime.now())){
                // token valid ca timp
                User tempUser = token.get().getUser();
                tempUser.setActive(true);
                userService.saveUser(tempUser);
//                emailTokenRepo.delete(token.get());
                return true;
            }
        }
        return false;
    }
}
