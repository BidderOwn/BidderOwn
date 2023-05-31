package site.bidderown.server.bounded_context.item.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import site.bidderown.server.base.base_entity.BaseEntity;
import javax.persistence.*;
import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Item extends BaseEntity {

    @Column(length = 30)
    private String title;

    @Column(length = 500)
    private String description;

    private int minimumPrice;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Users user;

//    @OneToMany(mappedBy = "image")
//    private List<Image> imageList;

    private LocalDateTime expireAt;

//    @OneToMany(mappedBy = "comment")
//    private List<Comment> commentList;

    public static Item createItem(Item items) {


        Item item = new Item();

        item.title = items.getTitle();

        item.description = items.getDescription();

        item.minimumPrice = items.getMinimumPrice();

//        item.imageList = itemRequest.getImageList();

//        item.expireAt = itemRequest.getExpireAt();

        return item;
    }


}


