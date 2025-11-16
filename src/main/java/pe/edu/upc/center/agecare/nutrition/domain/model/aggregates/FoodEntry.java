package pe.edu.upc.center.agecare.nutrition.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import pe.edu.upc.center.agecare.nutrition.domain.model.commands.CreateFoodEntryCommand;
import pe.edu.upc.center.agecare.nutrition.domain.model.valueobjects.MealType;
import pe.edu.upc.center.agecare.shared.domain.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Table(name = "food_entries")
public class FoodEntry extends AuditableAbstractAggregateRoot<FoodEntry> {

    @Getter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealType meal;

    @Getter
    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Getter
    @NotBlank
    @Column(nullable = false)
    private String date; // YYYY-MM-DD

    @Getter
    @NotBlank
    @Column(nullable = false)
    private String time; // HH:mm

    @Getter
    @Column(name = "added_by")
    private String addedBy;

    @Getter
    @Column(name = "added_by_id")
    private Long addedById;

    @Getter
    @Column(name = "target_id")
    private Long targetId;

    protected FoodEntry() {
    }

    public FoodEntry(CreateFoodEntryCommand command) {
        this.meal = command.meal();
        this.description = command.description();
        this.date = command.date();
        this.time = command.time();
        this.addedBy = command.addedBy();
        this.addedById = command.addedById();
        this.targetId = command.targetId();
    }

    public FoodEntry updateInformation(MealType meal, String description, String date, String time) {
        this.meal = meal;
        this.description = description;
        this.date = date;
        this.time = time;
        return this;
    }
}
