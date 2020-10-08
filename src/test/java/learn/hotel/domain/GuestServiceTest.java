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
    void ShouldFindByEmail() {
        Guest guest = service.findByEmail("ceciliabarkume@gmail.com");
        assertEquals("Cecilia",guest.getFirstName());
    }
    @Test
    void ShouldNotFindByInvalidEmail() {

    }
    @Test
    void ShouldNotFindByNullEmail() {
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
    }
    @Test
    void shouldNotAddInvalid() throws DataException {
    }
    @Test
    void shouldNotAddNull() throws DataException {
    }

    @Test
    void shouldEdit() throws DataException {
        Result<Guest> result = service.edit(new Guest(1, "Cecilia", "Barkume", "cbarkume@dev-10.com", null, null));
        System.out.println();
        assertEquals(0, result.getErrorMessages().size());
        assertEquals("cbarkume@dev-10.com", result.getPayload().getEmail());
        assertEquals("(734) 5079310", result.getPayload().getPhone());
        assertEquals(State.MI, result.getPayload().getState());
    }
    @Test
    void shouldNotEditInvalid() throws DataException {
    }
    @Test
    void shouldNotEditNull() throws DataException {
    }

    @Test
    void shouldDeleteByFullName() throws DataException {

    }
    @Test
    void shouldNotDeleteByInvalidNames() throws DataException {
    }
    @Test
    void shouldNotDeleteByNullNames() throws DataException {
    }

    @Test
    void shouldDeleteByHostEmailGuestEmail() {
    }
    @Test
    void shouldNotDeleteByInvalidEmails() {
    }
    @Test
    void shouldNotDeleteByNullEmails() {
    }
}