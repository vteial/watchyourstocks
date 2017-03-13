package io.wybis.watchyourstocks.model

import groovy.transform.Canonical
import groovy.transform.ToString
import io.wybis.watchyourstocks.dto.SessionDto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = 'address')
@Canonical
@ToString(includeNames = true)
class Address extends AbstractModel {

    public static final String ID_KEY = 'addressId'

    @Id
    @Column(name = 'id')
    long id

    @Column(name = 'address')
    String address

    @Column(name = 'city_or_town')
    String cityOrTown

    @Column(name = 'postal_code')
    String postalCode

    @Column(name = 'state_code')
    String stateCode

    @Column(name = 'country_code')
    String countryCode

    // common fields...
    @Column(name = 'create_time', nullable = false)
    Date createTime;

    @Column(name = 'update_time', nullable = false)
    Date updateTime;

    @Column(name = 'create_by', nullable = false)
    long createBy;

    @Column(name = 'update_by', nullable = false)
    long updateBy;

    // persistence operations
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
