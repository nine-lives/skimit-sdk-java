package com.skimtechnologies

import com.fasterxml.jackson.databind.ObjectMapper
import com.skimtechnologies.util.ObjectMapperFactory
import spock.lang.Specification

class SkimSpec extends Specification {

    private ObjectMapper mapper = ObjectMapperFactory.make();

    def "I can covert a JSON payload to the entity"() {
        given:
        String payload = '''
            {
              "uri": "https://en.wikipedia.org/wiki/Fake_news",
              "reading_time_saved": 2072,
              "keywords": [
                "fake news stories make real news",
                "political fake news"
              ],
              "body": "Fake news is a type of yellow journalism ...",
              "subtopic_probabilities": {
                "partnerships": 0.6917092445,
                "acquisitions": 0.2620076051,
                "investments": 0.3057035649,
                "personnel": 0.4172696804
              },
              "html": "<div><b>Fake news</b> is a type of ...</div>",
              "type": "article",
              "title": "Fake news",
              "reading_time_original": 2108,
              "images": [
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/The_fin_de_si%C3%A8cle_newspaper_proprietor_%28cropped%29.jpg/1200px-The_fin_de_si%C3%A8cle_newspaper_proprietor_%28cropped%29.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/PolitiFact%27s_Fake_News_Almanac_.png/345px-PolitiFact%27s_Fake_News_Almanac_.png"
              ]
            }
       '''

        when:
        Skim entity = mapper.readValue(payload, Skim);

        then:
        with(entity) {
            uri == 'https://en.wikipedia.org/wiki/Fake_news'
            readingTimeSaved == 2072
            keywords.size() == 2
            keywords[0] == 'fake news stories make real news'
            body == 'Fake news is a type of yellow journalism ...'
            subtopicProbabilities.size() == 4;
            subtopicProbabilities.partnerships == 0.6917092445G
            html == '<div><b>Fake news</b> is a type of ...</div>'
            type == 'article'
            title == 'Fake news'
            readingTimeOriginal == 2108
            images.size() == 2
            images[0] == 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/The_fin_de_si%C3%A8cle_newspaper_proprietor_%28cropped%29.jpg/1200px-The_fin_de_si%C3%A8cle_newspaper_proprietor_%28cropped%29.jpg'
        }
    }
}
