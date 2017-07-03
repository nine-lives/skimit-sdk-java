package com.skimtechnologies

import com.fasterxml.jackson.databind.ObjectMapper
import com.skimtechnologies.util.ObjectMapperFactory
import spock.lang.Specification

class AlertFeedbackValueSpec extends Specification {

    private ObjectMapper mapper = ObjectMapperFactory.make();

    def "I can see that the enum values are correct"() {
        given:

        when:
        AlertFeedbackValue[] values = AlertFeedbackValue.values()

        then:
        values.length == 2
        values[0] == AlertFeedbackValue.IRRELEVANT
        values[0].feedbackValue == 0
        values[1] == AlertFeedbackValue.RELEVANT
        values[1].feedbackValue == 1
    }
}
