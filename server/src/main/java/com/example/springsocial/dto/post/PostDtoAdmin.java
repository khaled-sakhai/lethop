package com.example.springsocial.dto.post;

import com.example.springsocial.dto.ImageDto;
import com.example.springsocial.dto.user.UserInfo;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.util.ProjectUtil;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDtoAdmin implements Serializable {

    private Long postId;
    private String title;
    private String content;
    private List<String> tags = new ArrayList<>();
    private List<ImageDto> images=new ArrayList<>();
    private String Category;
    private String lastModifiedDate;
    // user id + full name + profile image + last modify date
    private boolean isAnonymous=false;
    private UserInfo postUserInfo;
    private int savedCounter=0;
    private int likedCounter=0;
    private int commentCounter=0;
    private boolean isDeleted=false;

    public PostDtoAdmin(Post post){
        // for security -- front end must subtract 12345 from user id
        this.postId=post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isAnonymous=post.isAnonymous();
        for(Tag tag:post.getListTags()){
            this.tags.add(tag.getTagName());
        }
        if (!post.getPostImages().isEmpty()){
            post.getPostImages().forEach(img->this.images.add(new ImageDto(img)));
        }
        this.lastModifiedDate = ProjectUtil.convertDateToString(post.getLastModifiedDate());
        this.likedCounter=post.getLikesCount();
        this.isDeleted=post.isDeleted();
        this.commentCounter=post.getCommentsCount();
        this.savedCounter=post.getSavesCount();
        this.Category = post.getCategory().getCategory();

        if(post.getUser().getUserProfile()!=null){
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
