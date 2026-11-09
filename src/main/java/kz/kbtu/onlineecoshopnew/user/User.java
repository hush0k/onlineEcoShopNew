package kz.kbtu.onlineecoshopnew.user;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(
            name = "user_seq_gen",
            sequenceName = "user_seq",
            allocationSize = 50
    )
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name="role")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="hashed_password")
    private char[] hashedPassword;

    @Column(name="is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name="deleted_at")
    private LocalDate deletedAt;

    @Column(name="created_at")
    private Instant createdAt;

    @Column(name="updated_at")
    private Instant updatedAt;


    @PrePersist
    public void prePersist(){

        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
