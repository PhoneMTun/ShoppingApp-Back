package com.bfs.hibernateprojectdemo.domain;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Override
    public String toString() {
        return "Permission [id=" + id + ", name=" + name + "]";
    }
}
