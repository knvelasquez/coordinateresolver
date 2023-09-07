package model;

import java.time.LocalDateTime;

public class SecurityKey {
    private final String company;
    private final String privateKey;
    private final Integer shipId;
    private final LocalDateTime created;
    private final LocalDateTime expire;

    public SecurityKey(String company, String privateKey, Integer shipId, LocalDateTime created, LocalDateTime expire) {
        this.company = company;
        this.privateKey = privateKey;
        this.shipId = shipId;
        this.created = created;
        this.expire = expire;
    }

    public String getCompany() {
        return company;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public Integer getShipId() {
        return shipId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getExpire() {
        return expire;
    }
}
