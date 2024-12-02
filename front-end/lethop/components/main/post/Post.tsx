"use client";
import React, { useState } from "react";
import Image from "next/image";
import { PostProps } from "@/type/Post";
import styles from "./post.module.css";
import PostInfo from "./PostInfo";

export default function Post({ post }: { post: PostProps }) {
  const [text, setText] = useState<string>(post.textLines.slice(0, 200));

  const [toggleText, setToggleText] = useState<boolean>(false);

  function handleText() {
    setToggleText((rev) => !rev);
    setText(post.textLines);
  }
  return (
    <div className={styles.post}>
      <PostInfo
        id={post.id}
        title={post.title}
        date={post.date}
        tag={post.tag}
        category={post.category}
        authorPictureUrl={post.authorPictureUrl}
        author={post.author}
        textLines={post.textLines}
      />

      <h2 className="text-orange-4">{post.title}</h2>
      <p className="mb-2 whitespace-pre-line">
        {toggleText ? post.textLines : post.textLines.slice(0, 200)}
      </p>
      <button className="text-blue-500 text-left " onClick={handleText}>
        
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
