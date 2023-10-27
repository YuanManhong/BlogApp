package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    ModelMapper mapper;
    private CategoryRepository categoryRepository;

    // @Autowired if the class has only one attribute, then it can be omitted
    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper,
                           CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO){
        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId()));
        //convert DTO to entity
        Post post = mapToEntity(postDTO);
        post.setCategory(category);// you only know the categoryId in postDTO, so you need to set category again
        Post newPost = postRepository.save(post); // save into database

        //convert entity to DTO
        PostDTO postResponse = mapToDTO(newPost);

        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        // based on sortDir to create Sort object
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        // get content for page object
        List<Post> listOfPost = posts.getContent();

        List<PostDTO> content = listOfPost.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElement(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDTO getPostByID(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post", "id", id));
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setDescription(postDTO.getDescription());

        Post updatedPost = postRepository.save(post);

        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    private PostDTO mapToDTO(Post post){
        PostDTO postDTO = mapper.map(post, PostDTO.class);

       /*
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setDescription(post.getDescription());
        postDTO.setContent(post.getContent());*/
        return postDTO;
    }

    private Post mapToEntity(PostDTO postDTO){
        Post post = mapper.map(postDTO, Post.class);
        /*
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());*/
        return post;
    }
}
