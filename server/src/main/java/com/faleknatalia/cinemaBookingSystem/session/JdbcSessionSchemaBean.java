package com.faleknatalia.cinemaBookingSystem.session;

import org.h2.jdbc.JdbcSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class JdbcSessionSchemaBean {
    private static final Logger logger = LoggerFactory.getLogger(JdbcSessionSchemaBean.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${cinema.jdbc.session.create-drop-enabled}")
    private boolean createDropEnabled;

    @Value("${cinema.jdbc.session.create-schema-path}")
    private Resource createSchemaPath;

    @Value("${cinema.jdbc.session.drop-schema-path:#{null}}")
    private Resource dropSchemaPath;

    @PostConstruct
    public void init() throws IOException {
        validate();
        if (createDropEnabled) {
            String dropSchema = StreamUtils.copyToString(dropSchemaPath.getInputStream(), Charset.defaultCharset());
            jdbcTemplate.execute(dropSchema);
            logger.info("Sql executed: \n" + dropSchema);
        }
        String createSchema = StreamUtils.copyToString(createSchemaPath.getInputStream(), Charset.defaultCharset());
        try {
            jdbcTemplate.execute(createSchema);
            logger.info("Sql executed: \n" + createSchema);
        } catch (Exception e) {
            if (!((JdbcSQLException) e.getCause()).getOriginalMessage().equals("Table \"SPRING_SESSION\" already exists")) {
                logger.error("Error during session schema initialization", e);
            }
        }
    }

    private void validate() {
        if (createDropEnabled && dropSchemaPath == null) {
            throw new IllegalArgumentException("dropSchemaPath cannot be empty on createDropEnabled enabled");
        }
    }

}