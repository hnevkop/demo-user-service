package com.identity.example.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "tb_project")
public class Project {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120)
  private String name;

  @Column(unique = true)
  private UUID uuid;

  @Column private Boolean external;

  @ManyToMany(mappedBy = "projects", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<User> users = new HashSet<>();


  public Project() {
    this.uuid = UUID.randomUUID();
  }
}
