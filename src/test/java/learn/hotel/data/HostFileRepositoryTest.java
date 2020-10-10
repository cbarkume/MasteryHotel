package learn.hotel.data;

import learn.hotel.models.Host;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    HostFileRepository repository = new HostFileRepository("./data/hosts.csv");

    @Test
    void shouldFindById() {
        Host host = repository.findById("5da0a6a2-946a-41ff-bb16-0debc85f78c8");
        List<Host> hosts = repository.findAll();
        assertTrue(hosts.contains(host));
        assertEquals("5da0a6a2-946a-41ff-bb16-0debc85f78c8", host.getId());
    }

    @Test
    void shouldNotFindByInvalidId() {
        Host host = repository.findById("PEFOIJFGH)HE(W$(GBR");
        assertNull(host);
    }

    @Test
    void shouldNotFindByNullId() {
        Host host = repository.findById(null);
        assertNull(host);
    }
}