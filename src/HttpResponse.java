public class HttpResponse implements Http {
    private String version;
    private String responseCode;

    HttpResponse(String version, String responseCode) {
        this.version = version;
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        String ANSI_PURPLE = "\u001B[45m";
        String ANSI_RESET = "\u001B[0m";
        return ANSI_PURPLE + "HTTP Response" + ANSI_RESET + " Version: " + version + " ; Code Reponse: " + responseCode;
    }
}
