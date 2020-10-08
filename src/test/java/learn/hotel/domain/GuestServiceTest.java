package learn.hotel.domain;

import learn.hotel.data.DataException;
import learn.hotel.data.GuestRepositoryDouble;
import learn.hotel.data.HostRepositoryDouble;
import learn.hotel.models.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    GuestService service = new GuestService(new GuestRepositoryDouble());

    @Test
    void shouldFindByEmail() {
        Guest guest = service.findByEmail("ceciliabarkume@gmail.com");
        assertEquals("Cecilia",guest.getFirstName());
    }
    @Test
    void shouldNotFindByInvalidEmail() {
        Guest guest = service.findByEmail("BUB@BUB.NET");
        assertNull(guest);
    }
    @Test
    void shouldNotFindByNullEmail() {
        Guest guest = service.findByEmail(null);
        assertNull(guest);
    }

    @Test
    void shouldFindByFullName() {
        Guest guest = service.findByName("Cecilia", "Barkume");
        assertEquals("ceciliabarkume@gmail.com",guest.getEmail());

        guest = service.findByName(null, "barkume");
        assertEquals("ceciliabarkume@gmail.com",guest.getEmail());

        guest = service.findByName("", "barkume");
        assertEquals("ceciliabarkume@gmail.com",guest.getEmail());
    }
    @Test
    void shouldNotFindByInvalidName() {
        Guest guest = service.findByName("Dunko", "Barkume");
        assertNull(guest);

        guest = service.findByName("Cecilia", "Yup");
        assertNull(guest);

        guest = service.findByName("Cecilia", "");
        assertNull(guest);
    }
    @Test
    void shouldNotFindByNullName() {
        Guest guest = service.findByName("Cecilia", null);
        assertNull(guest);

        guest = service.findByName(null, null);
        assertNull(guest);
    }

    @Test
    void shouldAdd() throws DataException {
        Result<Guest> guest = service.add(new Guest(2, "Testo", "Testy", "testotesty@test.com", "(555) 5555555", State.LA));
        assertEquals(0, guest.getErrorMessages().size());

        guest = service.add(new Guest(-100, " ", " ", "@.", "(555) 5555555", State.LA));
        assertEquals(0, guest.getErrorMessages().size());
    }
    @Test
    void shouldNotAddInvalid() throws DataException {
        Result<Guest> guest = service.add(new Guest(2, "Testo", "Testy", "testotesty@test.com", " 555) 5555555", State.LA));
        assertTrue(guest.getErrorMessages().size() > 0);

        guest = service.add(new Guest(2, "Testo", "Testy", "testotesty@test.com", "(555  5555555", State.LA));
        assertTrue(guest.getErrorMessages().size() > 0);

        guest = service.add(new Guest(2, "Testo", "Testy", "testotesty@test.com", "(555)_5555555", State.LA));
        assertTrue(guest.getErrorMessages().size() > 0);

        guest = service.add(new Guest(2, "Testo", "Testy", "testotesty@test.com", "(555) 555", State.LA));
        assertTrue(guest.getErrorMessages().size() > 0);

        guest = service.add(new Guest(2, "Testo", "Testy", "testotesty(at)test.com", "(555) 5555555", State.LA));
        assertTrue(guest.getErrorMessages().size() > 0);

        guest = service.add(new Guest(2, "Testo", "Testy", "testotesty@test(dot)com", "(555) 5555555", State.LA));
        assertTrue(guest.getErrorMessages().size() > 0);
    }
    @Test
    void shouldNotAddNull() throws DataException {
        Result<Guest> guest = service.add(new Guest(2, null, "Testy", "testotesty@test.com", "(555) 5555555", State.LA));
        assertTrue(guest.getErrorMessages().size() > 0);

        guest = service.add(new Guest(2, "Testo", null, "testotesty@test.com", "(555) 5555555", State.LA));
        assertTrue(guest.getErrorMessages().size() > 0);

        guest = service.add(new Guest(2, "Testo", "Testy", null, "(555) 5555555", State.LA));
        assertTrue(guest.getErrorMessages().size() > 0);

        guest = service.add(new Guest(2, "Testo", "Testy", "testotesty@test.com", null, State.LA));
        assertTrue(guest.getErrorMessages().size() > 0);

        guest = service.add(new Guest(2, "Testo", "Testy", "testotesty@test.com", "(555) 5555555", null));
        assertTrue(guest.getErrorMessages().size() > 0);

        guest = service.add(null);
        assertTrue(guest.getErrorMessages().size() > 0);
    }

    @Test
    void shouldEdit() throws DataException {
        Result<Guest> result = service.edit(new Guest(1, "Cecilia", "Barkume", "cbarkume@dev-10.com", null, null));
        assertEquals(0, result.getErrorMessages().size());
        assertEquals("cbarkume@dev-10.com", result.getPayload().getEmail());
        assertEquals("(734) 5079310", result.getPayload().getPhone());
        assertEquals(State.MI, result.getPayload().getState());

        result = service.edit(new Guest(-30, "Cecilia", "Barkume", null, null, null));
        assertEquals(0, result.getErrorMessages().size());

        result = service.edit(new Guest(1, "Cecilia", "Barkume", "cbarkume@dev-10.com", "(555) 5555555", State.CT));
        assertEquals(0, result.getErrorMessages().size());
        assertEquals("cbarkume@dev-10.com", result.getPayload().getEmail());
        assertEquals("(555) 5555555", result.getPayload().getPhone());
        assertEquals(State.CT, result.getPayload().getState());
    }
    @Test
    void shouldNotEditInvalid() throws DataException {
        Result<Guest> result = service.edit(new Guest(1, "", "", "cbarkume@dev-10.com", null, null));
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.edit(new Guest(1, "Cecilia", "Barkume", "cbarkume(at)dev-10.com", null, null));
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.edit(new Guest(1, "Cecilia", "Barkume", null, "7345079310", null));
        assertTrue(result.getErrorMessages().size() > 0);
    }

    @Test
    void shouldDeleteByFullName() throws DataException {
        Result<Guest> result = service.deleteByName("", "Barkume");
        assertEquals(0, result.getErrorMessages().size());
    }
    @Test
    void shouldNotDeleteByInvalidNames() throws DataException {
        Result<Guest> result = service.deleteByName("Cecilia", "");
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.deleteByName("Cecilia", "Gumdrop");
        assertTrue(result.getErrorMessages().size() > 0);
    }
    @Test
    void shouldNotDeleteByNullNames() throws DataException {
        Result<Guest> result = service.deleteByName(null, null);
        assertTrue(result.getErrorMessages().size() > 0);
    }

    @Test
    void shouldDeleteByEmail() throws DataException {
        Result<Guest> result = service.deleteByEmail("ceciliabarkume@gmail.com");
        assertEquals(0, result.getErrorMessages().size());
    }
    @Test
    void shouldNotDeleteByInvalidEmail() throws DataException {
        Result<Guest> result = service.deleteByEmail("FAKEEMAIL");
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.deleteByEmail("");
        assertTrue(result.getErrorMessages().size() > 0);
    }
    @Test
    void shouldNotDeleteByNullEmail() throws DataException {
        Result<Guest> result = service.deleteByEmail(null);
        assertTrue(result.getErrorMessages().size() > 0);
    }
}