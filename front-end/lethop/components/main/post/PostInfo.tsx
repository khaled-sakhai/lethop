import React from "react";
import styles from "./postInfo.module.css";
import {
  ClockIcon,
  EllipsisHorizontalIcon,
  TagIcon,
  UserCircleIcon,
  HashtagIcon,
} from "@heroicons/react/24/outline";
import Image from "next/image";
import profile from "../../../assets/profile.jpg";
import { PostProps } from "@/type/Post";

export default function PostInfo({
  id,
  date,
  tag,
  category,
  authorPictureUrl,
  author,
}: PostProps) { console.log(authorPictureUrl)
  return (
   
    <div className={styles.post_signature}>
      <div className={styles.post_info_image}>
        <Image
          src={authorPictureUrl}
          alt="Profile Picture"
          width={45}
          height={45}
          className="rounded-full"
        />
      </div>

      <section className={styles.post_info}>
        <div className={styles.post_info_right}>
          <div className={styles.post_info_right_top}>
            <span>بواسطة: {author}</span>
            
            <span className={styles.post_info_item}>
              {/* <ClockIcon className={`${styles.post_info_icon} size-6`} /> */}
              {date}
            </span>
            
         
            
          </div>
          <div className={styles.post_info_right_bottom}>
            

            <span className={styles.post_info_item}>
              <HashtagIcon className={`${styles.post_info_icon} size-6`} />
              {tag}
            </span>

            <span className={styles.post_info_item}>
              <TagIcon className={`${styles.post_info_icon} size-6`} />
              {category}
            </span>
          </div>
        </div>

        <div className={styles.post_info_left}>
          <span>
            <EllipsisHorizontalIcon className="size-12" />
          </span>
        </div>
      </section>
    </div>
  );
}
