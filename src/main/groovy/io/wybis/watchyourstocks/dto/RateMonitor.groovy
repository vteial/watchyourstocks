package io.wybis.watchyourstocks.dto

import groovy.transform.Canonical
import groovy.transform.ToString

@Canonical
@ToString(includeNames = true)
class RateMonitor implements Serializable {

    String id

    String code

    String name

    double lowerValue

    String lowerValueS

    double upperValue

    String upperValueS

    double value

    String status

//    Date metTime
    long metTime

    long branchId

    long createBy

    long updateBy

//    Date createTime
    long createTime

//    Date updateTime
    long updateTime

}
