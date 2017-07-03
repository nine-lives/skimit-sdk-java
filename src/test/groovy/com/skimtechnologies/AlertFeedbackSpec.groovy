package com.skimtechnologies

import com.fasterxml.jackson.databind.ObjectMapper
import com.skimtechnologies.util.ObjectMapperFactory
import spock.lang.Specification

class AlertFeedbackSpec extends Specification {

    private ObjectMapper mapper = ObjectMapperFactory.make();

    def "I can covert a JSON payload to the entity"() {
        given:
        String payload = '''
            {
              "current_size": {
                "irrelevant": 2,
                "relevant": 23
              }
            }
       '''

        when:
        AlertFeedback entity = mapper.readValue(payload, AlertFeedback);

        then:
        with(entity.currentSize) {
            irrelevant == 2
            relevant == 23
        }
    }
}
