package learn.hotel.data;

import learn.hotel.models.Host;
import learn.hotel.models.State;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostFileRepository implements HostRepository {

    private final String filePath;

    public HostFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Host> findAll() {
        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Host findById(String id) {
        return findAll().stream()
                .filter(h -> h.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    private Host deserialize(String[] fields) {
        Host host = new Host();
        host.setId(fields[0]);
        host.setLastName(fields[1]);
        host.setEmail(fields[2]);
        host.setPhone(fields[3]);
        host.setAddress(fields[4]);
        host.setCity(fields[5]);
        host.setState(State.valueOf(fields[6]));
        host.setPostalCode(fields[7]);
        host.setStandardRate(new BigDecimal(fields[8]));
        host.setWeekendRate((new BigDecimal(fields[9])));

        return host;
    }


}
