package com.skimtechnologies

import com.fasterxml.jackson.databind.ObjectMapper
import com.skimtechnologies.util.ObjectMapperFactory
import spock.lang.Specification

class SkimItErrorSpec extends Specification {

    private ObjectMapper mapper = ObjectMapperFactory.make();

    def "I can covert a JSON payload to the entity"() {
        given:
        String payload = '''
            {
              "timestamp": "2017-07-03T10:54:28.685780",
              "error": "Parameter \\"uri\\" is required"
            }
       '''

        when:
        SkimItError entity = mapper.readValue(payload, SkimItError);

        then:
        with(entity) {
            timestamp == '2017-07-03T10:54:28.685780'
            error == 'Parameter "uri" is required'
        }
    }
}
