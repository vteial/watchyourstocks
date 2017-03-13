package io.wybis.watchyourstocks.dto

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.Canonical
import groovy.transform.ToString

@Canonical
@ToString(includeNames = true)
class RecaptchaResponseDto {

    boolean success

    List<String> errorCodes

    @JsonProperty('error-codes')
    List<String> getErrorCodes() {
        return errorCodes
    }
}
