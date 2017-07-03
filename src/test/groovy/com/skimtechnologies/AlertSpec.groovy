package com.skimtechnologies

import com.fasterxml.jackson.databind.ObjectMapper
import com.skimtechnologies.util.ObjectMapperFactory
import spock.lang.Specification

class AlertSpec extends Specification {

    private ObjectMapper mapper = ObjectMapperFactory.make();

    def "I can covert a JSON payload to the entity"() {
        given:
        String payload = '''
                 {
                    "id": "57da6ec907abd40e006195d4",
                    "title": "My First Alert"
                 }
       '''

        when:
        Alert entity = mapper.readValue(payload, Alert);

        then:
        with(entity) {
            id == '57da6ec907abd40e006195d4'
            title == 'My First Alert'
        }
    }
}
