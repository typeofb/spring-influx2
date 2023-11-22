package com.kari.sdds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileReader;
import java.time.Instant;
import java.util.*;

import com.kari.sdds.entity.Ms700;
import com.kari.sdds.repository.Ms700Repository;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DemoApplicationTests {

    @Autowired
    Ms700Repository Ms700Repository;

    private Ms700 Ms700Entity;
    private List<Ms700> Ms700Entities;

    @BeforeAll
    public void setUp() throws Exception {

        Random rand = new Random();
        String[] type = {
            "t_hmp_Avg",
            "Temp_C_Max",
            "WinSpd_WVc",
            "BP_Avg",
            "LineQ1_Avg"
        };

        Ms700Entity = Ms700.builder()
                .time(Instant.now())
                .type("WinSpd_WVc")
                .value(rand.nextDouble() * 10.0)
                .build();

        Ms700Entities = new ArrayList<>();
        for (int i = 0; i < 100; i ++) {
            Ms700 entity = Ms700.builder()
                    .time(Instant.now())
                    .type(type[i % 5])
                    .value((rand.nextDouble() + 0.001) * 100.0)
                    .build();
            Ms700Entities.add(entity);
        }
    }

	@Test
	public void saveMs700Entity() throws Exception {
        Ms700Repository.save(Ms700Entity);
        List<Ms700> entities = Ms700Repository.findByType(Ms700Entity.getType());
        for (Ms700 entity : entities) {
            assertNotEquals(0, entity.getValue());
        }
    }

    @Test
	public void saveMs700Entities() throws Exception {
        Ms700Repository.save(Ms700Entities);
        List<Ms700> entities = Ms700Repository.findByType("WinSpd_WVc");
        for (Ms700 entity : entities) {
            assertNotEquals(0, entity.getValue());
        }
    }

    @Test
    public void readMs700Entities() throws Exception {
        List<Ms700> entities = Ms700Repository.findByType("t_hmp_Avg");
        for (Ms700 entity : entities) {
            assertNotEquals(0, entity.getValue());
        }
    }

    @Test
    public void readCsvAndSaveMs700Entities() throws Exception {
        String[] type = {
                "t_hmp_Avg",
                "Temp_C_Max",
                "WinSpd_WVc",
                "BP_Avg",
                "LineQ1_Avg"
        };

        ClassPathResource resource = new ClassPathResource("/influxdb_data.csv");
        CSVReader reader = new CSVReader(new FileReader(resource.getFile()));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            Ms700Entities = new ArrayList<>();
            System.out.println("timestamp : " + nextLine[0]);

            Ms700 entity1 = Ms700.builder().time(Instant.parse(nextLine[0])).type(type[0]).value(Double.parseDouble(nextLine[1])).build();
            Ms700 entity2 = Ms700.builder().time(Instant.parse(nextLine[0])).type(type[1]).value(Double.parseDouble(nextLine[2])).build();
            Ms700 entity3 = Ms700.builder().time(Instant.parse(nextLine[0])).type(type[2]).value(Double.parseDouble(nextLine[3])).build();
            Ms700 entity4 = Ms700.builder().time(Instant.parse(nextLine[0])).type(type[3]).value(Double.parseDouble(nextLine[4])).build();
            Ms700 entity5 = Ms700.builder().time(Instant.parse(nextLine[0])).type(type[4]).value(Double.parseDouble(nextLine[5])).build();
            Ms700Entities.add(entity1);
            Ms700Entities.add(entity2);
            Ms700Entities.add(entity3);
            Ms700Entities.add(entity4);
            Ms700Entities.add(entity5);
            Ms700Repository.save(Ms700Entities);
            System.out.println();
        }
    }
}
