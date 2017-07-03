package com.skimtechnologies

class SkimIntegrationSpec extends BaseIntegrationSpec {

    def "I can see the data from the server is correct"() {
        when:
        Skim skim = skimIt.skim('https://en.wikipedia.org/wiki/Fake_news');

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

    def "I get an error when I try to call the server with no uri"() {
        when:
        skimIt.skim(null);

        then:
        SkimItServerException exception = thrown(SkimItServerException)
        exception.error != null
        exception.error.error == 'Parameter "uri" is required'
        exception.error.timestamp != null
        exception.statusCode == 400
        exception.statusMessage == 'Bad request'
    }

    def "I get an error when I try to call the server with an invalid uri"() {
        when:
        skimIt.skim("this-is-not-valid");

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
        invalidSkimIt.skim("https://en.wikipedia.org/wiki/Fake_news")

        then:
        SkimItServerException exception = thrown(SkimItServerException)
        exception.statusCode == 403
        exception.statusMessage == 'Forbidden'
        exception.error != null
        exception.error.error == 'Forbidden'
        exception.error.timestamp != null
    }
}
