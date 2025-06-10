package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(long ownerId);

    List<Item> findByDescriptionContainsIgnoreCaseAndIsAvailableIsTrueOrNameContainsIgnoreCaseAndIsAvailableIsTrue(
            String description, String name
    );

    @Modifying
    @Query("UPDATE Item i SET i.name = :name, i.description = :description, i.isAvailable = :isAvailable " +
            "WHERE i.id = :itemId")
    void updateItem(@Param("itemId") long itemId, @Param("name") String name, @Param("description") String description,
                    @Param("isAvailable") Boolean isAvailable);

    @Modifying
    @Query("DELETE Item i WHERE i.ownerId = :ownerId")
    void deleteAllItemsFromOwner(@Param("ownerId") long ownerId);
}
