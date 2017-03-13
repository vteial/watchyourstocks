package io.wybis.watchyourstocks.service

import io.wybis.watchyourstocks.dto.SessionDto

public interface AutoNumberService {

    long nextNumber(SessionDto sessionUser, String key);

}
