package com.skimtechnologies

import spock.lang.Specification

class SkimItServerExceptionSpec extends Specification {

    def "I can construct the exception"() {
        given:
        SkimItError error = new SkimItError()
        error.error = "error_message"
        error.timestamp = "2017-07-03T10:54:28.685780"

        when:
        SkimItServerException e = new SkimItServerException(401, 'Unauthorised', error)

        then:
        e.statusCode == 401
        e.statusMessage == 'Unauthorised'
        e.error.error == 'error_message'
        e.error.timestamp == '2017-07-03T10:54:28.685780'
    }
}
