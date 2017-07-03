package com.skimtechnologies

import java.time.Clock

class ExtractionIntegrationSpec extends BaseIntegrationSpec {

    def setupSpec() {
    }

    def "I can see the data from the server is correct"() {
        when:
        Extraction extraction = skimIt.extraction('https://en.wikipedia.org/wiki/News_satire');

        then:
        extraction.uri == 'https://en.wikipedia.org/wiki/News_satire'
        extraction.language == 'en'
        extraction.title.length() > 0
        extraction.images.size() > 0
        extraction.videos.size() > 0
        extraction.metaKeywords.length() > 0
        extraction.pageType.length() > 0
        extraction.favicon.length() > 0
        extraction.date.length() > 0
        println extraction.markup
    }
}
