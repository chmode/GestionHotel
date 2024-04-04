package model;

import java.util.Date;

public class Payment {
    private int id;
    private int reservationId;
    private double amount;
    private int status;
    private Date paymentTime;
    private String process;
    
    public Payment(int id, int reservationId, double amount, int status, Date paymentTime, String process){
        this.id = id;
        this.reservationId = reservationId;
        this.amount = amount;
        this.status = status;
        this.paymentTime = paymentTime;
        this.process = process;
    }
    
        public Payment(int reservationId, double amount, int status, Date paymentTime, String process){
        this.reservationId = reservationId;
        this.amount = amount;
        this.status = status;
        this.paymentTime = paymentTime;
        this.process = process;
    }
        
        
    public int getId() {
        return id;
    }
    public int getReservationId() {
        return reservationId;
    }
    public double getAmount() {
        return amount;
    }
    public int getStatus() {
        return status;
    }
    public Date getPaymentTime() {
        return paymentTime;
    }
    public String getProcess() {
        return process;
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }
    public void setPaymentProcess(String process) {
        this.process = process;
    }
    
    @Override
    public String toString(){
        return "Payment [id=" + id + ",reservationId=" + reservationId + ",amount=" + amount + ",status=" + status + ",paymentTime=" + paymentTime + ",process=" + process + "]";
    }
}
