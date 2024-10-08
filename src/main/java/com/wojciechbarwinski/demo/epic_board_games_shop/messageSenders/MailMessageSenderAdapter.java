package com.wojciechbarwinski.demo.epic_board_games_shop.messageSenders;

import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailMessageSenderAdapter implements MessageSenderPort{

    @Value("${spring.mail.username}")
    private String shopMail;

    @Value("${spring.shopLink.order}")
    private String shopOrderLink;

    private final JavaMailSender mailSender;

    @Override
    public void sendSimpleMessageAfterOrderWasConfirmed(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(shopMail);
        message.setTo(order.getOrdererMail());
        message.setSubject("Epic Board Game Shop - Order");
        message.setText(generateMailText(order.getId()));
        mailSender.send(message);
    }

    private String generateMailText(Long id) {
        String linkToPay = shopOrderLink + id + "/payment";
        String linkToCancel = shopOrderLink + id + "/cancel";

        return "Thank you for placing your order. Here are some useful links:\n\n" +
                "1. To pay for your order, click here: " + linkToPay + "\n" +
                "2. To cancel your order, click here: " + linkToCancel + "\n\n" +
                "Best regards,\nThe Epic Board Games Shop Team";
    }
}
