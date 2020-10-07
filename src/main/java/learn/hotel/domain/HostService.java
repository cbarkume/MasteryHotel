package learn.hotel.domain;

import learn.hotel.data.HostRepository;
import learn.hotel.models.Guest;
import learn.hotel.models.Host;

import java.util.List;

public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findAll() {
        return repository.findAll();
    }
}
