package com.skimtechnologies

import spock.lang.Unroll

class AlertsIntegrationSpec extends BaseIntegrationSpec {

    def "I can create, list and delete alerts from the server"() {
        when:
        Alert alert = skimIt.alertCreate(new AlertCreateRequest().withKeyTerms(["fake news"] as Set).withTitle("[INT_TEST] Fake News"));

        then:
        alert.title == '[INT_TEST] Fake News'
        alert.id.length() > 0

        when:
        List<Alert> alerts = skimIt.alerts();
        Alert found = alerts.find({ it.id == alert.id})

        then:
        found != null
        found.title == '[INT_TEST] Fake News'

        when:
        boolean deleted = skimIt.alertDelete(alert.id);

        then:
        deleted

        when:
        alerts = skimIt.alerts();
        Alert notfound = alerts.find({ it.id == alert.id})

        then:
        notfound == null
    }

    def "I get an error when I try to set up an alert with the same keywords"() {
        given:
        Alert alert = skimIt.alertCreate(new AlertCreateRequest().withKeyTerms(["skim it duplicate"] as Set).withTitle("[INT_TEST] Duplicate Test"));

        when:
        skimIt.alertCreate(new AlertCreateRequest().withKeyTerms(["skim it duplicate"] as Set).withTitle("[INT_TEST] Duplicate Test"));

        then:
        SkimItServerException e = thrown(SkimItServerException)
        e.error != null
        e.error.error.contains('already has an alert with similar key terms')
        e.error.timestamp != null
        e.statusCode == 400
        e.statusMessage == 'Bad request'

        cleanup:
        skimIt.alertDelete(alert.id)
    }

    @Unroll("I get an error when I try to create an alert with missing parameters - #test")
    def "I get an error when I try to create an alert with missing parameters"() {
        when:
        skimIt.alertCreate(new AlertCreateRequest().withKeyTerms(keyTerms as Set).withTitle(title));

        then:
        SkimItServerException e = thrown(SkimItServerException)
        e.error != null
        e.error.error.contains(error)
        e.error.timestamp != null
        e.statusCode == 400
        e.statusMessage == 'Bad request'

        where:
        test | title | keyTerms | error
        "no values" | null | null | 'Parameter "key_terms" is required'
        "no key terms null" | '[INT_TEST] Title' | null | 'Parameter "key_terms" is required'
        "no key terms empty" | '[INT_TEST] Title' | [] | 'Parameter "key_terms" is required'
        "no title null" | null | ['terms1xxx', 'terms2xxx'] | 'Parameter "title" is required'
        "no title empty" | '' | ['terms1yyy', 'terms2yyy'] | 'Parameter "title" is required'
    }

    @Unroll("I get an error when I try to delete an alert with invalid parameters - #test")
    def "I get an error when I try to delete an alert with invalid parameters "() {
        when:
        skimIt.alertDelete(alertId);

        then:
        SkimItServerException e = thrown(SkimItServerException)
        e.error != null
        e.error.error.contains(error)
        e.error.timestamp != null
        e.statusCode == 400
        e.statusMessage == 'Bad request'

        where:
        test | alertId |  error
        "null alert id" | null | 'is not a valid ObjectId, it must be a 12-byte input or a 24-character hex string'
        "empty alert id" | '' | 'Parameter "key_terms" is required'
        "invalid alert id" | 'invalid' | 'is not a valid ObjectId, it must be a 12-byte input or a 24-character hex string'
        "missing alert id" | '1234567890abcde123456789' | 'does not exist'
    }

    @Unroll("I get an error when I try to update feedback for an alert skim with invalid parameters - #test")
    def "I get an error when I try to update feedback for an alert skim with invalid parameters"() {
        when:
        skimIt.alertFeedback(alertId, skimId, value);

        then:
        SkimItServerException e = thrown(SkimItServerException)
        e.error != null
        e.error.error.contains(error)
        e.error.timestamp != null
        e.statusCode == 400
        e.statusMessage == 'Bad request'

        where:
        test | alertId | skimId | value | error
        "all null" | null | null | AlertFeedbackValue.RELEVANT | 'is not a valid ObjectId, it must be a 12-byte input or a 24-character hex string'
        "all empty" | '' | '' | AlertFeedbackValue.IRRELEVANT | 'is not a valid ObjectId, it must be a 12-byte input or a 24-character hex string'
        "alert id null" | null | '1234' | AlertFeedbackValue.RELEVANT | 'is not a valid ObjectId, it must be a 12-byte input or a 24-character hex string'
        "alert id empty" | '' | '1234' | AlertFeedbackValue.IRRELEVANT | 'is not a valid ObjectId, it must be a 12-byte input or a 24-character hex string'
        "alert id invalid" | 'invalid' | '' | AlertFeedbackValue.RELEVANT | 'is not a valid ObjectId, it must be a 12-byte input or a 24-character hex string'
        "skim id null" | '5953c84f1985920900958f24' | null | AlertFeedbackValue.IRRELEVANT | 'is not a valid ObjectId, it must be a 12-byte input or a 24-character hex string'
        "skim id empty" | '5953c84f1985920900958f24' | '' | AlertFeedbackValue.RELEVANT | 'is not a valid ObjectId, it must be a 12-byte input or a 24-character hex string'
        "skim id invalid" | '5953c84f1985920900958f24' | 'invalid' | AlertFeedbackValue.IRRELEVANT | 'is not a valid ObjectId, it must be a 12-byte input or a 24-character hex string'
    }

    @Unroll("I can see I get an error for an invalid api token - #call")
    def "I can see I get an error for an invalid api token when calling alerts"() {
        given:
        SkimIt invalidSkimIt = SkimIt.make("invalidKey")

        when:
        method(invalidSkimIt);

        then:
        SkimItServerException exception = thrown(SkimItServerException)
        exception.statusCode == 403
        exception.statusMessage == 'Forbidden'
        exception.error != null
        exception.error.error == 'Forbidden'
        exception.error.timestamp != null

        where:
        call | method
        'get alerts' | { it.alerts() }
        'create alert' | { it.alertCreate(new AlertCreateRequest().withKeyTerms(["term"] as Set).withTitle("[INT_TEST] Invalid")) }
        'delete alert' | { it.alertDelete("12345") }
        'alert skims' | { it.alertSkims("12345", new AlertSkimsRequest()) }
        'alert feedback' | { it.alertFeedback("12345", "6789", AlertFeedbackValue.RELEVANT) }
    }


    def setupSpec() {
        cleanupAll()
    }

    def cleanupSpec() {
        cleanupAll()
    }

    def cleanupAll() {
        for (Alert alert : skimIt.alerts()) {
            if (alert.title.contains("[INT_TEST]")) {
                skimIt.alertDelete(alert.id)
            }
        }
    }

}
