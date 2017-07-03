package com.skimtechnologies

import java.time.LocalDateTime
import java.time.ZoneId

class AlertsIntegrationSpec extends BaseIntegrationSpec {

    def "I can see the data from the server is correct"() {
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

        when:
        AlertSkimsPage page = skimIt.alertSkims('5953c84f1985920900958f24', new AlertSkimsRequest()
                .withLimit(5)
                .withTopics([Topic.personnel] as Set)
                .withSince((LocalDateTime.now().minusDays(7).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000) as Integer))
                ;
        //AlertSkimsPage page = skimIt.alertSkims('595657a139618600019bf735', new AlertSkimsRequest().withLimit(5));
        println "????????????????????????????????????????? " + page.pagination.total
        println "????????????????????????????????????????? " + page.pagination.next

        then:
        true

        when:
        page = skimIt.alertSkims('595657a139618600019bf735', new AlertSkimsRequest()
                .withLimit(5)
                .withTopics([] as Set)
                .withSince((LocalDateTime.now().minusDays(7).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000) as Integer));
        println "????????????????????????????????????????? " + page.pagination.total
        println "????????????????????????????????????????? " + page.pagination.next

        then:
        true
    }

    def "I get an error when I try to set up an alert with the same keywords"() {
        given:
        Alert alert = skimIt.alertCreate(new AlertCreateRequest().withKeyTerms(["skim it duplicate"] as Set).withTitle("[INT_TEST] Duplicate Test"));

        when:
        skimIt.alertCreate(new AlertCreateRequest().withKeyTerms(["skim it duplicate"] as Set).withTitle("[INT_TEST] Duplicate Test"));

        then:
        SkimItServerException e = thrown(SkimItServerException)
        e.error.error.contains('already has an alert with similar key terms')

        cleanup:
        skimIt.alertDelete(alert.id)
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
