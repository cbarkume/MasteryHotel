package learn.hotel.domain;

import learn.hotel.data.HostRepositoryDouble;
import learn.hotel.models.Host;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    HostService service = new HostService(new HostRepositoryDouble());

    @Test
    void shouldFindByEmail() {
        Host host = service.findByEmail("Test@EMAIL.com");
        assertNotNull(host);
        assertEquals("TestID", host.getId());
    }

    @Test
    void shouldNotFindByInvalidEmail() {
        Host host = service.findByEmail("FAKE EMAIL");
        assertNull(host);
    }

    @Test
    void shouldNotFindByNullEmail() {
        Host host = service.findByEmail(null);
        assertNull(host);
    }

    @Test
    void shouldFindByLastName() {
        Host host = service.findByLastName("TestNAME");
        assertNotNull(host);
        assertEquals("TestID", host.getId());
    }

    @Test
    void shouldNotFindByInvalidLastName() {
        Host host = service.findByLastName("Fish");
        assertNull(host);
    }

    @Test
    void shouldNotFindByNullLastName() {
        Host host = service.findByLastName(null);
        assertNull(host);
    }
}