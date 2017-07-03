package com.skimtechnologies

import spock.lang.Unroll

import java.time.Clock

class SkimIntegrationSpec extends BaseIntegrationSpec {

    def setupSpec() {
    }

    def "I can see the data from the server is correct"() {
        when:
        long clock =  Clock.systemUTC().millis()
        Skim skim
        try {
            skim = skimIt.skim('https://en.wikipedia.org/wiki/Fake_news');
        } catch (Exception e) {
            println "MS: " + (Clock.systemUTC().millis() - clock);
            throw e;
        }

        then:
        skim.uri == 'https://en.wikipedia.org/wiki/Fake_news'
        skim.readingTimeSaved > 0
        skim.readingTimeOriginal > 0
        skim.keywords.size() > 0
        skim.subtopicProbabilities.size() > 0
        skim.body.length() > 0
        skim.html.length() > 0
        skim.type == 'article'
        skim.title.length() > 0
        skim.images.size() > 0
    }
}
