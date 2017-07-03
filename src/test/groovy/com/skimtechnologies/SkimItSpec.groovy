package com.skimtechnologies

import spock.lang.Specification

class SkimItSpec extends Specification {

    def "I can use the auth key constructor to create the api"() {
        when:
        SkimIt skimIt = SkimIt.make('apiKey')

        then:
        Configuration config = skimIt.client.configuration;
        config.apiKey == 'apiKey'
        config.endpoint == 'https://api.skimtechnologies.com/v2'
        config.maxConnectionsPerRoute == 20
        config.requestBurstSize == 20
        config.requestsPerSecond == 5
        !config.blockTillRateLimitReset
    }

    def "I can use the configuration object to create the api"() {
        given:
        Configuration config = new Configuration()
                .withApiKey("secret")
                .withEndpoint("https://bpi.skimtechnologies.com/v2")

        when:
        SkimIt skimIt = SkimIt.make(config)

        then:
        config == skimIt.client.configuration;
        config.endpoint == "https://bpi.skimtechnologies.com/v2"
    }
}
