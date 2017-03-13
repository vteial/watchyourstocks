package io.wybis.watchyourstocks.service

import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.dto.UserDto
import io.wybis.watchyourstocks.service.exceptions.GeneralException
import io.wybis.watchyourstocks.service.exceptions.InvalidCredentialException
import io.wybis.watchyourstocks.service.exceptions.ModelNotFoundException

import javax.servlet.http.HttpSession

public interface SessionService {

    static String SESSION_USER_KEY = 'user'

    static String SESSION_USER_ID_KEY = 'userId'

    static String SESSION_USER_PASSWORD_KEY = 'userPassword'

    static String SESSION_LOGIN_REDIRECT_KEY = 'loginRedirectKey'

    boolean validateRecaptcha(String recaptchaValue)

    Map<String, Object> properties(HttpSession session)

//    String resetPasswordRequest(UserDto userDto, String domainPrefix) throws ModelNotFoundException, GeneralException

    void signIn(HttpSession session, UserDto userDto)
            throws InvalidCredentialException

    void signOut(HttpSession session)

    void changeDetails(SessionDto sessionUser, UserDto userDto)
            throws ModelNotFoundException

    void changePassword(SessionDto sessionUser, UserDto userDto)
            throws ModelNotFoundException, GeneralException

}
