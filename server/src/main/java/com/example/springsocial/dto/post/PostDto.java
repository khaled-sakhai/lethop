package com.example.springsocial.dto.post;
import com.example.springsocial.dto.comments.CommentResponse;
import com.example.springsocial.dto.user.UserInfo;
import com.example.springsocial.util.ProjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
import org.hibernate.Hibernate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto implements Serializable {

    private Long postId;
    private String title;
    private String content;
    private List<String> tags = new ArrayList<>();
    private String Category;
    private String lastModifiedDate;
    // user id + full name + profile image + last modify date
    private boolean isAnonymous=false;
    private UserInfo postUserInfo;
    private int savedCounter=0;
    private int likedCounter=0;
    private int commentCounter=0;

    public PostDto(Post post){
        // for security -- front end must subtract 12345 from user id
        this.postId=post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isAnonymous=post.isAnonymous();
        for(Tag tag:post.getListTags()){
            this.tags.add(tag.getTagName());
        }
        this.lastModifiedDate = ProjectUtil.convertDateToString(post.getLastModifiedDate());
        this.likedCounter=post.getLikesCount();
        
        this.commentCounter=post.getCommentsCount();
        this.savedCounter=post.getSavesCount();
        this.Category = post.getCategory().getCategory();

        if(!isAnonymous && post.getUser().getUserProfile()!=null){

          this.postUserInfo=new UserInfo();
          if(post.getUser().getUserProfile().getProfilePicture()!=null){
              this.postUserInfo.setUserImageUrl(post.getUser().getUserProfile().getProfilePicture().getUrl()) ;
           }
        // for security -- front end must subtract 54321 from user id
          this.postUserInfo.setUserId(post.getUser().getId() ) ;
          this.postUserInfo.setUserName(post.getUser().getUserProfile().getFullName());
        }


    }
    

}
