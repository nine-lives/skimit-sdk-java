package com.skimtechnologies.util

import com.skimtechnologies.AlertSkimsRequest
import com.skimtechnologies.Topic
import spock.lang.Specification

class RequestParameterMapperSpec extends Specification {

    private final RequestParameterMapper mapper = new RequestParameterMapper();

    def "I can get a map of the request parameters"() {
        given:
        AlertSkimsRequest request = new AlertSkimsRequest()
                .withLimit(3)
                .withSince(50000)
                .withTopics([Topic.investments, Topic.personnel] as TreeSet)
        when:
        Map<String, String> parameters = mapper.writeToMap(request)

        then:
        parameters['limit'] == "3"
        parameters['since'] == "50000"
        parameters['topics'] == "investments,personnel" || parameters['topics'] == "personnel,investments"
        parameters.size() == 3
    }

    def "I can write to query parameters"() {
        given:
        AlertSkimsRequest request = new AlertSkimsRequest()
                .withLimit(3)
                .withSince(50000)
                .withTopics([Topic.investments, Topic.personnel] as TreeSet)
        when:
        String query = new RequestParameterMapper().write(request)

        then:
        query == "?limit=3&since=50000&topics=investments%2Cpersonnel" || query == "?limit=3&since=50000&topics=personnel%2Cinvestments"
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
        String query = "https://api.skimtechnologies.com/v2/alert/1?limit=3&since=50000&topics=investments,personnel"

        when:
        AlertSkimsRequest request = mapper.read(new URL(query), AlertSkimsRequest);
        Map<String, String> parameters = new RequestParameterMapper().writeToMap(request)

        then:
        parameters['limit'] == "3"
        parameters['since'] == "50000"
        parameters['topics'] == "investments,personnel"
        parameters.size() == 3
    }

    def "I can modify a mapped request object"() {
        given:
        String query = "https://api.skimtechnologies.com/v2/alert/1?limit=3&since=50000&topics=investments,personnel"

        when:
        AlertSkimsRequest request = mapper.read(new URL(query), AlertSkimsRequest);
        request.withTopics([Topic.investments] as Set)
        request.withLimit(10)
        Map<String, String> parameters = new RequestParameterMapper().writeToMap(request)

        then:
        parameters['limit'] == "10"
        parameters['since'] == "50000"
        parameters['topics'] == "investments"
        parameters.size() == 3
    }
}
