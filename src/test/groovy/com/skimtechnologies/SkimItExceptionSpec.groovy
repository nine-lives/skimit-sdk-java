package com.skimtechnologies

import spock.lang.Specification

class SkimItExceptionSpec extends Specification {

    def "I can create an exception with just a message"() {
        when:
        SkimItException e = new SkimItException("error message")

        then:
        e.message == "error message"
    }

    def "I can create an exception with just a cause"() {
        given:
        IllegalArgumentException cause = new IllegalArgumentException();
        when:
        SkimItException e = new SkimItException(cause)

        then:
        e.cause == cause
    }

}
