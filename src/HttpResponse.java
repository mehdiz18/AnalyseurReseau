public class HttpResponse implements Http {
    private String version;
    private int responseCode;
    private String message;

    HttpResponse(String version, int responseCode, String message) {
        this.version = version;
        this.responseCode = responseCode;
        this.message = message;
    }

    @Override
    public String toString() {
        String ANSI_PURPLE = "\u001B[45m";
        String ANSI_RESET = "\u001B[0m";
        return ANSI_PURPLE + "HTTP Response" + ANSI_RESET + " Version: " + version + " ; Code Reponse: " + responseCode
                + " ; Message: " + message;
    }
}
