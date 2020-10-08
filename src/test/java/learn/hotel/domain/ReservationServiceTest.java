package learn.hotel.domain;

import learn.hotel.data.GuestRepositoryDouble;
import learn.hotel.data.HostRepositoryDouble;
import learn.hotel.data.ReservationRepositoryDouble;
import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service = new ReservationService(
            new GuestRepositoryDouble(),
            new HostRepositoryDouble(),
            new ReservationRepositoryDouble(),
            new GuestService(new GuestRepositoryDouble()),
            new HostService(new HostRepositoryDouble()));

    @Test
    void ShouldFindByHostEmailGuestEmail() {

    }
    @Test
    void ShouldNotFindByInvalidEmail() {

    }
    @Test
    void ShouldNotFindByNullEmail() {
    }

    @Test
    void shouldFindByHostLastNameGuestFullName() {
    }
    @Test
    void shouldNotFindByInvalidName() {
    }
    @Test
    void shouldNotFindByNullName() {
    }

    @Test
    void shouldFindByHostEmail() {
    }
    @Test
    void shouldNotFindByInvalidHostEmail() {
    }
    @Test
    void shouldNotFindByNullHostEmail() {
    }

    @Test
    void shouldFindByHostLastName() {
    }
    @Test
    void shouldNotFindByInvalidHostLastName() {
    }
    @Test
    void shouldNotFindByNullHostLastName() {
    }

    @Test
    void shouldAdd() {
    }
    @Test
    void shouldNotAddInvalid() {
    }
    @Test
    void shouldNotAddNull() {
    }

    @Test
    void shouldEdit() {
    }
    @Test
    void shouldNotEditInvalid() {
    }
    @Test
    void shouldNotEditNull() {
    }

    @Test
    void shouldCancelByHostLastNameGuestFullName() {
    }
    @Test
    void shouldNotCancelByInvalidNames() {
    }
    @Test
    void shouldNotCancelByNullNames() {
    }

    @Test
    void shouldCancelByHostEmailGuestEmail() {
    }
    @Test
    void shouldNotCancelByInvalidEmails() {
    }
    @Test
    void shouldNotCancelByNullEmails() {
    }

}