package model;

import java.util.Date;

public class Reservation {
    private int id;
    private int clientId;
    private int roomId;
    private Date checkInDate;
    private Date checkOutDate;
    
    public Reservation(){
        
    }
    public Reservation(int id, int clientId, int roomId, Date checkInDate, Date checkOutDate){
        this.id = id;
        this.clientId = clientId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
    
        public Reservation(int clientId, int roomId, Date checkInDate, Date checkOutDate){
        this.clientId = clientId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
        
        
    public int getId() {
        return id;
    }
    public int getClientId() {
        return clientId;
    }
    public int getRoomId() {
        return roomId;
    }
    public Date getCheckInDate() {
        return checkInDate;
    }
    public Date getCheckOutDate() {
        return checkOutDate;
    }
        
    
    public void setId(int id) {
        this.id = id;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }
    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    @Override
    public String toString(){
        return "Reservation [id=" + id + ",clientId=" + clientId + ",roomId=" + roomId + ",checkInDate=" + checkInDate + ",checkOutDate=" + checkOutDate + "]";
    }
}
