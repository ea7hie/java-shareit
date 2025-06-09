package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findAllByItemId(long itemId);

    /*@Query("SELECT c.Item i FROM comments c WHERE c.")
    Collection<Item> findAllByOwnerIdWithComments(long ownerId);*/

    //  @Query("SELECT NEW com.example.dto.ProductDto(p.productName, p.price) FROM Product p WHERE p.price > :price")

  /*  @Query("SELECT NEW ru.practicum.shareit.item.dto.ItemDto(c.item.id, c.item.name, c.item.description, " +
            "c.item.ownerId, c.item.requestId, c.item.isAvailable, NEW ru.practicum.shareit.item.comment.CommentDto(" +
            "c.id, c.text, c.author.name, c.datePosting)) FROM Comment c WHERE c.item.id = :itemId")
    Collection<ItemDto> findAllByOwnerIdWithComments(long itemId);*/

    Collection<Comment> findAllByItemIdIn(Collection<Long> itemIds);
}
