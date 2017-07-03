package com.skimtechnologies

import spock.lang.Specification

class ConfigurationSpec extends Specification {

    private String version;

    def setup() {
        Properties versionProperties = new Properties();
        versionProperties.load(Configuration.class.getClassLoader().getResourceAsStream("version.properties"));
        version = versionProperties.getProperty("version");
    }

    def "The defaults are the defaults"() {
        when:
        Configuration config = new Configuration()

        then:
        config.apiKey == null
        config.endpoint == 'https://api.skimtechnologies.com/v2'
        config.maxConnectionsPerRoute == 20
        config.userAgent == "skimit-java-sdk/${version}".toString()
        config.requestBurstSize == 20
        config.requestsPerSecond == 5
        !config.blockTillRateLimitReset
        version ==~ /2\.\d+\.\d+/
    }

    def "I can set configuration values"() {
        when:
        Configuration config = new Configuration()
            .withApiKey("secret")
            .withEndpoint("https://bpi.skimtechnologies.com/")
            .withMaxConnectionsPerRoute(22)
            .withUserAgent("ninelives/9.0.0")
            .withBlockTillRateLimitReset(true)
            .withRequestBurstSize(25)
            .withRequestsPerSecond(10)

        then:
        config.apiKey == 'secret'
        config.endpoint == 'https://bpi.skimtechnologies.com/'
        config.maxConnectionsPerRoute == 22
        config.userAgent == "ninelives/9.0.0 skimit-java-sdk/${version}".toString()
        config.requestBurstSize == 25
        config.requestsPerSecond == 10
        config.blockTillRateLimitReset
    }
}
