package io.wybis.watchyourstocks.dto

import groovy.transform.Canonical
import groovy.transform.ToString
import io.wybis.watchyourstocks.model.Account

@Canonical
@ToString(includeNames = true)
class SessionDto implements Serializable {

    long id

    String userId

    String emailId

    String firstName

    String lastName

    String type

    String roleId

    long cashAccountId

    Account cashAccount

    long profitAccountId

    Account profitAccount

    long branchId

    String branchCode

    String branchName

    long branchVirtualEmployeeId

}
