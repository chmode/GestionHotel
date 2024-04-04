package dao;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Reservation;
import database.DbConnect;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ReservationDao {
  
        public ReservationDao() {
    }

    public int createReservation(Reservation reservation) throws SQLException, ClassNotFoundException {
        int generatedReservationId = -1;

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("INSERT INTO reservation(client_id, room_id, date_in, date_out) VALUES(?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            prs.setInt(1, reservation.getClientId());
            prs.setInt(2, reservation.getRoomId());
            prs.setDate(3, new java.sql.Date(reservation.getCheckInDate().getTime()));
            prs.setDate(4, new java.sql.Date(reservation.getCheckOutDate().getTime()));

            int affectedRows = prs.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = prs.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedReservationId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedReservationId;
    }

    public int updateReservation(Reservation reservation) throws SQLException, ClassNotFoundException {
    int updateResult = -1;

    try (Connection connection = DbConnect.getConnection()) {
        connection.setAutoCommit(false);

        Reservation existingReservation = getReservationById(reservation.getId());
        if (existingReservation == null) {
            connection.rollback();
            return -2; // Reservation not found
        }

        try (PreparedStatement updateStmt = connection.prepareStatement(
                "UPDATE reservation SET client_id=?, room_id=?, date_in=?, date_out=? WHERE id=?")) {
            updateStmt.setInt(1, reservation.getClientId());
            updateStmt.setInt(2, reservation.getRoomId());
            updateStmt.setDate(3, new java.sql.Date(reservation.getCheckInDate().getTime()));
            updateStmt.setDate(4, new java.sql.Date(reservation.getCheckOutDate().getTime()));
            updateStmt.setInt(5, reservation.getId());

            int affectedRows = updateStmt.executeUpdate();

            if (affectedRows > 0) {
                connection.commit();
                updateResult = 1; // Successfully updated
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return updateResult;
    }


    public int deleteReservation(int reservationId) throws SQLException, ClassNotFoundException {
    int deleteResult = -1;

    try (Connection connection = DbConnect.getConnection()) {
        connection.setAutoCommit(false);

        Reservation reservation = getReservationById(reservationId);
        if (reservation == null) {
            connection.rollback();
            return -2; // Reservation not found
        }

        try (PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM reservation WHERE id = ?")) {
            deleteStmt.setInt(1, reservationId);

            int affectedRows = deleteStmt.executeUpdate();

            if (affectedRows > 0) {
                connection.commit();
                deleteResult = 1; // Successfully deleted
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return deleteResult;
    }


    public Reservation getReservationById(int reservationId) throws SQLException, ClassNotFoundException {
        Reservation reservation = null;

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM reservation WHERE id = ?")) {
            prs.setInt(1, reservationId);

            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next()) {
                    reservation = new Reservation(
                            resultSet.getInt("id"),
                            resultSet.getInt("client_id"),
                            resultSet.getInt("room_id"),
                            resultSet.getDate("date_in"),
                            resultSet.getDate("date_out")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservation;
    }

    public List<Reservation> getAllReservations() throws SQLException, ClassNotFoundException {
        List<Reservation> reservationList = new ArrayList<>();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM reservation");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("client_id"),
                        resultSet.getInt("room_id"),
                        resultSet.getDate("date_in"),
                        resultSet.getDate("date_out")
                );
                reservationList.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservationList;
    }


    public ObservableList<Reservation> getAllReservationsObservable() throws SQLException, ClassNotFoundException {
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM reservation;");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getInt("id"));
                reservation.setClientId(resultSet.getInt("client_id"));
                reservation.setRoomId(resultSet.getInt("room_id"));
                reservation.setCheckInDate(resultSet.getDate("date_in"));
                reservation.setCheckOutDate(resultSet.getDate("date_out"));

                reservationList.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservationList;
    }




    public ObservableList<Integer> loadNBR(){
        ObservableList<Integer> nbrLIST = FXCollections.observableArrayList();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT Nb_personne FROM type_room;");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                nbrLIST.add(resultSet.getInt("Nb_personne"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return nbrLIST;
    }

    public ObservableList<Integer> availableRooms(int nbrPersonne, LocalDate dateIn, LocalDate dateOut){
        ObservableList<Integer> avrLIST = FXCollections.observableArrayList();
        try (Connection connection = DbConnect.getConnection();


             PreparedStatement preparedStatement = connection.prepareStatement("SELECT matricule FROM room JOIN type_room ON room.type = type_room.id WHERE type_room.Nb_personne = ? AND status = true AND matricule NOT IN (SELECT room.matricule FROM room JOIN reservation ON room.id = reservation.room_id JOIN type_room ON room.type = type_room.id WHERE type_room.Nb_personne = ? AND ((date_in <= ? AND date_out >= ?) OR (date_in <= ? AND date_out >= ?) OR (date_in >= ? AND date_out <= ?)))")) {

            preparedStatement.setInt(1, nbrPersonne);
            preparedStatement.setInt(2, nbrPersonne);
            preparedStatement.setDate(3, Date.valueOf(dateIn));
            preparedStatement.setDate(4, Date.valueOf(dateIn));
            preparedStatement.setDate(5, Date.valueOf(dateOut));
            preparedStatement.setDate(6, Date.valueOf(dateOut));
            preparedStatement.setDate(7, Date.valueOf(dateIn));
            preparedStatement.setDate(8, Date.valueOf(dateOut));



                 /* --------- */

            ResultSet AVromms = preparedStatement.executeQuery();
            while (AVromms.next()) {
                avrLIST.add(AVromms.getInt("matricule"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return avrLIST;
    }



    public int isclientAvailable(String email){

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT id FROM users WHERE email = ? AND role = 'client';")) {

            prs.setString(1, email);

            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next()) {
                    //clientid = resultSet.getInt("id");
                    return resultSet.getInt("id");
                } else {
                    return -44;
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public int isRoomAvailable(int matricule, LocalDate dateIn, LocalDate dateOut) {

            int roomid;
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT status, id FROM room WHERE matricule = ? ")) {

            prs.setInt(1, matricule);

            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next()) {
                    roomid = resultSet.getInt("id");
                    if(!resultSet.getBoolean("status")){
                        return -2;
                    }
                } else {
                    return -22;
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        }


        try (Connection connection = DbConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reservation WHERE room_id = ? AND ((date_in <= ? AND date_out >= ?) OR (date_in <= ? AND date_out >= ?) OR (date_in >= ? AND date_out <= ?))")) {

            preparedStatement.setInt(1, roomid);
            preparedStatement.setDate(2, Date.valueOf(dateIn));
            preparedStatement.setDate(3, Date.valueOf(dateIn));
            preparedStatement.setDate(4, Date.valueOf(dateOut));
            preparedStatement.setDate(5, Date.valueOf(dateOut));
            preparedStatement.setDate(6, Date.valueOf(dateIn));
            preparedStatement.setDate(7, Date.valueOf(dateOut));

            ResultSet resultSet = preparedStatement.executeQuery();
            //return !resultSet.next(); // If the result set is empty, the room is available
            if(!resultSet.next()){
                return roomid;
            }else{
                return -2;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int addReservation(String email, LocalDate dateIn, LocalDate dateOut, int matricule) {
        LocalDate currentDate = LocalDate.now();
        if (dateIn.isBefore(currentDate) || dateOut.isBefore(currentDate) || dateOut.isBefore(dateIn) || dateIn.isAfter(dateOut)) {
            return -3; // Illogical date
        }


        int clid = isclientAvailable(email);
        if(clid <= 0){
            return -44;
        }


        int roomAvailability = isRoomAvailable(matricule, dateIn, dateOut);

        if(roomAvailability <= 0){
            switch (roomAvailability) {
                case -2:
                    return -4; // Room not available
                case -3:
                    return -5; // Date not available
                case -22:
                    return -22;// room nexest pas
                default:
                    return -1; // Handle other cases or exceptions
            }
        }

        String insertQuery = "INSERT INTO reservation (client_id, date_in, date_out, room_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setInt(1, clid);
            preparedStatement.setDate(2, Date.valueOf(dateIn));
            preparedStatement.setDate(3, Date.valueOf(dateOut));
            preparedStatement.setInt(4, roomAvailability);

            int affectedRows = preparedStatement.executeUpdate();
            return (affectedRows > 0) ? 1 : -1;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


}
