package io.wybis.watchyourstocks.web.controllers

import org.springframework.mail.MailSender

import javax.annotation.Resource;

public abstract class AbstractController {

    @Resource
    MailSender mailSender
}
