package com.example.springsocial.dto.post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private List<String> tags = new ArrayList<>();
    private String Category;
    private String lastModifiedDate;
    // user id + full name + profile image + last modify date
    private boolean isAnonymous=false;
    private PostUserInfo postUserInfo;
    private int savedCounter=0;
    private int likedCounter=0;
    private int commentCounter=0;

    public PostDto(Post post){
        // for security -- front end must subtract 12345 from user id
        this.postId=post.getId() + 12345;
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isAnonymous=post.isAnonymous();
        for(Tag tag:post.getListTags()){
            this.tags.add(tag.getTagName());
        }
        // Format the lastModifiedDate to show year, month, day, and hours
        // Convert Date to LocalDateTime
        LocalDateTime localDateTime = post.getLastModifiedDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        // Format the LocalDateTime to show year, month, day, and hours
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.lastModifiedDate = localDateTime.format(formatter);
        this.likedCounter=post.getLikesCount();
        
        this.commentCounter=post.getCommentsCount();
        this.savedCounter=post.getSavesCount();
        System.out.println("xcxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println(post.getCategory().getCategory());
        this.Category = post.getCategory().getCategory();
        
        if(!isAnonymous && post.getUser().getUserProfile()!=null){
        this.postUserInfo=new PostUserInfo();
        if(post.getUser().getUserProfile().getProfilePicture()!=null){
            this.postUserInfo.setUserImageUrl(post.getUser().getUserProfile().getProfilePicture().getUrl()) ;
        }
        // for security -- front end must subtract 54321 from user id
        this.postUserInfo.setUserId(post.getUser().getId() + 54321) ;
        this.postUserInfo.setUserName(post.getUser().getUserProfile().getFullName());
        }
    

    }
    

}
