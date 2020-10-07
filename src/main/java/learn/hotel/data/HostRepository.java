package learn.hotel.data;

import learn.hotel.models.Host;

import java.util.List;

public interface HostRepository {
    List<Host> findAll();

    Host findById(String id);
}
