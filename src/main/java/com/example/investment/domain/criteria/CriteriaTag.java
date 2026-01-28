package com.example.investment.domain.criteria;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "criteria_tags",
        uniqueConstraints = @UniqueConstraint(name = "uq_criteria_tag_name", columnNames = "name")
)
public class CriteriaTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false, length = 50)
    private String name;
    public static CriteriaTag create(String name) {
        CriteriaTag t = new CriteriaTag();
        t.name = requireNonBlank(name, "name");
        return t;
    }
    public void changeName(String name) {
        this.name = requireNonBlank(name, "name");
    }

    private static String requireNonBlank(String v, String name) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException(name + " must not be blank");
        return v.trim();
    }

}
