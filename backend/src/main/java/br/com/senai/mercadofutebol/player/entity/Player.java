
package br.com.senai.mercadofutebol.player.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "jogadores")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "nome", nullable = false, length = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "posicao", nullable = false, length = 3)
    private PlayerPosition position;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "agilidade", nullable = false)
    private Integer agility;

    @NotNull
    @Min(16)
    @Max(45)
    @Column(name = "idade", nullable = false)
    private Integer age;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(name = "velocidade", nullable = false)
    private Integer pace;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(name = "finalizacao", nullable = false)
    private Integer shooting;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(name = "passe", nullable = false)
    private Integer passing;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(name = "drible", nullable = false)
    private Integer dribbling;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(name = "defesa", nullable = false)
    private Integer defending;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(name = "fisico", nullable = false)
    private Integer physical;

    @NotNull
    @Min(1)
    @Max(99)
    @Column(name = "potencial", nullable = false)
    private Integer potential;

    @NotNull
    @Column(name = "valor_base", nullable = false, precision = 6, scale = 2)
    private BigDecimal baseValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "condicao", nullable = false, length = 20)
    private PhysicalCondition condition = PhysicalCondition.APTO;

    @Column(name = "clube_id")
    private Integer clubId;

    @Column(name = "excluido_em")
    private LocalDateTime deletedAt;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime updatedAt;

    public Player() {
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        if (condition == null) {
            condition = PhysicalCondition.APTO;
    }

        if (createdAt == null) {
            createdAt = now;
    }

    updatedAt = now;
}

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public PlayerPosition getPosition() { return position; }
    public void setPosition(PlayerPosition position) { this.position = position; }

    public Integer getAgility() { return agility; }
    public void setAgility(Integer agility) { this.agility = agility; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Integer getPace() { return pace; }
    public void setPace(Integer pace) { this.pace = pace; }

    public Integer getShooting() { return shooting; }
    public void setShooting(Integer shooting) { this.shooting = shooting; }

    public Integer getPassing() { return passing; }
    public void setPassing(Integer passing) { this.passing = passing; }

    public Integer getDribbling() { return dribbling; }
    public void setDribbling(Integer dribbling) { this.dribbling = dribbling; }

    public Integer getDefending() { return defending; }
    public void setDefending(Integer defending) { this.defending = defending; }

    public Integer getPhysical() { return physical; }
    public void setPhysical(Integer physical) { this.physical = physical; }

    public Integer getPotential() { return potential; }
    public void setPotential(Integer potential) { this.potential = potential; }

    public BigDecimal getBaseValue() { return baseValue; }
    public void setBaseValue(BigDecimal baseValue) { this.baseValue = baseValue; }

    public PhysicalCondition getCondition() { return condition; }
    public void setCondition(PhysicalCondition condition) { this.condition = condition; }

    public Integer getClubId() { return clubId; }
    public void setClubId(Integer clubId) { this.clubId = clubId; }

    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void setDeleted(boolean deleted) {
        this.deletedAt = deleted ? LocalDateTime.now() : null;
    }
}
