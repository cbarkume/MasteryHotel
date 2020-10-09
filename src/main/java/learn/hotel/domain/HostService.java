package learn.hotel.domain;

import learn.hotel.data.HostRepository;
import learn.hotel.models.Guest;
import learn.hotel.models.Host;

import java.util.ArrayList;
import java.util.List;

public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findAll() {
        return repository.findAll();
    }

    public Host findByEmail(String email) {
        if (email == null) {
            return null;
        }

        List<Host> all = findAll();
        for (Host h : all) {
            if (h.getEmail().equalsIgnoreCase(email)) {
                return h;
            }
        }
        return null;
    }

    public Host findByLastName(String lastName) {
        if (lastName == null) {
            return null;
        }

        List<Host> all = findAll();
        for (Host h : all) {
            if (h.getLastName().equalsIgnoreCase(lastName)) {
                return h;
            }
        }
        return null;
    }

    public List<Host> findByLastNamePrefix(String lastName) {
        if (lastName == null) {
            return null;
        }

        List<Host> all = findAll();
        List<Host> matches = new ArrayList<>();

        for (Host h : all) {
            if (h.getLastName().startsWith(lastName)) {
                matches.add(h);
            }
        }
        if (matches.size() == 0) {
            return null;
        }
        return matches;
    }
}
