public class TestUtils {
    public static String onlyFailures(String details) {
        if (details == null) return "No details available";
        String[] lines = details.split("\\r?\\n");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.contains("[FAIL]")) {
                sb.append(line.trim()).append(System.lineSeparator());
                // append subsequent indented/context lines
                int j = i + 1;
                while (j < lines.length) {
                    String nxt = lines[j];
                    if (nxt.trim().isEmpty()) {
                        j++; continue;
                    }
                    if (nxt.startsWith(" ") || nxt.startsWith("\t") || nxt.startsWith("    ")) {
                        sb.append(nxt.trim()).append(System.lineSeparator());
                        j++; continue;
                    }
                    break;
                }
            }
        }

        String out = sb.toString().trim();
        return out.isEmpty() ? details.trim() : out;
    }
}
