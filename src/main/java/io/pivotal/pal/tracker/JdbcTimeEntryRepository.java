package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry t) {

        //jdbcTemplate.execute();

        SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("time_entries")
                .usingColumns("project_id",
                              "user_id",
                              "date",
                              "hours"
                ).usingGeneratedKeyColumns("id");

        Map<String, Object> timeEntryParameters = new HashMap<>();
        timeEntryParameters.put("project_id", t.getProjectId());
        timeEntryParameters.put("user_id", t.getUserId());
        timeEntryParameters.put("date", t.getDate());
        timeEntryParameters.put("hours", t.getHours());

        Number id = simpleInsert.executeAndReturnKey(timeEntryParameters);

        t.setId((long) id.intValue());
        return t;
    }

    @Override
    public TimeEntry find(long id) {
        try {
            Map<String, Object> foundEntry = jdbcTemplate.queryForMap("Select * from time_entries where id = ?", id);
            System.out.println(foundEntry);
            LocalDate localDate = LocalDate.parse(foundEntry.get("date").toString());
            TimeEntry timeEntry = new TimeEntry(
                    (long) foundEntry.get("id"),
                    (long) foundEntry.get("project_id"),
                    (long) foundEntry.get("user_id"),
                    localDate,
                    (int) foundEntry.get("hours")
            );
            return timeEntry;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> timeEntryList = new ArrayList<>();
        jdbcTemplate.query("Select * from time_entries", result -> {
            TimeEntry timeEntry = new TimeEntry(
                    result.getLong("id"),
                    result.getLong("project_id"),
                    result.getLong("user_id"),
                    result.getDate("date").toLocalDate(),
                    result.getInt("hours")
            );
            timeEntryList.add(timeEntry);
        });

        return timeEntryList;
    }

    @Override
    public TimeEntry update(long id, TimeEntry t) {
        jdbcTemplate.update("Update time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id = ?",
                            t.getProjectId(), t.getUserId(), t.getDate(), t.getHours(), id);
//        return ;
        t.setId(id);
        return t;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("Delete from time_entries where id = ?", id);

    }
}
