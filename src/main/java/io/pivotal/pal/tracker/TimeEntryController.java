package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        return new ResponseEntity<>(timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);
    }

    @GetMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long timeEntryId) {
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        if(null != timeEntry){
            httpStatus = httpStatus.OK;
        }
        return new ResponseEntity<>(timeEntry, httpStatus);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<>(new ArrayList<>(timeEntryRepository.list()), HttpStatus.OK);
    }

    @PutMapping("/{timeEntryId}")
    public ResponseEntity update(@PathVariable Long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry timeEntry = timeEntryRepository.update(timeEntryId,expected);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        if(null != timeEntry){
            httpStatus = httpStatus.OK;
        }
        return new ResponseEntity<>(timeEntry, httpStatus);
    }

    @DeleteMapping("/{timeEntryId}")
    public ResponseEntity delete(@PathVariable  Long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
}
