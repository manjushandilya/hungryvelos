package com.velokofi.hungryvelos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

@Document
public class AuthorizedClient {

    @Id
    private String principalName;

    private byte[] bytes;

    public static OAuth2AuthorizedClient fromBytes(final byte[] bytes) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            final ObjectInputStream ois = new ObjectInputStream(bis);
            final OAuth2AuthorizedClient client = (OAuth2AuthorizedClient) ois.readObject();
            return client;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toBytes(final OAuth2AuthorizedClient client) {
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream objectOut = new ObjectOutputStream(bos);
            objectOut.writeObject(client);
            objectOut.close();
            return bos.toByteArray();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorizedClient client = (AuthorizedClient) o;
        return this.getPrincipalName() == client.getPrincipalName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(principalName);
    }

}
