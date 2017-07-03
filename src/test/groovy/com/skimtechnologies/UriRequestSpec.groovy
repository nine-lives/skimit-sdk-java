package com.skimtechnologies

import com.fasterxml.jackson.databind.ObjectMapper
import com.skimtechnologies.util.ObjectMapperFactory
import spock.lang.Specification

class UriRequestSpec extends Specification {
    private ObjectMapper mapper = ObjectMapperFactory.make()

    def "I can make the request to the expected JSON payload"() {
        given:
        UriRequest request = new UriRequest()
                .withUri('https://en.wikipedia.org/wiki/Fake_news')

        when:
        Map<String, Object> result = mapper.readValue(mapper.writeValueAsString(request), Map.class)

        then:
        result.get("uri") == 'https://en.wikipedia.org/wiki/Fake_news'
        result.size() == 1
    }
}
