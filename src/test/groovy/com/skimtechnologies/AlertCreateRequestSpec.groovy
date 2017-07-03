package com.skimtechnologies

import com.fasterxml.jackson.databind.ObjectMapper
import com.skimtechnologies.util.ObjectMapperFactory
import spock.lang.Specification

class AlertCreateRequestSpec extends Specification {
    private ObjectMapper mapper = ObjectMapperFactory.make()

    def "I can make the request to the expected JSON payload"() {
        given:
        AlertCreateRequest request = new AlertCreateRequest()
                .withTitle('My Other Alert')
                .withKeyTerms(['term1', 'term2'] as TreeSet<String>)

        when:
        Map<String, Object> result = mapper.readValue(mapper.writeValueAsString(request), Map.class)

        then:
        result.get("title") == 'My Other Alert'
        result.get("key_terms") instanceof List
        result.get("key_terms") as Set == ['term1', 'term2'] as Set
        result.size() == 2
    }

    def "I only send attributes that are set"() {
        given:
        AlertCreateRequest request = new AlertCreateRequest()

        when:
        Map<String, Object> result = mapper.readValue(mapper.writeValueAsString(request), Map.class)

        then:
        result.size() == 0
    }
}
