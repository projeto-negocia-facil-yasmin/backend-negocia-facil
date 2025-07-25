package br.edu.ifpb.dac.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "advertisements")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "advertiser_id", nullable = false)
    @Setter
    private User advertiser;

    @Setter
    private String description;

    @ManyToMany
    @JoinTable(name = "advertisement_products",
                inverseJoinColumns = 
                    @JoinColumn(name = "product_id", referencedColumnName = "id"))
    
    private List<Product> products;

    @Column(name = "total_price", precision = 5, scale = 2)
    @PositiveOrZero
    private BigDecimal totalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_timestamp")
    @Setter
    private Date createdAt;

    public void calculateTotalPrice(){
        BigDecimal total = products.stream().map(Product::getPrice)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalPrice = total;
    }

    //Calcula o preço automaticamente ao definir os produtos
    public void setProducts(List<Product> products) {
        this.products = products;
        calculateTotalPrice();
    }
}
