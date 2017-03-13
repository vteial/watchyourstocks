package io.wybis.watchyourstocks.model

import groovy.transform.Canonical
import groovy.transform.ToString
import io.wybis.watchyourstocks.dto.SessionDto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = 'product_rate')
@Canonical
@ToString(includeNames = true)
class ProductRate implements Model {

    private static final long serialVersionUID = 1L;

    static final String ID_KEY = "productRateId"

    @Id
    @Column(name = 'id')
    long id

    @Column(name = 'code')
    String code

    @Column(name = 'name')
    String name

    @Column(name = 'value')
    double value

    @Column(name = 'fetch_time')
    Date fetchTime

    @Column(name = 'exchange')
    String exchange

    @Column(name = 'provided_by')
    String providedBy

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
