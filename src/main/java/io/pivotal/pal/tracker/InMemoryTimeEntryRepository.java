package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private Map<Long,TimeEntry> idTimeEntryMap = new HashMap<>();
    private Long sequence = 0L;



    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(++sequence);
        idTimeEntryMap.put(sequence,timeEntry);
        return timeEntry;
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(idTimeEntryMap.values());
    }

    public TimeEntry find(long id) {
        return idTimeEntryMap.get(id);
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        if(!idTimeEntryMap.containsKey(id)){
            return null;
        }
        timeEntry.setId(id);
        idTimeEntryMap.put(id,timeEntry);
        return timeEntry;
    }


    public void delete(long id) {
        idTimeEntryMap.remove(id);
    }

    public Map<Long, TimeEntry> getIdTimeEntryMap() {
        return idTimeEntryMap;
    }

    public void setIdTimeEntryMap(Map<Long, TimeEntry> idTimeEntryMap) {
        this.idTimeEntryMap = idTimeEntryMap;
    }
}
