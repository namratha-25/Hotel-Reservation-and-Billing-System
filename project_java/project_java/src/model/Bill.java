package model;

import java.sql.Date;

public class Bill {

    private int billId;
    private int reservationId;
    private double totalAmount;
    private double taxAmount;
    private double serviceCharges;
    private double finalAmount;
    private Date billDate;

    public Bill() {
    }

    public Bill(int billId, int reservationId, double totalAmount,
                double taxAmount, double serviceCharges,
                double finalAmount, Date billDate) {

        this.billId = billId;
        this.reservationId = reservationId;
        this.totalAmount = totalAmount;
        this.taxAmount = taxAmount;
        this.serviceCharges = serviceCharges;
        this.finalAmount = finalAmount;
        this.billDate = billDate;
    }

    // Getters & Setters

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(double taxAmount) { this.taxAmount = taxAmount; }

    public double getServiceCharges() { return serviceCharges; }
    public void setServiceCharges(double serviceCharges) { this.serviceCharges = serviceCharges; }

    public double getFinalAmount() { return finalAmount; }
    public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }

    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }
}
