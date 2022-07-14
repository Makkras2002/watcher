package com.currency.watcher.entity;

public class RegisteredUser {
    private String name;
    private CryptoCurrency cryptoCurrency;

    public RegisteredUser() {
    }

    public RegisteredUser(String name, CryptoCurrency cryptoCurrency) {
        this.name = name;
        this.cryptoCurrency = cryptoCurrency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CryptoCurrency getCryptoCurrency() {
        return cryptoCurrency;
    }

    public void setCryptoCurrency(CryptoCurrency cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisteredUser that = (RegisteredUser) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return cryptoCurrency != null ? cryptoCurrency.equals(that.cryptoCurrency) : that.cryptoCurrency == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (cryptoCurrency != null ? cryptoCurrency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RegisteredUser{");
        sb.append("name='").append(name).append('\'');
        sb.append(", cryptoCurrency=").append(cryptoCurrency);
        sb.append('}');
        return sb.toString();
    }
}
