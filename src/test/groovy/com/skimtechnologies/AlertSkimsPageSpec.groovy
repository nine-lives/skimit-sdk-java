package com.skimtechnologies

import com.fasterxml.jackson.databind.ObjectMapper
import com.skimtechnologies.util.ObjectMapperFactory
import spock.lang.Specification

class AlertSkimsPageSpec extends Specification {

    private ObjectMapper mapper = ObjectMapperFactory.make();

    def "I can covert a JSON payload to the entity"() {
        given:
        String payload = '''
            {
              "data": [
                {
                  "alert_id": "582ef22be7c78401008f8aab",
                  "created_at": "2016-11-18 13:10:52.593000",
                  "id": "582efddce7c78401008f8b02",
                  "uri": "http://www.vg247.com/2016/11/18/overwatch-lucio-meets-voice-cast-animated/"
                }
              ],
              "pagination": {
                "limit": 3,
                "start_time": 1479474652,
                "end_time": 1479474652,
                "next": "/alerts/582ef22be7c78401008f8aab?since=1479474652&limit=3",
                "total": 104
              }
            }
       '''

        when:
        AlertSkimsPage entity = mapper.readValue(payload, AlertSkimsPage);

        then:
        entity.data.size() == 1
        entity.data[0].id == '582efddce7c78401008f8b02'
        entity.data[0].createdAt == '2016-11-18 13:10:52.593000'
        entity.data[0].alertId == '582ef22be7c78401008f8aab'
        entity.data[0].uri == 'http://www.vg247.com/2016/11/18/overwatch-lucio-meets-voice-cast-animated/'

        entity.pagination.limit == 3
        entity.pagination.startTime == 1479474652
        entity.pagination.endTime == 1479474652
        entity.pagination.next == '/alerts/582ef22be7c78401008f8aab?since=1479474652&limit=3'
        entity.pagination.total == 104
    }

    def "I can create the next request from the pagination response"() {
        given:
        String payload = '''
            {
              "data": [
                {
                  "alert_id": "582ef22be7c78401008f8aab",
                  "created_at": "2016-11-18 13:10:52.593000",
                  "id": "582efddce7c78401008f8b02",
                  "uri": "http://www.vg247.com/2016/11/18/overwatch-lucio-meets-voice-cast-animated/"
                }
              ],
              "pagination": {
                "limit": 3,
                "start_time": 1479474652,
                "end_time": 1479474652,
                "next": "/alerts/582ef22be7c78401008f8aab?since=1479474652&limit=3",
                "total": 104
              }
            }
        '''
        AlertSkimsPage page = mapper.readValue(payload, AlertSkimsPage);

        when:
        AlertSkimsRequest request = AlertSkimsRequest.forNextPage(page)

        then:
        request.limit == 3
        request.since == 1479474652
        request.topics == null
    }
}
