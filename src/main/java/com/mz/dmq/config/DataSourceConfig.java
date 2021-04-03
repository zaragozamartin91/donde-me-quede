package com.mz.dmq.config;

import org.javatuples.Quintet;
import org.javatuples.Tuple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Profile("prod")
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource(@Value("${DATABASE_URL}") String databaseUrl) {
        // url is like postgres://dondemequede:d0nd3m3qu3d3@192.168.99.102:5432/dmq
        Pattern pattern = Pattern.compile("\\w+://(\\w+):(\\w+)@([\\w\\-._]+):(\\d+)/(\\w+)");
        Quintet<String, String, String, String, String> dbConfig = Optional.of(databaseUrl)
                .map(pattern::matcher)
                .filter(Matcher::find)
                .map(m -> Quintet.fromCollection(List.of(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5))))
                .orElseThrow(() -> new IllegalStateException("Error al configurar conexion con BBDD"));

        // expected jdbc:postgresql://${DB_HOST:192.168.99.102}:${DB_PORT:5432}/${DB_NAME:dmq}
        String username = dbConfig.getValue0();
        String password = dbConfig.getValue1();
        String url = String.format("jdbc:postgresql://%s:%s/%s", dbConfig.getValue2(), dbConfig.getValue3(), dbConfig.getValue4());

        return DataSourceBuilder
                .create()
                .username(username)
                .password(password)
                .url(url)
                .driverClassName("org.postgresql.Driver")
                .build();

    }
}
