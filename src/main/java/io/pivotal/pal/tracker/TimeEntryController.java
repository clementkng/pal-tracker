package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TimeEntryController {
    TimeEntryRepository timeEntryRepository;
    public TimeEntryController(TimeEntryRepository t) {
        this.timeEntryRepository = t;
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry t) {
        TimeEntry thing = this.timeEntryRepository.create(t);
        return new ResponseEntity<>(thing, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") Long id) {
        TimeEntry thing= this.timeEntryRepository.find(id);
        if (thing != null) {
            return new ResponseEntity<>(thing, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/time-entries", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<>(this.timeEntryRepository.list(), HttpStatus.OK);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody TimeEntry timeEntry) {
        TimeEntry thing = this.timeEntryRepository.update(id, timeEntry);
        if (thing != null) return new ResponseEntity(thing, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("id") Long id) {
        this.timeEntryRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}