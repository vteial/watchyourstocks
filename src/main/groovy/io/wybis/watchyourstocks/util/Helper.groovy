package io.wybis.watchyourstocks.util

class Helper {

    static String capitalize(String s) {
        if (!s || s == '') {
            return s
        }
        if (s.indexOf(' ') == -1) {
            return s.capitalize()
        }
        s = s.tokenize().collect { it.capitalize() }.join(' ')
        return s
    }

    static String getStackTraceAsString(Throwable t) {
        StringWriter sw = new StringWriter()
        PrintWriter pw = new PrintWriter(sw)
        t.printStackTrace(pw)
        return sw.toString()
    }

    static String executeCommand(String cmd) {
        def sout = new StringBuffer(), serr = new StringBuffer()
        def proc = cmd.execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(1000)
//    	println "out> $sout"
//    	println "err> $serr"
        return sout.toString()
    }
}
