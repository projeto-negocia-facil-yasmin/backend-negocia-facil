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
    private String password;
    private String imgUrl;
    private String fullName;
    private String enrollmentNumber;

    public User(String username, String password, String fullName, String enrollmentNumber) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.enrollmentNumber = enrollmentNumber;
    }

    public User(Long id, String username, String imgUrl, String fullName, String enrollmentNumber) {
        this.id = id;
        this.username = username;
        this.imgUrl = imgUrl;
        this.fullName = fullName;
        this.enrollmentNumber = enrollmentNumber;
    }

    public User(Long id, String username, String password, String imgUrl, String fullName, String enrollmentNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.imgUrl = imgUrl;
        this.fullName = fullName;
        this.enrollmentNumber = enrollmentNumber;
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