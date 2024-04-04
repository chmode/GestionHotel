package dao;

import java.sql.Connection;
import database.DbConnect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Payment;

public class PaymentDao {

    public PaymentDao(){
        
    }
    
    public int createPayment(Payment payment) throws SQLException, ClassNotFoundException {
        int generatedPaymentId = -1;

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement(
                     "INSERT INTO paiement(reservation_id, price, status, time_pay, process) VALUES (?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            prs.setLong(1, payment.getReservationId());
            prs.setDouble(2, payment.getAmount());
            prs.setInt(3, payment.getStatus());
            prs.setTime(4, new java.sql.Time(payment.getPaymentTime().getTime()));
            prs.setString(5, payment.getProcess());

            int affectedRows = prs.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = prs.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedPaymentId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedPaymentId;
    }

    public int updatePayment(Payment payment) throws SQLException, ClassNotFoundException {
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("UPDATE paiement SET reservation_id=?, price=?, status=?, time_pay=?, process=? WHERE id=?")) {
            prs.setLong(1, payment.getReservationId());
            prs.setDouble(2, payment.getAmount());
            prs.setInt(3, payment.getStatus());
            prs.setTime(4, new java.sql.Time(payment.getPaymentTime().getTime()));
            prs.setString(5, payment.getProcess());
            prs.setLong(6, payment.getId());

            int affectedRows = prs.executeUpdate();

            if (affectedRows > 0) {
                return 1; // Successfully updated
            } else {
                return -2; // Payment not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error during update
        }
    }

    public int deletePayment(int paymentId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DbConnect.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement prs = connection.prepareStatement("DELETE FROM paiement WHERE id = ?")) {
                prs.setInt(1, paymentId);

                int affectedRows = prs.executeUpdate();

                if (affectedRows > 0) {
                    connection.commit();
                    return 1; // Successfully deleted
                } else {
                    connection.rollback();
                    return -2; // Payment not found
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return -1; // Error during deletion
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Payment getPaymentById(int paymentId) throws SQLException, ClassNotFoundException {
        Payment payment = null;

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM paiement WHERE id = ?")) {
            prs.setInt(1, paymentId);

            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next()) {
                    payment = new Payment(
                            resultSet.getInt("id"),
                            resultSet.getInt("reservation_id"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("status"),
                            resultSet.getTime("time_pay"),
                            resultSet.getString("process")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payment;
    }

    public List<Payment> getAllPayments() throws SQLException, ClassNotFoundException {
        List<Payment> paymentList = new ArrayList<>();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM paiement");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                Payment payment = new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("reservation_id"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("status"),
                        resultSet.getTime("time_pay"),
                        resultSet.getString("process")
                );
                paymentList.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paymentList;
    }
    
    
}
