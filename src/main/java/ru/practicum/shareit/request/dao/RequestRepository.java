package ru.practicum.shareit.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.ItemRequest;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    Collection<ItemRequest> findAllByRequesterId(long requesterId);

    @Modifying
    @Query("UPDATE ItemRequest i SET i.description = :description WHERE i.id = :itemReqId")
    void updateItemRequest(@Param("itemReqId") long itemReqId, @Param("description") String description);
}
