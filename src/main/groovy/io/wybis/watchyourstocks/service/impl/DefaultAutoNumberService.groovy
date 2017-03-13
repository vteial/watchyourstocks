package io.wybis.watchyourstocks.service.impl

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.AutoNumber
import io.wybis.watchyourstocks.repository.AutoNumberRepository
import io.wybis.watchyourstocks.service.AutoNumberService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Service
@Slf4j
public class DefaultAutoNumberService implements AutoNumberService {

    @Resource
    AutoNumberRepository autoNumberRepository

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public synchronized long nextNumber(SessionDto sessionUser, String key) {
        AutoNumber an = null

        Date now = new Date()
        an = this.autoNumberRepository.findOne(key)
        if (an == null) {
            an = new AutoNumber()
            an.id = key
            an.prePersist(sessionUser, now)
        } else {
            an.preUpdate(sessionUser, now)
        }
        an.value += 1
        an = this.autoNumberRepository.save(an)

        return an.value
    }

}
