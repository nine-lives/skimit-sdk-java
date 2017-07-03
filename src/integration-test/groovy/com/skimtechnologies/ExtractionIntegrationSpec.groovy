package com.skimtechnologies

class ExtractionIntegrationSpec extends BaseIntegrationSpec {

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
    }

    def "I get an error when I try to call the server with no uri"() {
        when:
        skimIt.extraction(null);

        then:
        SkimItServerException exception = thrown(SkimItServerException)
        exception.error != null
        exception.error.error == 'Parameter \\"uri\\" is required'
        exception.error.timestamp != null
        exception.statusCode == 400
        exception.statusMessage == 'Bad request'
    }

    def "I get an error when I try to call the server with an invalid uri"() {
        when:
        skimIt.extraction("this-is-not-valid");

        then:
        SkimItServerException exception = thrown(SkimItServerException)
        exception.error != null
        exception.error.error == 'Malformed or invalid uri parameter'
        exception.error.timestamp != null
        exception.statusCode == 400
        exception.statusMessage == 'Bad request'
    }

    def "I can see I get an error for an invalid api token"() {
        given:
        SkimIt invalidSkimIt = SkimIt.make("invalidKey")

        when:
        invalidSkimIt.extraction("https://en.wikipedia.org/wiki/Fake_news")

        then:
        SkimItServerException exception = thrown(SkimItServerException)
        exception.statusCode == 403
        exception.statusMessage == 'Forbidden'
        exception.error != null
        exception.error.error == 'Forbidden'
        exception.error.timestamp != null
    }
}
