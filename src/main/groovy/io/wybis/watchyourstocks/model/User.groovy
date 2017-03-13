package io.wybis.watchyourstocks.model

import groovy.transform.Canonical
import groovy.transform.ToString
import io.wybis.watchyourstocks.dto.SessionDto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = 'users')
@Canonical
@ToString(includeNames = true)
public class User extends AbstractModel {

    static final String ID_KEY = 'userId'

    @Id
    @Column(name = 'id')
    long id

    @Column(name = 'userId')
    String userId

    @Column(name = 'password')
    String password

    transient String retypePassword

    @Column(name = 'id_number')
    String identificationNumber

    @Column(name = 'email_id')
    String emailId

    @Column(name = 'first_name')
    String firstName

    @Column(name = 'last_name')
    String lastName

    @Column(name = 'hand_phone_number')
    String handPhoneNumber

    @Column(name = 'land_phone_number')
    String landPhoneNumber

    @Column(name = 'address_id')
    long addressId

    transient Address address

    @Column(name = 'token')
    String token

    @Column(name = 'type')
    String type

    @Column(name = 'status')
    String status

    @Column(name = 'role_id')
    String roleId

    transient Role role

    @Column(name = 'branch_id')
    long branchId

    transient Branch branch

    @Column(name = 'cash_account_id')
    long cashAccountId

    transient Account cashAccount

    @Column(name = 'profit_account_id')
    long profitAccountId

    transient Account profitAccount

    transient List<Long> accountIds

    transient List<Account> accounts

    // common fields
    @Column(name = 'create_time', nullable = false)
    Date createTime;

    @Column(name = 'update_time', nullable = false)
    Date updateTime;

    @Column(name = 'create_by', nullable = false)
    long createBy;

    @Column(name = 'update_by', nullable = false)
    long updateBy;

    // persistance operations
    public void preUpdate(SessionDto sessionUser, Date now) {
        this.updateTime = now
        this.updateBy = sessionUser.id
    }

    public void prePersist(SessionDto sessionUser, Date now) {
        this.createTime = now
        this.updateTime = now
        this.createBy = sessionUser.id
        this.updateBy = sessionUser.id
    }

    // domain operations
    void correctData() {
        if (userId) {
            userId = userId.toLowerCase()
        }
        if (emailId) {
            emailId = emailId.toLowerCase()
        }
        if (firstName) {
            firstName = firstName.toLowerCase()
        }
        if (lastName) {
            lastName = lastName.toLowerCase()
        }
    }

    boolean isVirtual() {
        return userId == null ? false : userId.startsWith('${this.branchId}@')
    }

}