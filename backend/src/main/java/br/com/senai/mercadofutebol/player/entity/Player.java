
package br.com.senai.mercadofutebol.player.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private PlayerPosition position;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(nullable = false)
    private Integer agility;

    @NotNull
    @Min(16)
    @Max(45)
    @Column(nullable = false)
    private Integer age;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(nullable = false)
    private Integer pace;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(nullable = false)
    private Integer shooting;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(nullable = false)
    private Integer passing;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(nullable = false)
    private Integer dribbling;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(nullable = false)
    private Integer defending;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(nullable = false)
    private Integer physical;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(nullable = false)
    private Integer potential;

    @NotNull
    @Column(name = "base_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PhysicalCondition condition = PhysicalCondition.APTO;

    @Column(name = "club_id")
    private Long clubId;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    public Player() {
    }

    @PrePersist
    protected void onCreate() {
        if (condition == null) {
            condition = PhysicalCondition.APTO;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerPosition getPosition() {
        return position;
    }

    public void setPosition(PlayerPosition position) {
        this.position = position;
    }

    public Integer getAgility() {
        return agility;
    }

    public void setAgility(Integer agility) {
        this.agility = agility;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getPace() {
        return pace;
    }

    public void setPace(Integer pace) {
        this.pace = pace;
    }

    public Integer getShooting() {
        return shooting;
    }

    public void setShooting(Integer shooting) {
        this.shooting = shooting;
    }

    public Integer getPassing() {
        return passing;
    }

    public void setPassing(Integer passing) {
        this.passing = passing;
    }

    public Integer getDribbling() {
        return dribbling;
    }

    public void setDribbling(Integer dribbling) {
        this.dribbling = dribbling;
    }

    public Integer getDefending() {
        return defending;
    }

    public void setDefending(Integer defending) {
        this.defending = defending;
    }

    public Integer getPhysical() {
        return physical;
    }

    public void setPhysical(Integer physical) {
        this.physical = physical;
    }

    public Integer getPotential() {
        return potential;
    }

    public void setPotential(Integer potential) {
        this.potential = potential;
    }

    public BigDecimal getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(BigDecimal baseValue) {
        this.baseValue = baseValue;
    }

    public PhysicalCondition getCondition() {
        return condition;
    }

    public void setCondition(PhysicalCondition condition) {
        this.condition = condition;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
