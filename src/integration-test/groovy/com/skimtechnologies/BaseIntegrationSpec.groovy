package com.skimtechnologies

import com.skimtechnologies.util.ObjectMapperFactory
import spock.lang.Specification

public abstract class BaseIntegrationSpec extends Specification {
    protected static SkimIt skimIt
    private static boolean firstRun = true

    def setupSpec() {
        ObjectMapperFactory.setFailOnUnknownProperties(true)
        skimIt = SkimIt.make(new Configuration()
                .withEndpoint(System.getProperty("skimitEndpoint") ?: System.getenv("skimitEndpoint") ?: "https://api.skimtechnologies.com/v2")
                .withApiKey(System.getProperty("skimitApiKey") ?: System.getenv("skimitApiKey")))

        if (firstRun) {
            cleanupAll()
            setupBaseTestData()
            firstRun = false
        }
    }

    def cleanupSpec() {
    }

    private static setupBaseTestData() {
    }
}
