package learn.hotel.data;

import learn.hotel.models.Host;
import learn.hotel.models.State;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private final ArrayList<Host> hosts = new ArrayList<>();
    private Host HOST = new Host("TestID", "TestNAME", "Test@EMAIL.com", "(555) 5555555", "TestADDRESS",
            "TestCITY", State.DE, "TestPOSTAL", new BigDecimal(10.00), new BigDecimal(15.00));

    public HostRepositoryDouble() {
        hosts.add(HOST);
    }

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
