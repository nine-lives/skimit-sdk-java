package com.skimtechnologies

import com.fasterxml.jackson.databind.ObjectMapper
import com.skimtechnologies.util.ObjectMapperFactory
import spock.lang.Specification

class ExtractionSpec extends Specification {

    private ObjectMapper mapper = ObjectMapperFactory.make();

    def "I can covert a JSON payload to the entity"() {
        given:
        String payload = '''
                 {
                    "text": "Fake news is a neologism used to refer to non-satirical news stories, which have originated online...",
                    "language": "en",
                    "meta_keywords": "None",
                    "markup": {
                        "tag": "p",
                        "children": {
                            "tag": "p",
                            "children": [
                                {
                                    "tag": "i",
                                    "children": [
                                        "Fake news"
                                    ]
                                },
                                " is a ",
                                {
                                    "tag": "a",
                                    "children": [
                                        "neologism"
                                    ],
                                    "href": "https://en.wikipedia.org/wiki/Neologism"
                                }
                            ]
                        }
                    },
                    "images": [
                        "https://upload.wikimedia.org/wikipedia/commons/1/1c/Graphic_on_Fake_News_by_VOA.jpg"
                    ],
                    "page_type": "listicle",
                    "uri": "https://en.wikipedia.org/wiki/Fake_news",
                    "videos": [],
                    "favicon": "https://en.wikipedia.org/static/favicon/wikipedia.ico",
                    "date": "2016-12-13",
                    "title": "Fake news",
                    "meta_description": "description"
                 }
       '''

        when:
        Extraction entity = mapper.readValue(payload, Extraction);

        then:
        with(entity) {
            text == 'Fake news is a neologism used to refer to non-satirical news stories, which have originated online...'
            language == 'en'
            metaKeywords == 'None'
            markup instanceof Map
            images.size() == 1
            images[0] == 'https://upload.wikimedia.org/wikipedia/commons/1/1c/Graphic_on_Fake_News_by_VOA.jpg'
            pageType == 'listicle'
            uri == 'https://en.wikipedia.org/wiki/Fake_news'
            videos.size() == 0
            favicon == 'https://en.wikipedia.org/static/favicon/wikipedia.ico'
            date == '2016-12-13'
            title == 'Fake news'
            metaDescription == 'description'
        }
    }
}
