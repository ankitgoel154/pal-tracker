package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
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

    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository,MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<>(timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);
    }

    @GetMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long timeEntryId) {
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        if(null != timeEntry){
            actionCounter.increment();
            httpStatus = httpStatus.OK;
        }
        return new ResponseEntity<>(timeEntry, httpStatus);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return new ResponseEntity<>(new ArrayList<>(timeEntryRepository.list()), HttpStatus.OK);
    }

    @PutMapping("/{timeEntryId}")
    public ResponseEntity update(@PathVariable Long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry timeEntry = timeEntryRepository.update(timeEntryId,expected);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        if(null != timeEntry){
            actionCounter.increment();
            httpStatus = httpStatus.OK;
        }
        return new ResponseEntity<>(timeEntry, httpStatus);
    }

    @DeleteMapping("/{timeEntryId}")
    public ResponseEntity delete(@PathVariable  Long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
}
