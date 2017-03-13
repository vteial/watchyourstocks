package io.wybis.watchyourstocks.service.impl

import io.wybis.watchyourstocks.service.AutoNumberService
import org.springframework.core.env.Environment

import javax.annotation.Resource

abstract class AbstractService {

    @Resource
    Environment env;

    @Resource
    protected AutoNumberService autoNumberService;

    protected boolean isProfileIsActive(String profileId) {

        return env.activeProfiles.contains(profileId)

    }
}
