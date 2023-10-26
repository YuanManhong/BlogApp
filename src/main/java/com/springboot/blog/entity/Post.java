package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}

/*
CascadeType.ALL:
当你保存（persist）一个Post实体时，相关的Comment实体也会被保存。
当你更新（merge）一个Post实体时，相关的Comment实体也会被更新。
当你删除（remove）一个Post实体时，相关的Comment实体也会被删除。

orphanRemoval
当 orphanRemoval 被设置为 false 时，从集合中删除一个 Comment 对象只会将其从集合中移除，而不会删除数据库中的相应行。这意味着在数据库中的 Comment 记录仍然存在，但不再与 Post 对象的 comments 集合相关联。
 */

/*
@Data Annotation:

@Data is a shortcut to create common methods like toString, hashCode, equals, getters, and setters for your class.
Problem with Bidirectional Relationships:

When two classes reference each other (like a Post has many Comments, and each Comment refers back to its Post), using @Data can cause issues.
If you try to print a Post (using toString), it will try to print its Comments, and each Comment will try to print its Post again, creating an endless loop that crashes the program.
ModelMapper:

ModelMapper may accidentally trigger this problem while it's copying data from one object to another.
Solution:

Instead of @Data, use @Getter and @Setter to create only the getter and setter methods, avoiding the risky toString, hashCode, and equals methods.
This way, you still get the convenient methods to access and change data, but without the risk of endless loops.
Testing:

Always test your code to make sure it behaves as expected, especially when classes reference each other.
Custom Methods:

If needed, you can write your own toString, hashCode, or equals methods to handle relationships safely.
Explore Further:

Look into documentation and examples to see best practices for using Lombok and ModelMapper together, especially with complex relationships.
This setup helps to keep your code easy to manage while preventing potential is
 */