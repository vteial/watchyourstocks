package io.wybis.wysbisanalytics

import groovy.sql.Sql
import io.wybis.watchyourstocks.model.AccessData
import org.joda.time.DateTime

import java.util.regex.Matcher
import java.util.regex.Pattern

println 'started...'

Pattern filePattern  = Pattern.compile('^([a-z].*)-(\\d+)-(\\d\\d)-(\\d\\d)_\\d\\d_\\d\\d_\\d\\d\\.\\d\\d\\d\\.log')
String fileName = 'harmoney-main-2015-09-30_00_00_58.740.log'
Matcher matcher = filePattern.matcher(fileName)
if(matcher.matches()) {
    println matcher
    println matcher[0][1]
    println matcher[0][2]
    println matcher[0][3]
    println matcher[0][4]
}

println 'finished...'