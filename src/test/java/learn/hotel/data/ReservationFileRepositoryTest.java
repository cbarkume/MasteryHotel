package learn.hotel.data;

import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;
import learn.hotel.models.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    static final String SEED_PATH = "./data/reservation_test/reservation-seed.csv";
    static final String TEST_PATH = "./data/reservation_test/reservation-test.csv";
    static final String TEST_DIR_PATH = "./data/reservation_test";
    static final int RESERVATION_COUNT = 16;

    GuestFileRepository guestRepo = new GuestFileRepository("./data/guests.csv");
    HostFileRepository hostRepo = new HostFileRepository("./data/hosts.csv");
    ReservationFileRepository reservationRepo = new ReservationFileRepository(TEST_DIR_PATH, guestRepo, hostRepo);

    Guest testGuest = guestRepo.findById(133);
    Host testHost = new Host("reservation-test","McTesterson","testy@test.com","(555) 5555555","48187 Test Road","Teston", State.MI,"21231",new BigDecimal(10),new BigDecimal(20));
    Reservation reservation = new Reservation(RESERVATION_COUNT + 1, LocalDate.of(2020,10,6), LocalDate.of(2020,10,14), testHost, testGuest);

    @BeforeEach
    void setup() throws IOException {
        reservation.setTotal(reservation.calculateTotal());
        testGuest.setGuestId(133);
        testHost.setId("reservation-test");
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindByHostGuest() {
        Reservation reservation = reservationRepo.findByHostIdGuestId(testHost.getId(),testGuest.getGuestId());
        assertTrue(reservation.getId() == 1);
    }

    @Test
    void shouldNotFindWhenInvalidHost() {
        testHost.setId("bsuhfou");
        Reservation reservation = reservationRepo.findByHostIdGuestId(testHost.getId(),testGuest.getGuestId());
        assertNull(reservation);
    }

    @Test
    void shouldNotFindWhenInvalidGuest() {
        testGuest.setGuestId(23904);
        Reservation reservation = reservationRepo.findByHostIdGuestId(testHost.getId(),testGuest.getGuestId());
        assertNull(reservation);
    }

    @Test
    void shouldNotFindWhenBothInvalid() {
        testHost.setId("bsuhfou");
        testGuest.setGuestId(23904);
        Reservation reservation = reservationRepo.findByHostIdGuestId(testHost.getId(),testGuest.getGuestId());
        assertNull(reservation);
    }

    @Test
    void shouldNotFindWhenHostNull() {
        Reservation reservation = reservationRepo.findByHostIdGuestId(null,testGuest.getGuestId());
        assertNull(reservation);
    }

    @Test
    void shouldNotFindWhenGuestNull() {
        Reservation reservation = reservationRepo.findByHostIdGuestId(testHost.getId(),-15);
        assertNull(reservation);
    }

    @Test
    void shouldNotFindWhenBothNull() {
        Reservation reservation = reservationRepo.findByHostIdGuestId(null,-15);
        assertNull(reservation);
    }

    @Test
    void shouldFindByHost() {
        testHost.setId("reservation-test");
        List<Reservation> reservations = reservationRepo.findByHostId(testHost.getId());
        assertEquals(16, reservations.size());
    }

    @Test
    void shouldNotFindForInvalidHost() {
        testHost.setId("bsuhfou");
        List<Reservation> reservations = reservationRepo.findByHostId(testHost.getId());
        assertEquals(0, reservations.size());
    }

    @Test
    void shouldNotFindForNullHost() {
        List<Reservation> reservations = reservationRepo.findByHostId(null);
        assertEquals(0,reservations.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation reservationResult = reservationRepo.add(reservation);
        assertEquals(reservationResult,reservation);
        assertEquals(17, reservationRepo.findByHostId(testHost.getId()).size());
    }

    @Test
    void shouldNotAddNullReservation() throws DataException {
        Reservation reservationResult = reservationRepo.add(null);
        assertNull(reservationResult);
        assertEquals(16, reservationRepo.findByHostId(testHost.getId()).size());
    }

    @Test
    void shouldEdit() throws DataException {
        reservationRepo.add(reservation);
        reservation.setEndDate(LocalDate.of(2020,11,1));
        boolean success = reservationRepo.edit(reservation);
        assertTrue(success);
        assertEquals(17, reservationRepo.findByHostId(testHost.getId()).size());
    }

    @Test
    void shouldNotEditNullReservation() throws DataException {
        boolean success = reservationRepo.edit(null);
        assertFalse(success);
        assertEquals(16, reservationRepo.findByHostId(testHost.getId()).size());
    }

    @Test
    void shouldCancel() throws DataException {
        Reservation reservationResult = reservationRepo.cancelByHostIdGuestId(reservation.getHost().getId(), reservation.getGuest().getGuestId());
        assertEquals(1, reservationResult.getId());
    }

    @Test
    void shouldNotCancelNullReservation() throws DataException {
        Reservation reservationResult = reservationRepo.cancelByHostIdGuestId(null, -15);
        assertNull(reservationResult);
        assertEquals(16, reservationRepo.findByHostId(testHost.getId()).size());
    }
}