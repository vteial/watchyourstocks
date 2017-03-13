package io.wybis.watchyourstocks.model

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.Canonical
import groovy.transform.ToString
import io.wybis.watchyourstocks.dto.SessionDto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
@Canonical
@ToString(includeNames = true)
public class AppConfig extends AbstractModel {

    @Id
    @Column(name = 'id')
    long id

    @Column(name = 'value')
    String value

    transient Map<String, Object> valueExtended

    @Column(name = 'value_extended')
    String valueExtendedX

    @Column(name = 'for_user_id')
    long forUserId

    transient User forUser

    @Column(name = 'by_user_id')
    long byUserId

    transient User byUser

    @Column(name = 'branch_id')
    long branchId

    transient Branch branch

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
    void deserializeFromJson(ObjectMapper jsonObjectMapper) {
        try {
            if (valueExtendedX) {
                valueExtended = jsonObjectMapper.readValue(valueExtendedX, new TypeReference<Map<String, Object>>() {})
            }
        } catch (Throwable t) {
            t.printStackTrace()
        }
    }

    void serializeToJson(ObjectMapper jsonObjectMapper) {
        try {
            if (valueExtended) {
                StringWriter sw = new StringWriter()
                jsonObjectMapper.writeValue(sw, value)
                valueExtendedX = sw.toString()
            }
        } catch (Throwable t) {
            t.printStackTrace()
        }
    }
}
