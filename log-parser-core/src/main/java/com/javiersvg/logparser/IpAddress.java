package com.javiersvg.logparser;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

class IpAddress {
    private static final Logger LOGGER = Logger.getLogger(IpAddress.class.getName());
    private static final String BLOCKED_IP_ADDRESS_KEY = "blocked.ipAddress";
    private static final String QUERY = "INSERT INTO BLOCKED_IPS (IP,OCCURRENCES,DATE,REASON) VALUES (?,?,?,?)";
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");
    private String value;
    private DataSource dataSource;

    IpAddress(String value, DataSource dataSource) {
        this.value = value;
        this.dataSource = dataSource;
    }

    public void block(LocalDateTime startDate, Duration duration, long count) {
        String reason = String.format(resourceBundle.getString(BLOCKED_IP_ADDRESS_KEY),
                count, startDate, duration);
        LOGGER.log(Level.INFO, "Blocked IP: {0} {1}", new String[]{value, reason});
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(QUERY)){
            preparedStatement.setString(1, value);
            preparedStatement.setLong(2, count);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(startDate));
            preparedStatement.setString(4, reason);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new LogParserException("Unable to save blocked IP " + value, e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IpAddress ipAddress = (IpAddress) o;
        return Objects.equals(value, ipAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
