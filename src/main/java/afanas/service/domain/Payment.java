package afanas.service.domain;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "payment")
@ToString(of = {"id","text"})

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "status")
    private String status;
    @Column (name = "orderid")
    private int orderID;

    public Payment () {

    }

    public Payment(int orderID, String status) {
        this.orderID = orderID;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrderID() { return orderID; }

    public void setOrderID(int orderID) { this.orderID = orderID; }
}
