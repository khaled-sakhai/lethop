"use client";
import React, { useState } from "react";
import Image from "next/image";
import profile from "../../../assets/profile.jpg";
import postPicture from "../../../assets/post-pic.png";
import { PostProps } from "@/type/Post";
import styles from "./post.module.css";
import {
  ClockIcon,
  EllipsisHorizontalIcon,
  TagIcon,
  UserCircleIcon,
  HashtagIcon,
} from "@heroicons/react/24/outline";

export default function Post({ post }: { post: PostProps }) {
  const [text, setText] = useState<string>(post.textLines.slice(0, 200));

  const [toggleText, setToggleText] = useState<boolean>(false);

  function handleText() {
    setToggleText((rev) => !rev);
    setText(post.textLines);
  }
  return (
    <div className={styles.post}>
      <div className={styles.post_signature}>
        <div className={styles.post_info_image}>
          <Image
            src={profile}
            alt="Profile Picture"
            width={45}
            height={45}
            className="rounded-full"
          />
        </div>

        <section className={styles.post_info}>
          <div className={styles.post_info_right}>
            <div className={styles.post_info_right_top}>
              <span>بواسطة: mohamed khaled</span>
            </div>
            <div className={styles.post_info_right_bottom}>
              <span className={styles.post_info_item}>
                <ClockIcon className={`${styles.post_info_icon} size-6`} />
                {post.date}
              </span>

              <span className={styles.post_info_item}>
                <HashtagIcon className={`${styles.post_info_icon} size-6`} />
                {post.tag}
              </span>

              <span className={styles.post_info_item}>
                <TagIcon className={`${styles.post_info_icon} size-6`} /> قصص
                نجاح
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

      {/* <Image 
		src={postPicture}
    width={600}
		 alt="Profile Picture" 
		 
		  className="rounded-2xl" 
		  /> */}
      <h2 className="text-orange-4">{post.title}</h2>
      <p className="mb-2 whitespace-pre-line">
        {toggleText ? post.textLines : post.textLines.slice(0, 200)}
      </p>
      <button className="text-blue-500 text-left " onClick={handleText}>
        {" "}
        {toggleText ? "إخفاء" : "اكمل القراءة"}
      </button>
      <div className="flex items-center justify-between mt-4">
        <div className="flex gap-x-6 text-gray-500 hover:text-gray-700 font-tag">
          <button className="">تعليق</button>
          <button className="">اعجاب</button>
          <button className="">حفظ</button>
        </div>

        <div className="flex gap-x-6 text-gray-500 hover:text-gray-700  font-tag">
          <button className="">34 تعليق</button>
          <button className="">121 اعجاب</button>
          <button className="">18 حفظ</button>
        </div>
      </div>
    </div>
  );
}
