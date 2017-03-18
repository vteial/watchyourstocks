package io.wybis.watchyourstocks.model

import groovy.transform.Canonical
import groovy.transform.ToString
import io.wybis.watchyourstocks.dto.SessionDto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = 'auto_number')
@Canonical
@ToString(includeNames = true)
public class AutoNumber extends AbstractModel {

    private static final long serialVersionUID = 1L

    @Id
    @Column(name = 'id')
    String id

    @Column(name = 'val', nullable = false)
    long value

    // common fields...
    @Column(name = 'create_time', nullable = false)
    Date createTime

    @Column(name = 'update_time', nullable = false)
    Date updateTime

    @Column(name = 'create_by', nullable = false)
    long createBy

    @Column(name = 'update_by', nullable = false)
    long updateBy

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