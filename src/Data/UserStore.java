package Data;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Loads / saves users from a plain-text CSV file  data/users.txt .
 * Each line:  asuId,password,role,suspended
 */
public final class UserStore {

    private static final Path FILE = Paths.get("data", "users.txt");
    private static final String HEADER =
            "# asuId,password,role,suspended (auto-generated)\n";

    /** Ensures the data directory + file exist. */
    private static void ensureFile() throws IOException {
        if (!Files.exists(FILE.getParent())) Files.createDirectories(FILE.getParent());
        if (!Files.exists(FILE)) Files.write(FILE, HEADER.getBytes());
    }

    /** Reads all users from disk. */
    public static Map<String,User> load() throws IOException {
        ensureFile();
        Map<String,User> map = new HashMap<>();
        for (String line : Files.readAllLines(FILE)) {
            if (line.isBlank() || line.startsWith("#")) continue;
            User u = User.fromCsv(line.trim());
            map.put(u.getAsuId(), u);
        }
        return map;
    }

    /** Overwrites the file with the current set. */
    public static void save(Collection<User> users) throws IOException {
        ensureFile();
        List<String> lines = new ArrayList<>();
        lines.add(HEADER.trim());
        users.stream().map(User::toCsv).forEach(lines::add);
        Files.write(FILE, lines);
    }

    /** Atomic add – returns true if the id was free, false if duplicate. */
    public static boolean addUser(User u) throws IOException {
        Map<String,User> all = load();
        if (all.containsKey(u.getAsuId())) return false;
        all.put(u.getAsuId(), u);
        save(all.values());
        return true;
    }

    private UserStore() {}   // static-only utility
}
