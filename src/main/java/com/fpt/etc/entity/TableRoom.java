package com.fpt.etc.entity;

import com.fpt.etc.entity.enums.TableRoomStatus;
import com.fpt.etc.entity.enums.TableRoomType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "table_room")
@Getter
@Setter
public class TableRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_room_id")
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TableRoomType type;

    private Integer capacity;

    private String location;

    @Enumerated(EnumType.STRING)
    private TableRoomStatus status;
}

