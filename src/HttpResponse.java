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
        return version + " " + responseCode + " " + message;
    }
}
