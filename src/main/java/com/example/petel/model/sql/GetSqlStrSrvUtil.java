package com.example.petel.model.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * 取得SQL String (Implements)
 * @author 00588604
 *
 */
@Component
public class GetSqlStrSrvUtil {

    @CacheEvict(value = "${sqlUtils.sql.key}", allEntries = true)
    public void cleanSQLCache() {
        System.out.println("clear sql cache success.");
    }

    @Cacheable(value = "${sqlUtils.sql.key}")
    public String getSql(String sqlName) throws IOException {
        ClassPathResource resource = new ClassPathResource("sql/" + sqlName);
        InputStream dbAsStream = resource.getInputStream();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(dbAsStream))) {
            while (br.ready()) {
                sb.append(br.readLine());
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

}

