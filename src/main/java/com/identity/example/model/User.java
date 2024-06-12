package com.identity.example.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private UUID uuid;

  @Column(nullable = false, length = 200)
  private String email;

  @Column(nullable = false, length = 129)
  private String password;

  @Column(nullable = false, length = 120)
  private String name;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "users_projects",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "project_id"))
  private Set<com.identity.example.model.Project> projects = new HashSet<>();

  public User() {
    this.uuid = UUID.randomUUID();
  }

  public User(String email, String password, String name) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.uuid = UUID.randomUUID();
  }
}
