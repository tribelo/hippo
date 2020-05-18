package uk.nhs.digital.apispecs.config;

public class ApigeeConfig {

    private String specUrl;
    private String tokenUrl;
    private String username;
    private String password;
    private String basicToken;
    private String otpKey;
    private String domain;

    public ApigeeConfig(String specUrl, String tokenUrl, String username, String password, String basicToken, String otpKey, String domain) {
        this.specUrl = specUrl;
        this.tokenUrl = tokenUrl;
        this.username = username;
        this.password = password;
        this.basicToken = basicToken;
        this.otpKey = otpKey;
        this.domain = domain;
    }

    public String getSpecUrl() {
        return specUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBasicToken() {
        return basicToken;
    }

    public String getOtpKey() {
        return otpKey;
    }

    public String getDomain() {
        return domain;
    }
}
