package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
)
public class Post {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

}

/*
CascadeType.ALL:
当你保存（persist）一个Post实体时，相关的Comment实体也会被保存。
当你更新（merge）一个Post实体时，相关的Comment实体也会被更新。
当你删除（remove）一个Post实体时，相关的Comment实体也会被删除。

orphanRemoval
当 orphanRemoval 被设置为 false 时，从集合中删除一个 Comment 对象只会将其从集合中移除，而不会删除数据库中的相应行。这意味着在数据库中的 Comment 记录仍然存在，但不再与 Post 对象的 comments 集合相关联。
 */