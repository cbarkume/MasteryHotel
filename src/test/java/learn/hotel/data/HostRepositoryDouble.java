package learn.hotel.data;

import learn.hotel.models.Host;

import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private final ArrayList<Host> hosts = new ArrayList<>();

    @Override
    public List<Host> findAll() {
        return hosts;
    }

    @Override
    public Host findById(String id) {
        return findAll().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
