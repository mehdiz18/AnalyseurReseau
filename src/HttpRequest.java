public class HttpRequest implements Http {
    private String methode;
    private String url;
    private String version;
    private String host;

    public HttpRequest(String methode, String url, String version, String host) {
        this.methode = methode;
        this.url = url;
        this.version = version;
        this.host = host;
    }

    public String getMethode() {
        return this.methode;
    }

    public String getUrl() {
        return this.url;
    }

    public String getVersion() {
        return this.version;
    }

    @Override
    public String toString() {
        return methode + " " + url + " " + version + " " + host;
    }
}
