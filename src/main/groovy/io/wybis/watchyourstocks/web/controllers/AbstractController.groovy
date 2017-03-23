package io.wybis.watchyourstocks.web.controllers

import org.springframework.core.env.Environment
import org.springframework.mail.MailSender

import javax.annotation.Resource;

public abstract class AbstractController {

    @Resource
    Environment env

    @Resource
    MailSender mailSender

    protected boolean isProfileIsActive(String profileId) {

        return env.activeProfiles.contains(profileId)

    }

}
