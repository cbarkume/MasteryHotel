package learn.hotel.domain;

import learn.hotel.data.DataException;
import learn.hotel.data.GuestRepository;
import learn.hotel.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findAll() {
        return repository.findAll();
    }

    public Guest findByEmail (String email) {
        List<Guest> all = findAll();

        if (email == null) {
            return null;
        }

        for (Guest g : all) {
            if (g.getEmail().equalsIgnoreCase(email)) {
                return g;
            }
        }
        return null;
    }

    public Guest findByName (String firstName, String lastName) {
        List<Guest> all = findAll();

        if (lastName == null || firstName == null) {
            return null;
        }

        for (Guest g : all) {
            if (g.getLastName().equalsIgnoreCase(lastName) && g.getFirstName().equalsIgnoreCase(firstName)) {
                return g;
            }
        }
        return null;
    }

    public Result<Guest> add(Guest guest) throws DataException {
        Result<Guest> result = validate(guest, false);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(repository.add(guest));

        return result;
    }

    public Result<Guest> edit(Guest guest) throws DataException {
        Result<Guest> result = validate(guest, true);
        if (!result.isSuccess()) {
            return result;
        }

        if (!repository.edit(guest)) {
            result.addErrorMessage("Unable to edit.");
        }
        return result;
    }

    public Result<Guest> deleteByName(String firstName, String lastName) throws DataException {
        Result<Guest> result = new Result<>();

        if (firstName == null || lastName == null) {
            result.addErrorMessage("Invalid Names.");
            return result;
        }

        Guest guest = findByName(firstName, lastName);

        if (guest == null) {
            result.addErrorMessage("Name not found. Check your spelling.");
            return result;
        }


        if (!repository.deleteById(guest.getGuestId())) {
            result.addErrorMessage("Error: Already entered guest has invalid ID.");
        }
        return result;
    }

    private Result<Guest> validate(Guest guest, boolean canBeDuplicate) {
        Result<Guest> result = new Result<>();
        result = validateNulls(guest, result);
        if (!result.isSuccess()){
            return result;
        }

        result = validateFields(guest, result);
        if (!result.isSuccess()){
            return result;
        }

        if (!canBeDuplicate) {
            result = validateNotDuplicate(guest, result);
        }

        return result;
    }

    private Result<Guest> validateNulls(Guest guest, Result<Guest> result) {
        if (guest == null) {
            result.addErrorMessage("Guest cannot be null.");
            return result;
        }

        if (guest.getFirstName() == null) {
            result.addErrorMessage("First name cannot be null.");
        }

        if (guest.getLastName() == null) {
            result.addErrorMessage("Last name cannot be null.");
        }

        if (guest.getEmail() == null) {
            result.addErrorMessage("Email cannot be null.");
        }

        if (guest.getPhone() == null) {
            result.addErrorMessage("Phone Number cannot be null.");
        }

        if (guest.getState() == null) {
            result.addErrorMessage("State cannot be null.");
        }
        return result;
    }

    private Result<Guest> validateFields(Guest guest, Result<Guest> result) {
        if (guest.getPhone().length() != 13
                || guest.getPhone().charAt(0) != '('
                || guest.getPhone().charAt(4) != ')'
                || guest.getPhone().charAt(5) != ' ') {
            result.addErrorMessage("Phone number be in format: (###) #######.");
            return result;
        }

        char[] validNumbers = {'0','1','2','3','4','5','6','7','8','9'};
        for (int i = 0; i < guest.getPhone().length(); i++) {
            boolean found = false;
            for (char c : validNumbers) {
                if (guest.getPhone().charAt(i) == c) {
                    found = true;
                }
            }
            if (!found) {
                result.addErrorMessage("Phone number be in format: (###) #######.");
                return result;
            }
        }
        return result;

    }

    private Result<Guest> validateNotDuplicate(Guest guest, Result<Guest> result) {
        List<Guest> all = findAll();

        for (Guest g : all) {
            if (guest.getLastName().equals(g.getLastName())
                    && guest.getFirstName().equals(g.getFirstName())) {
                result.addErrorMessage("Guest already exists. To update this guest's information, use 'Edit'.");
                return result;
            }
        }

        return result;
    }
}
