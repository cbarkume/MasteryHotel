package learn.hotel.data;

import learn.hotel.models.Guest;
import learn.hotel.models.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    static final String SEED_PATH = "./data/guest-seed.csv";
    static final String TEST_PATH = "./data/guest-test.csv";
    static final int NEXT_ID = 1001;

    GuestFileRepository repository = new GuestFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        assertTrue(repository.findAll().size() == NEXT_ID - 1);
    }

    @Test
    void shouldFindById() {
        Guest guest = repository.findById(60);
        assertTrue(repository.findAll().contains(guest));
        assertEquals(60, guest.getGuestId());
        assertEquals("Pendleberry", guest.getLastName());
    }

    @Test
    void shouldNotFindByInvalidId() {
        Guest guest = repository.findById(32804598);
        assertNull(guest);
    }

    @Test
    void shouldNotFindByNegativeId() {
        Guest guest = repository.findById(-1);
        assertNull(guest);
    }

    @Test
    void shouldAdd() throws DataException {
        Guest guest = new Guest(NEXT_ID, "Cecilia", "Barkume", "ceciliabarkume@gmail.com", "(734) 5079310", State.MI);
        Guest resultGuest = repository.add(guest);
        assertEquals(resultGuest, guest);
        assertEquals( "Barkume", guest.getLastName());
        List<Guest> all = repository.findAll();
        assertTrue(all.contains(guest));
    }

    @Test
    void shouldNotAddNull() throws DataException {
        Guest resultGuest = repository.add(null);
        assertNull(resultGuest);
        assertEquals(1000, repository.findAll().size());
    }

    @Test
    void shouldEdit() throws DataException {
        Guest guest = repository.findById(60);
        guest.setFirstName("Testy");
        guest.setLastName("McTesterson");
        boolean success = repository.edit(guest);
        assertTrue(success);
        assertEquals(1000, repository.findAll().size());
        Guest resultGuest = repository.findById(60);
        assertEquals(guest,resultGuest);
    }

    @Test
    void shouldNotEditNull() throws DataException {
        Guest guest = null;
        boolean success = repository.edit(guest);
        assertFalse(success);
        assertEquals(1000, repository.findAll().size());
    }

    @Test
    void shouldDelete() throws DataException {
        Guest guest = repository.findById(60);
        Boolean result = repository.deleteById(guest.getGuestId());
        assertTrue(result);
        assertFalse(repository.findAll().contains(guest));
        assertEquals(999, repository.findAll().size());
    }

    @Test
    void shouldNotDeleteForInvalidId() throws DataException {
        Boolean result = repository.deleteById(-100);
        assertFalse(result);
        assertEquals(1000, repository.findAll().size());
    }
}