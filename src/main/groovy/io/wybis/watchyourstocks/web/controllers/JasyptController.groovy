package io.wybis.watchyourstocks.web.controllers

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.ResponseDto
import io.wybis.watchyourstocks.util.Helper
import org.jasypt.encryption.StringEncryptor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import javax.annotation.Resource

@Controller
@RequestMapping('/sessions')
@Slf4j
class JasyptController extends AbstractController {

    private static final String ENCRYPTED_VALUE_PREFIX = 'ENC('
    private static final String ENCRYPTED_VALUE_SUFFIX = ')'

    @Resource
    private StringEncryptor stringEncryptor

    static boolean isEncryptedValue(final String value) {
        if (value == null) {
            return false
        }
        final String trimmedValue = value.trim()
        return (trimmedValue.startsWith(ENCRYPTED_VALUE_PREFIX) &&
                trimmedValue.endsWith(ENCRYPTED_VALUE_SUFFIX))
    }

    private static String getInnerEncryptedValue(final String value) {
        return value.substring(
                ENCRYPTED_VALUE_PREFIX.length(),
                (value.length() - ENCRYPTED_VALUE_SUFFIX.length()))
    }

    @RequestMapping(value = '/encrypt', method = RequestMethod.GET)
    @ResponseBody
    ResponseDto encrypt(@RequestParam('text') String text) {
        ResponseDto responseDto = new ResponseDto();

        try {
            String encrypted = stringEncryptor.encrypt(text.trim())
            if (log.isDebugEnabled()) {
                log.debug('ORIGINAL  : {}', text)
                log.debug('ENCRYPTED : {}', encrypted)
                log.debug('DECRYPTED : {}', stringEncryptor.decrypt(encrypted))
            }
            responseDto.data = String.format('ENC(%s)', encrypted)
        } catch (Throwable t) {
            log.error('encryption failed', t)
            responseDto.setType(ResponseDto.ERROR);
            responseDto.message = 'encryption failed...'
            responseDto.data = Helper.getStackTraceAsString(t)
        }

        return responseDto;
    }

    @RequestMapping(value = '/decrypt', method = RequestMethod.GET)
    @ResponseBody
    ResponseDto decrypt(@RequestParam('text') String text) {
        ResponseDto responseDto = new ResponseDto();

        try {
            String decrypted = stringEncryptor.decrypt(isEncryptedValue(text) ? getInnerEncryptedValue(text) : text);
            if (log.isDebugEnabled()) {
                log.debug('ORIGINAL  : {}', text);
                log.debug('DECRYPTED : {}', decrypted);
                log.debug('ENCRYPTED : {}', String.format('ENC(%s)', stringEncryptor.encrypt(decrypted)));
            }
            responseDto.data = decrypted;
        } catch (Throwable t) {
            log.error('decryption failed', t)
            responseDto.setType(ResponseDto.ERROR);
            responseDto.message = 'decryption failed...'
            responseDto.data = Helper.getStackTraceAsString(t)
        }

        return responseDto;
    }

}
