package com.simplecoding.cheforest.jpa.season.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SEASON_INGREDIENT")
public class SeasonIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "season_ingredient_seq")
    @SequenceGenerator(
            name = "season_ingredient_seq",
            sequenceName = "SEASON_INGREDIENT_SEQ",
            allocationSize = 1
    )
    @Column(name = "INGREDIENT_ID")
    private Long ingredientId;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "SEASONS", length = 20)
    private String seasons;

    @Column(name = "SEASON_DETAIL", length = 50)
    private String seasonDetail;

    @Column(name = "DESCRIPTION", columnDefinition = "CLOB")
    private String description;

    @Column(name = "EFFECTS", columnDefinition = "CLOB")
    private String effects;

    @Column(name = "IMAGE_URL", length = 500)
    private String imageUrl;
}
