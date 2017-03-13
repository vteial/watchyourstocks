package io.wybis.watchyourstocks.model

import groovy.transform.Canonical
import groovy.transform.ToString
import io.wybis.watchyourstocks.dto.SessionDto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = 'branch')
@Canonical
@ToString(includeNames = true)
public class Branch extends AbstractModel {

    static final String ID_KEY = 'branchId'

    @Id
    @Column(name = 'id')
    long id

    @Column(name = 'code')
    String code

    @Column(name = 'name')
    String name

    @Column(name = 'alias_name')
    String aliasName

    @Column(name = 'licence_number')
    String licenceNumber

    @Column(name = 'email_id')
    String emailId

    @Column(name = 'hand_phone_number')
    String handPhoneNumber

    @Column(name = 'lang_phone_number')
    String landPhoneNumber

    @Column(name = 'fax_number')
    String faxNumber;

    @Column(name = 'address_id')
    long addressId

    transient Address address

    @Column(name = 'virtual_employee_id')
    long virtualEmployeeId

    @Column(name = 'status')
    String status

    @Column(name = 'parent_id')
    long parentId

    transient List<Account> accounts;

    transient List<Product> products

    transient List<User> employees

    transient List<User> brokers

    transient List<User> investors

    // common fields...
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
}