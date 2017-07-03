package com.skimtechnologies

import com.skimtechnologies.util.ObjectMapperFactory
import spock.lang.Specification

abstract class BaseIntegrationSpec extends Specification {
    protected static SkimIt skimIt

    def setupSpec() {
        ObjectMapperFactory.setFailOnUnknownProperties(true)
        skimIt = SkimIt.make(new Configuration()
                .withEndpoint(System.getProperty("skimitEndpoint") ?: System.getenv("skimitEndpoint") ?: "https://api.skimtechnologies.com/v2")
                .withApiKey(System.getProperty("skimitApiKey") ?: System.getenv("skimitApiKey")))
    }
}
