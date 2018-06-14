package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private Map<Long, TimeEntry> myStuff;

    public InMemoryTimeEntryRepository() {
        this.myStuff = new HashMap<>();
    }

    public TimeEntry create(TimeEntry t) {
        int id = myStuff.size() + 1;
        myStuff.put((long) id, t);
        t.setId(id);
        return t;
    }

    public TimeEntry find(long id) {
        return myStuff.get(id);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(myStuff.values());
    }

    public TimeEntry update(long id, TimeEntry t) {
        TimeEntry timeEntry = new TimeEntry(id, t.getProjectId(), t.getUserId(), t.getDate(), t.getHours());
        myStuff.put(id, timeEntry);
        return timeEntry;
    }

    public void delete(long id) {
        myStuff.remove(id);
    }
}
