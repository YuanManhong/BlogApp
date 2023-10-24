package com.springboot.blog.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data // create getters and setters
public class PostDTO { //DTO: data transfer object,
    private long id;
    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;
    @NotEmpty
    @Size(min = 10, message = "Post title should have at least 10 characters")
    private String description;
    @NotEmpty
    private String content;
    private Set<CommentDTO> comments;
}

/*
note: sending DTO or JPA object
Different Needs:

DTO: It's like a summary or a snapshot of the data you want to show or send.
JPA Entity: It's the full story of the data in your database.
Sometimes, you just want to share the summary, not the whole story.
Avoid Errors:

If you directly send the JPA entity, sometimes parts of the data might not load correctly and can cause errors.
Safety:

Using DTOs is like only showing a trailer of a movie. You only show what you want others to see, keeping private scenes hidden.
Faster:

Sending just the needed information (DTO) can be quicker than sending everything (JPA entity).
Control:

Using DTOs means you choose what data to send. You're in control, so no unexpected changes happen to your main data.
In short, DTOs are like sharing just the highlights or important points, while JPA entities are the full detailed story. It's often better to just share the highlights for simplicity and safety.
 */