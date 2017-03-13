package io.wybis.watchyourstocks.service.impl

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.RecaptchaResponseDto
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.dto.UserDto
import io.wybis.watchyourstocks.model.Account
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.model.Product
import io.wybis.watchyourstocks.model.User
import io.wybis.watchyourstocks.model.constants.UserStatus
import io.wybis.watchyourstocks.repository.AccountRepository
import io.wybis.watchyourstocks.repository.BranchRepository
import io.wybis.watchyourstocks.repository.ProductRepository
import io.wybis.watchyourstocks.repository.UserRepository
import io.wybis.watchyourstocks.service.ProductService
import io.wybis.watchyourstocks.service.SessionService
import io.wybis.watchyourstocks.service.UserService
import io.wybis.watchyourstocks.service.exceptions.GeneralException
import io.wybis.watchyourstocks.service.exceptions.InvalidCredentialException
import io.wybis.watchyourstocks.service.exceptions.ModelNotFoundException
import io.wybis.watchyourstocks.service.exceptions.UnAuthorizedException
import org.springframework.stereotype.Service

import javax.annotation.Resource
import javax.servlet.http.HttpSession

@Service
@Slf4j
public class DefaultSessionService extends AbstractService implements
        SessionService {

    @Resource
    BranchRepository branchRepository;

    @Resource
    AccountRepository accountRepository;

    @Resource
    ProductRepository productRepository;

    @Resource
    UserRepository userRepository;

    @Resource(name = 'userService')
    UserService userService

    @Resource
    ProductService productService

    Map<String, Object> app = [:]

    @Override
    boolean validateRecaptcha(String recaptchaValue) {
        RecaptchaResponseDto recaptchaResponseDto = null

//        URL url = new URL('https://www.google.com/recaptcha/api/siteverify')
//        def params = [secret: '6Lds6xQTAAAAAPOmsJnFYEVRmfiC1QCKpVy71DpA', response: recaptchaValue]
//        def response = url.post(params: params)
//        if (response.statusCode != 200) {
//            log.severe(response.text)
//            log.severe('Recaptcha verifiction failed...')
//        }
//        if (localMode) {
//            System.out.println(response.text)
//        }
//        recaptchaResponseDto = JacksonCategory.jsonObjectMapper.readValue(response.text, RecaptchaResponseDto.class)
//        if (localMode) {
//            System.out.println(recaptchaResponseDto)
//        }

        return recaptchaResponseDto.success
    }

    @Override
    public Map<String, Object> properties(HttpSession session) {
        def props = this.app.clone()

        props.sessionDto = session.getAttribute(SESSION_USER_KEY)
        props.sessionId = session.id

        return props;
    }

/*
    @Override
    String resetPasswordRequest(UserDto userDto, String domainPrefix) throws ModelNotFoundException, GeneralException {
        if (!userDto.recaptchaValue) {
            throw new GeneralException('Missing recaptcha value...')
        }

        if (!this.validateRecaptcha(userDto.recaptchaValue)) {
            throw new GeneralException('Invalid recaptcha value...')
        }

        userDto.userId = userDto.userId.toLowerCase();
        def dsl = datastore.build {
            from User.class.simpleName
            where userId == userDto.userId
            limit 1
        }
        def entitys = dsl.execute()
        if (entitys.size() == 0) {
            throw new ModelNotFoundException()
        }

        User auser = entitys[0] as User
        auser.token = UUID.randomUUID().toString()
        auser.save()

        return this.sendResetPasswordEmail(auser, domainPrefix)
    }

    private String sendResetPasswordEmail(User auser, String domainPrefix) {
        log.info('reset password email started...')

        String resetUrl = "${domainPrefix}/sessions/reset-password-confirm?userId=${auser.id}&userToken=${auser.token}"

        String mailContent = """
Hi ${auser.firstName},

    Please use this link ${resetUrl} to reset password for your account.

Thanks and Regards,
App Admin,
watchyourstocks.appspot.com
"""
        mail.send(from: 'vteial@gmail.com',
                to: auser.emailId,
                subject: "EventAndGifts Reset Password",
                textBody: mailContent)

        log.info("reset url is ${resetUrl}")
        log.info('reset password email finished...')

        return resetUrl
    }
*/

    public void signIn(HttpSession session, UserDto userDto)
            throws InvalidCredentialException {

        User aUser = this.userService.findByUserId(userDto.userId)
        log.debug('Is {} exist in db = {}', userDto.userId, (aUser != null))
        if (!aUser) {
            throw new InvalidCredentialException()
        }

        if (aUser.status == UserStatus.PENDING || aUser.status == UserStatus.PASSIVE) {
            throw new UnAuthorizedException()
        }

        if (!this.isProfileIsActive('dev') && aUser.password != userDto.password) {
            throw new InvalidCredentialException()
        }

        SessionDto sessionDto = new SessionDto()
        sessionDto.with {
            id = aUser.id
            userId = aUser.userId
            firstName = aUser.firstName
            lastName = aUser.lastName
            type = aUser.type
            roleId = aUser.roleId
            cashAccountId = aUser.cashAccountId
            profitAccountId = aUser.profitAccountId
            branchId = aUser.branchId
        }
        Account account = this.accountRepository.findOne(aUser.cashAccountId)
        sessionDto.cashAccount = account
        Product product = this.productRepository.findOne(account.productId)
        account.product = product
        account = this.accountRepository.findOne(aUser.profitAccountId)
        sessionDto.profitAccount = account
        product = this.productRepository.findOne(account.productId)
        account.product = product

        Branch branch = this.branchRepository.findOne(sessionDto.branchId)
        sessionDto.branchCode = branch.code
        sessionDto.branchName = branch.name
        sessionDto.branchVirtualEmployeeId = branch.virtualEmployeeId

        session.setAttribute(SESSION_USER_KEY, sessionDto)

    }

    @Override
    public void signOut(HttpSession session) {
        session.removeAttribute(SESSION_USER_KEY)
    }

    @Override
    public void changeDetails(SessionDto sessionUser, UserDto userDto)
            throws ModelNotFoundException {
        User euser = this.userRepository.findOne(sessionUser.id)

        euser.firstName = userDto.firstName
        euser.lastName = userDto.lastName

        Date now = new Date()
        euser.preUpdate(sessionUser, now)

        euser = this.userRepository.save(euser)

        sessionUser.firstName = euser.firstName
        sessionUser.lastName = euser.lastName
    }

    @Override
    public void changePassword(SessionDto sessionUser, UserDto userDto)
            throws ModelNotFoundException, GeneralException {
        User euser = this.userRepository.findOne(sessionUser.id)

        if (!euser) {
            throw new ModelNotFoundException()
        }

        if (euser.password != userDto.currentPassword) {
            throw new GeneralException('Invalid current password...')
        }

        if (userDto.newPassword != userDto.retypeNewPassword) {
            throw new GeneralException('New password and Retype new password should be equal...')
        }

        if (euser.password == userDto.newPassword) {
            throw new GeneralException('Current password and New password should not be equal...')
        }

        euser.password = userDto.newPassword

        Date now = new Date()
        euser.preUpdate(sessionUser, now)

        this.userRepository.save(euser)
    }
}
