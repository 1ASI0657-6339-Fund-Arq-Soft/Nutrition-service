package pe.edu.upc.center.agecare.nutrition.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.center.agecare.nutrition.domain.model.aggregates.FoodEntry;
import pe.edu.upc.center.agecare.nutrition.domain.model.commands.CreateFoodEntryCommand;
import pe.edu.upc.center.agecare.nutrition.domain.model.commands.DeleteFoodEntryCommand;
import pe.edu.upc.center.agecare.nutrition.domain.model.commands.UpdateFoodEntryCommand;
import pe.edu.upc.center.agecare.nutrition.domain.services.FoodEntryCommandService;
import pe.edu.upc.center.agecare.nutrition.infrastructure.persistence.jpa.repositories.FoodEntryRepository;

import java.util.Optional;

@Service
public class FoodEntryCommandServiceImpl implements FoodEntryCommandService {
    private final FoodEntryRepository foodEntryRepository;

    public FoodEntryCommandServiceImpl(FoodEntryRepository foodEntryRepository) {
        this.foodEntryRepository = foodEntryRepository;
    }

    @Override
    public Long handle(CreateFoodEntryCommand command) {
        var foodEntry = new FoodEntry(command);
        try {
            foodEntryRepository.save(foodEntry);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving food entry: " + e.getMessage());
        }
        return foodEntry.getId();
    }

    @Override
    public Optional<FoodEntry> handle(UpdateFoodEntryCommand command) {
        var foodEntryId = command.foodEntryId();
        
        if (!foodEntryRepository.existsById(foodEntryId)) {
            throw new IllegalArgumentException("Food entry does not exist");
        }

        var foodEntryToUpdate = foodEntryRepository.findById(foodEntryId).get();
        
        try {
            var updatedFoodEntry = foodEntryRepository.save(
                foodEntryToUpdate.updateInformation(
                    command.meal(),
                    command.description(),
                    command.date(),
                    command.time()
                )
            );
            return Optional.of(updatedFoodEntry);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating food entry: " + e.getMessage());
        }
    }

    @Override
    public void handle(DeleteFoodEntryCommand command) {
        if (!foodEntryRepository.existsById(command.foodEntryId())) {
            throw new IllegalArgumentException("Food entry does not exist");
        }
        
        try {
            foodEntryRepository.deleteById(command.foodEntryId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting food entry: " + e.getMessage());
        }
    }
}
