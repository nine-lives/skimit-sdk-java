package com.skimtechnologies

import com.skimtechnologies.util.RequestParameterMapper
import spock.lang.Specification

class AlertSkimsRequestSpec extends Specification {

    private final RequestParameterMapper mapper = new RequestParameterMapper();

    def "I can get a map of the request parameters"() {
        given:
        AlertSkimsRequest request = new AlertSkimsRequest()
                .withLimit(3)
                .withSince(1479474652)
                .withTopics([Topic.personnel] as Set)

        when:
        Map<String, String> parameters = mapper.writeToMap(request)

        then:
        parameters['limit'] == "3"
        parameters['since'] == "1479474652"
        parameters['topics'] == "personnel"
        parameters.size() == 3
    }

    def "I can write to query parameters"() {
        given:
        AlertSkimsRequest request = new AlertSkimsRequest()
                .withLimit(3)
                .withSince(1479474652)
                .withTopics([Topic.personnel] as Set)

        when:
        String query = new RequestParameterMapper().write(request)

        then:
        query == "?limit=3&since=1479474652&topics=personnel"
    }

    def "Query parameters is empty string if no values are set"() {
        given:
        AlertSkimsRequest request = new AlertSkimsRequest()

        when:
        String query = mapper.write(request)

        then:
        query == ""
    }

    def "I can convert a query string back to a request object"() {
        given:
        String query = "https://api.skimtechnologies.com/v2/alert/1?limit=3&since=1479474652&topics=investments,personnel"

        when:
        AlertSkimsRequest request = mapper.read(new URL(query), AlertSkimsRequest);
        Map<String, String> parameters = new RequestParameterMapper().writeToMap(request)

        then:
        parameters['limit'] == "3"
        parameters['since'] == "1479474652"
        parameters['topics'] == "investments,personnel"
        parameters.size() == 3
    }

    def "I can modify a mapped request object"() {
        given:
        String query = "https://api.skimtechnologies.com/v2/alert/1?limit=3&since=1479474652&topics=investments,personnel"

        when:
        AlertSkimsRequest request = mapper.read(new URL(query), AlertSkimsRequest);
        request.withTopics([Topic.investments] as Set)
        request.withLimit(10)
        Map<String, String> parameters = new RequestParameterMapper().writeToMap(request)

        then:
        parameters['limit'] == "10"
        parameters['since'] == "1479474652"
        parameters['topics'] == "investments"
        parameters.size() == 3
    }
}
