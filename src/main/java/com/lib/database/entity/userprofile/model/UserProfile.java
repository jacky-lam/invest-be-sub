package com.lib.database.entity.userprofile.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database model of the table
 */
public class UserProfile {

    public static final String schemaName = "dbo";
    public static final String tableName = "user_profile";

    private final long id;
    private final String firstName;
    private final String lastName;
    private final String fullName;
    private final String emailAddress;
    private final String loginName;
    private final String baseCurrencyCode;

    public UserProfile(long id, String firstName, String lastName, String fullName,
                       String emailAddress, String loginName, String baseCurrencyCode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.loginName = loginName;
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public static UserProfile createInstanceFromTableRow(ResultSet rs) throws SQLException {
        return new UserProfile(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("full_name"),
                rs.getString("email_address"),
                rs.getString("login_name"),
                rs.getString("base_currency_code")
        );
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    // TODO: override `equals` & `hashCode`

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", loginName='" + loginName + '\'' +
                ", baseCurrencyCode='" + baseCurrencyCode + '\'' +
                '}';
    }
}
