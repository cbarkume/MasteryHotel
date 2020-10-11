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
        if (email == null) {
            return null;
        }

        List<Guest> all = findAll();

        for (Guest g : all) {
            if (g.getEmail().equalsIgnoreCase(email)) {
                return g;
            }
        }
        return null;
    }

    public Guest findByName (String firstName, String lastName) {
        List<Guest> all = findAll();
        List<Guest> matches = new ArrayList<>();

        if (lastName == null) {
            return null;
        }

        for (Guest g : all) {
            if (g.getLastName().equalsIgnoreCase(lastName)) {
                matches.add(g);
            }
        }
        if (matches.size() == 1 && (firstName == null || firstName.isBlank() || firstName.isEmpty())) {
            return matches.get(0);
        }

        if (firstName == null) {
            return null;
        }
        for (Guest g : all) {
            if (g.getLastName().equalsIgnoreCase(lastName) && g.getFirstName().equalsIgnoreCase(firstName)) {
                return g;
            }
        }
        return null;
    }

    public List<Guest> findByLastNamePrefix(String lastName) {
        if (lastName == null) {
            return null;
        }

        String firstLetter = lastName.substring(0,1);
        firstLetter = firstLetter.toUpperCase();
        String name = firstLetter;

        if (lastName.length() > 1) {
            String nameRemainder = lastName.substring(1);
            nameRemainder = nameRemainder.toLowerCase();
            name += nameRemainder;
        }

        List<Guest> all = findAll();
        List<Guest> matches = new ArrayList<>();

        for (Guest g : all) {
            if (g.getLastName().startsWith(name)) {
                matches.add(g);
            }
        }
        return matches;
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
        Result<Guest> result = new Result<>();
        result = validateNulls(guest, result, true);
        if (!result.isSuccess()) {
            return result;
        }
        guest.setGuestId(findByName(guest.getFirstName(), guest.getLastName()).getGuestId());

        if (guest.getEmail() == null || guest.getEmail().isEmpty() || guest.getEmail().isBlank()) {
            guest.setEmail(findByName(guest.getFirstName(), guest.getLastName()).getEmail());
        }
        if (guest.getPhone() == null || guest.getPhone().isEmpty() || guest.getPhone().isBlank()) {
            guest.setPhone(findByName(guest.getFirstName(), guest.getLastName()).getPhone());
        }
        if (guest.getState() == null) {
            guest.setState(findByName(guest.getFirstName(), guest.getLastName()).getState());
        }

        result = validate(guest, true);
        if (!result.isSuccess()) {
            return result;
        }

        if (!repository.edit(guest)) {
            result.addErrorMessage("Unable to edit.");
        }
        result.setPayload(guest);
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
        result.setPayload(guest);
        return result;
    }

    public Result<Guest> deleteByEmail(String email) throws DataException {
        Result<Guest> result = new Result<>();

        Guest guest = findByEmail(email);

        if (guest == null) {
            result.addErrorMessage("Email not found. A proper email format is 'example@yahoo.com'.");
            return result;
        }


        if (!repository.deleteById(guest.getGuestId())) {
            result.addErrorMessage("Error: Previously entered guest has invalid ID.");
        }
        result.setPayload(guest);
        return result;
    }

    private Result<Guest> validate(Guest guest, boolean isEdit) {
        Result<Guest> result = new Result<>();
        result = validateNulls(guest, result, isEdit);
        if (!result.isSuccess()){
            return result;
        }

        result = validatePhone(guest, result);
        if (!result.isSuccess()){
            return result;
        }

        result = validateEmail(guest, result);
        if (!result.isSuccess()){
            return result;
        }

        result = validateNotDuplicate(guest, result, isEdit);
        return result;
    }

    private Result<Guest> validateNulls(Guest guest, Result<Guest> result, boolean isEdit) {
        if (guest == null) {
            result.addErrorMessage("Guest cannot be null.");
            return result;
        }

        if (guest.getFirstName() == null || guest.getFirstName().isBlank() || guest.getFirstName().isEmpty()) {
            result.addErrorMessage("First name cannot be null.");
        }

        if (guest.getLastName() == null || guest.getLastName().isBlank() || guest.getLastName().isEmpty()) {
            result.addErrorMessage("Last name cannot be null.");
        }

        if(isEdit) {
            return result;
        }

        if (guest.getEmail() == null || guest.getEmail().isBlank() || guest.getEmail().isEmpty()) {
            result.addErrorMessage("Email cannot be null.");
        }

        if (guest.getPhone() == null || guest.getPhone().isBlank() || guest.getPhone().isEmpty()) {
            result.addErrorMessage("Phone Number cannot be null.");
        }

        if (guest.getState() == null) {
            result.addErrorMessage("State cannot be null.");
        }
        return result;
    }

    private Result<Guest> validatePhone(Guest guest, Result<Guest> result) {
        if (guest.getPhone().length() != 13
                || guest.getPhone().charAt(0) != '('
                || guest.getPhone().charAt(4) != ')'
                || guest.getPhone().charAt(5) != ' ') {
            result.addErrorMessage("Phone number must be in format: (###) #######.");
            return result;
        }

        char[] validNumbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (int i = 1; i < 13; i++) {
            if (i == 4) {
                i += 2;
            }
            boolean found = false;
            for (char c : validNumbers) {
                if (guest.getPhone().charAt(i) == c) {
                    found = true;
                }
            }
            if (!found) {
                result.addErrorMessage("Phone number must be in format: (###) #######.");
                return result;
            }
        }
        return result;
    }

    private Result<Guest> validateEmail(Guest guest, Result<Guest> result) {

        if ((!guest.getEmail().contains("@")) || (!guest.getEmail().contains("."))) {
            result.addErrorMessage("Invalid email. A proper email format is 'example@yahoo.com'.");
        }
        return result;
    }

    private Result<Guest> validateNotDuplicate(Guest guest, Result<Guest> result, boolean isEdit) {
        List<Guest> all = findAll();

        for (Guest g : all) {
            if (guest.getLastName().equalsIgnoreCase(g.getLastName())
                    && guest.getFirstName().equalsIgnoreCase(g.getFirstName())) {
                if (!isEdit) {
                    result.addErrorMessage("Guest already exists. To update this guest's information, use 'Edit'.");
                }
                return result;
            }
        }

        if (isEdit) {
            result.addErrorMessage("Entered Guest does not exist. ");
        }
        return result;
    }
}
