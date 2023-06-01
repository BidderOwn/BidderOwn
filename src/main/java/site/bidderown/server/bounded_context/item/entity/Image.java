package site.bidderown.server.bounded_context.item.entity;

import site.bidderown.server.base.base_entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Image extends BaseEntity {

    @ManyToOne
    @JoinColumn
    private Item item;

    private String url;

    private String name;

}
