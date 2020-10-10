package learn.hotel.domain;

import learn.hotel.data.DataException;
import learn.hotel.data.GuestRepositoryDouble;
import learn.hotel.data.HostRepositoryDouble;
import learn.hotel.data.ReservationRepositoryDouble;
import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;
import learn.hotel.models.State;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service = new ReservationService(
            new GuestRepositoryDouble(),
            new HostRepositoryDouble(),
            new ReservationRepositoryDouble(),
            new GuestService(new GuestRepositoryDouble()),
            new HostService(new HostRepositoryDouble()));

    private Host testHost = new Host("TestID", "TestNAME", "Test@EMAIL.com", "(555) 5555555", "TestADDRESS",
            "TestCITY", State.DE, "TestPOSTAL", new BigDecimal(10.00), new BigDecimal(15.00));
    private Guest testGuest = new Guest(3, "John", "Banana", "jnans@gmail.com", "(444) 4444444", State.FL);

    private Host oldHost = new Host("TestID", "TestNAME", "Test@EMAIL.com", "(555) 5555555", "TestADDRESS",
            "TestCITY", State.DE, "TestPOSTAL", new BigDecimal(10.00), new BigDecimal(15.00));
    private Guest oldGuest = new Guest(1, "Cecilia", "Barkume", "ceciliabarkume@gmail.com", "(734) 5079310", State.MI);

    @Test
    void ShouldFindByHostEmailGuestEmail() {
        Reservation reservation = service.findByHostEmailGuestEmail("Test@EMAIL.com","ceciliabarkume@gmail.com");
        assertEquals("Cecilia",reservation.getGuest().getFirstName());
    }
    @Test
    void ShouldNotFindByInvalidEmail() {
        Reservation reservation = service.findByHostEmailGuestEmail(" ","ceciliabarkume@gmail.com");
        assertNull(reservation);

        reservation = service.findByHostEmailGuestEmail("Test@EMAIL.com"," ");
        assertNull(reservation);

        reservation = service.findByHostEmailGuestEmail("zrgrgzdgf@.","gragz@.rfsge");
        assertNull(reservation);
    }

    @Test
    void shouldFindByHostLastNameGuestFullName() {
        Reservation reservation = service.findByHostLastNameGuestFullName("TestNAME","Cecilia", "Barkume");
        assertEquals("ceciliabarkume@gmail.com",reservation.getGuest().getEmail());

        reservation = service.findByHostLastNameGuestFullName("TestNAME"," ", "Barkume");
        assertEquals("ceciliabarkume@gmail.com",reservation.getGuest().getEmail());
    }
    @Test
    void shouldNotFindByInvalidName() {
        Reservation reservation = service.findByHostLastNameGuestFullName(" ","Cecilia", "Barkume");
        assertNull(reservation);

        reservation = service.findByHostLastNameGuestFullName("TestNAME","Barkume", " ");
        assertNull(reservation);

        reservation = service.findByHostLastNameGuestFullName("fdgdd","vvdf", "vsdfljfbs");
        assertNull(reservation);
    }

    @Test
    void shouldFindByHostEmail() {
        List<Reservation> reservations = service.findByHostEmail("Test@EMAIL.com");
        assertEquals(1, reservations.size());
        assertEquals("TestNAME",reservations.get(0).getHost().getLastName());
    }
    @Test
    void shouldNotFindByInvalidHostEmail() {
        List<Reservation> reservations = service.findByHostEmail("Test@s.com");
        assertEquals(0,reservations.size());

        reservations = service.findByHostEmail(" ");
        assertEquals(0,reservations.size());

        reservations = service.findByHostEmail(null);
        assertEquals(0,reservations.size());
    }

    @Test
    void shouldFindByHostLastName() {
        List<Reservation> reservations = service.findByHostLastName("TestNAME");
        assertEquals(1, reservations.size());
        assertEquals("Barkume",reservations.get(0).getGuest().getLastName());
    }
    @Test
    void shouldNotFindByInvalidHostLastName() {
        List<Reservation> reservations = service.findByHostLastName("sdgsddgsd");
        assertEquals(0,reservations.size());

        reservations = service.findByHostLastName(" ");
        assertEquals(0,reservations.size());

        reservations = service.findByHostLastName(null);
        assertEquals(0,reservations.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Result<Reservation> result = service.add(new Reservation(2, LocalDate.of(2021,11,7), LocalDate.of(2021,11,15), testHost, testGuest));
        assertEquals(0, result.getErrorMessages().size());
    }
    @Test
    void shouldNotAddInvalid() throws DataException {
        Result<Reservation> result = service.add(new Reservation(2, LocalDate.of(2021,11,16), LocalDate.of(2021,11,15), testHost, testGuest));
        assertTrue(result.getErrorMessages().size() > 0);
    }
    @Test
    void shouldNotAddNull() throws DataException {
        Result<Reservation> result = service.add(new Reservation(2, LocalDate.of(2021,11,7), LocalDate.of(2021,11,15), null, testGuest));
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.add(new Reservation(2, LocalDate.of(2021,11,7), LocalDate.of(2021,11,15), testHost, null));
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.add(new Reservation(2, LocalDate.of(2021,11,7), LocalDate.of(2021,11,15), null, null));
        assertTrue(result.getErrorMessages().size() > 0);
    }

    @Test
    void shouldEdit() throws DataException {
        Result<Reservation> result = service.edit(new Reservation(1, LocalDate.of(2021,11,11), LocalDate.of(2021,11,15), oldHost, oldGuest));
        assertEquals(0, result.getErrorMessages().size());

        result = service.edit(new Reservation(1, null, LocalDate.of(2021,11,15), oldHost, oldGuest));
        assertEquals(0, result.getErrorMessages().size());

        result = service.edit(new Reservation(1, LocalDate.of(2020,10,5), null, oldHost, oldGuest));
        assertEquals(0, result.getErrorMessages().size());

        result = service.edit(new Reservation(1, null, null, oldHost, oldGuest));
        assertEquals(0, result.getErrorMessages().size());
    }
    @Test
    void shouldNotEditInvalid() throws DataException {
        Result<Reservation> result = service.edit(new Reservation(1, LocalDate.of(2021,11,15), LocalDate.of(2021,11,15), oldHost, oldGuest));
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.edit(new Reservation(1, LocalDate.of(2023,11,15), LocalDate.of(2021,11,15), oldHost, oldGuest));
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.edit(new Reservation(1, LocalDate.of(2021,11,14), LocalDate.of(2021,11,15), null, oldGuest));
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.edit(new Reservation(1, LocalDate.of(2021,11,14), LocalDate.of(2021,11,15), oldHost, null));
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.edit(new Reservation(1, LocalDate.of(2021,11,14), LocalDate.of(2021,11,15), null, null));
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.edit(new Reservation(1, null, LocalDate.of(1990,11,15), oldHost, oldGuest));
        assertTrue(result.getErrorMessages().size() > 0);

        result = service.edit(new Reservation(1, LocalDate.of(2028,11,14), null, oldHost, oldGuest));
        assertTrue(result.getErrorMessages().size() > 0);
    }

    @Test
    void shouldCancelByHostLastNameGuestFullName() throws DataException {
        Result<Reservation> result = service.cancelByHostLastNameGuestFullName("TestNAME", "Cecilia", "Barkume");
        assertEquals(0, result.getErrorMessages().size());
    }
    @Test
    void shouldNotCancelByInvalidNames() throws DataException {
        Result<Reservation> result = service.cancelByHostLastNameGuestFullName("drgh", "sgrg", "srgs");
        assertTrue(result.getErrorMessages().size() > 0);
    }
    @Test
    void shouldNotCancelByNullNames() throws DataException {
        Result<Reservation> result = service.cancelByHostLastNameGuestFullName(null, null, null);
        assertTrue(result.getErrorMessages().size() > 0);
    }

    @Test
    void shouldCancelByHostEmailGuestEmail() throws DataException {
        Result<Reservation> result = service.cancelByHostEmailGuestEmail("Test@email.com", "ceciliabarkume@gmail.com");
        assertEquals(0, result.getErrorMessages().size());
    }
    @Test
    void shouldNotCancelByInvalidEmails() throws DataException {
        Result<Reservation> result = service.cancelByHostEmailGuestEmail("SO@.FIH", "AFU@.BA");
        assertTrue(result.getErrorMessages().size() > 0);
    }
    @Test
    void shouldNotCancelByNullEmails() throws DataException {
        Result<Reservation> result = service.cancelByHostEmailGuestEmail(null, null);
        assertTrue(result.getErrorMessages().size() > 0);
    }

}