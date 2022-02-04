package com.kari.sdds.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kari.sdds.entity.Ms700;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class Ms700Repository {

    @Value("${spring.influx2.bucket}")
    String bucket;

    @Autowired
    private InfluxDBClient influx;

    public void save(final Ms700 entity) {
        final WriteApi writeApi = influx.getWriteApi();
        writeApi.writeMeasurement(WritePrecision.US, entity);
    }

    public void save(final List<Ms700> entities) {
        final WriteApi writeApi = influx.getWriteApi();
        writeApi.writeMeasurements(WritePrecision.US, entities);
    }

    public List<Ms700> findBytype(String type) {
        List<Ms700> entities = new ArrayList<>();
        String query = "from(bucket:" + "\"" + bucket + "\")"
                + "|> range(start: 0)"
                + "|> filter(fn:(r) => r[\"_measurement\"] == " + "\"Ms700\")"
                + "|> filter(fn:(r) => r[\"type\"] == " + "\"" + type + "\")"
                + "|> sort(columns: [\"_time\"])";
        final QueryApi queryApi = influx.getQueryApi();
        final List<FluxTable> tables = queryApi.query(query);
        for (final FluxTable fluxTable : tables) {
            final List<FluxRecord> records = fluxTable.getRecords();
            for (final FluxRecord fluxRecord : records) {
                Ms700 entity = new Ms700();
                entity.setTime(fluxRecord.getTime());
                entity.setValue(Double.parseDouble(fluxRecord.getValueByKey("_value").toString()));
                entities.add(entity);
            }
        }
        return entities;
    }
}
