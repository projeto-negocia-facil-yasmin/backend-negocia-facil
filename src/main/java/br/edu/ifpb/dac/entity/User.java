package br.edu.ifpb.dac.entity;

import br.edu.ifpb.dac.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String imgUrl;
    private String fullName;
    private String enrollmentNumber;
    private String phone;

    public User(String username, String password, String fullName, String enrollmentNumber, String phone) {
        this.username = username;
        this.fullName = fullName;
        this.enrollmentNumber = enrollmentNumber;
        this.phone = phone;
    }

    public User(Long id, String username, String imgUrl, String fullName, String enrollmentNumber, String phone) {
        this.id = id;
        this.username = username;
        this.imgUrl = imgUrl;
        this.fullName = fullName;
        this.enrollmentNumber = enrollmentNumber;
        this.phone = phone;
    }

    public User(Long id, String username, String password, String imgUrl, String fullName, String enrollmentNumber, String phone) {
        this.id = id;
        this.username = username;
        this.imgUrl = imgUrl;
        this.fullName = fullName;
        this.enrollmentNumber = enrollmentNumber;
        this.phone = phone;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private List<Role> roles = new ArrayList<>();
}