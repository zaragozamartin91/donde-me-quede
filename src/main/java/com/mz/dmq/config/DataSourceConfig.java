package com.mz.dmq.config;

import com.mz.dmq.util.CmdRunner;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Quintet;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Profile({"prod", "postgres"})
@Configuration
@Slf4j
public class DataSourceConfig {

    @Bean
    @Profile("prod")
    public DataSource getDataSource(@Value("${DATABASE_URL}") String databaseUrl) {
        // url is like postgres://dondemequede:d0nd3m3qu3d3@192.168.99.102:5432/dmq
        return buildDatasourceFromUri(databaseUrl);
    }

    /**
     * Datasource bean for manual testing using postgres and DockerToolbox
     * @return Postgres datasource
     */
    @Bean
    @Profile("postgres")
    public DataSource postgresDataSource() {
        log.info("Fetching postgres ip from 'docker-machine ip' command");
        String databaseUrl = Optional.of(new CmdRunner(List.of("cmd", "/c", "docker-machine", "ip")))
                .map(CmdRunner::runSync)
                .filter(r -> r.getValue0() == 0)
                .map(Triplet::getValue1)
                .map(String::strip)
                .map(s -> "jdbc:postgresql://dondemequede:d0nd3m3qu3d3@" + s + ":5432/dmq")
                .orElseThrow(() -> new IllegalStateException("Error al obtener docker machine ip"));
        return buildDatasourceFromUri(databaseUrl);
    }

    private DataSource buildDatasourceFromUri(String databaseUrl) {
        // url is like postgres://dondemequede:d0nd3m3qu3d3@192.168.99.102:5432/dmq
        Pattern pattern = Pattern.compile("[\\w\\-._:]+://(\\w+):(\\w+)@([\\w\\-._]+):(\\d+)/(\\w+)");
        Quintet<String, String, String, String, String> conf = Optional.of(databaseUrl)
                .map(pattern::matcher)
                .filter(Matcher::find)
                .map(m -> Quintet.fromCollection(List.of(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5))))
                .orElseThrow(() -> new IllegalStateException("Error al configurar conexion con BBDD"));

        // expected jdbc:postgresql://${DB_HOST:192.168.99.102}:${DB_PORT:5432}/${DB_NAME:dmq}
        String username = conf.getValue0();
        String password = conf.getValue1();
        String url = String.format("jdbc:postgresql://%s:%s/%s", conf.getValue2(), conf.getValue3(), conf.getValue4());

        return DataSourceBuilder
                .create()
                .username(username)
                .password(password)
                .url(url)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
