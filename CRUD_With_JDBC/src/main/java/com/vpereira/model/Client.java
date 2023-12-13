package com.vpereira.model;

import com.vpereira.annotation.Column;
import com.vpereira.annotation.Id;
import com.vpereira.annotation.Table;

import java.util.Objects;

@Table("Client")
public class Client implements Entity{

    @Id
    private Long id;
    @Column(value = "first_name", notNull = true, length = 20)
    private String firstName;
    @Column(value = "last_name", notNull = true, length = 20)
    private String lastName;
    @Column(value = "email", notNull = true, length = 40)
    private String email;
    @Column(value = "gender", length = 10)
    private String gender;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(email, client.email) && Objects.equals(gender, client.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, gender);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
