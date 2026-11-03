package kz.kbtu.onlineecoshopnew.order.order;

import jakarta.persistence.OneToMany;
import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderStatus;
import kz.kbtu.onlineecoshopnew.order.orderItems.OrederItems;
import kz.kbtu.onlineecoshopnew.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order-seq")
    @SequenceGenerator(
            name = "order-seq",
            sequenceName = "orders_seq",
            allocationSize = 50
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "order", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrederItems> items = new ArrayList<>();

    @Column(name="created_at")
    private Instant createdAt;

    @Column(name="predicted_date_of_delivery")
    private LocalDate predictedDateOfDelivery;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name="sub_price")
    private BigDecimal subPrice;

    @Column(name="delivery_price")
    private BigDecimal deliveryPrice;

    @Column(name="discount")
    private BigDecimal discount;

    @Column(name="total_price")
    private BigDecimal totalPrice;

    @Column(name="currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name="delivery_city")
    private String deliveryCity;

    @Column(name="delivery_address")
    private String deliveryAddress;


    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }

}
