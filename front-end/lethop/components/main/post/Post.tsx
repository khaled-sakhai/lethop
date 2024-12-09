"use client";
import React, { useState } from "react";
import Image from "next/image";
import { PostProps } from "@/type/Post";
import styles from "./post.module.css";
import PostInfo from "./PostInfo";
import PostInteraction from "./PostInteraction";

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
      <h2 className="text-orange-4 text-base">{post.title}</h2>

      <div className="mb-2 whitespace-pre-line font-base">
        {toggleText ? post.textLines : post.textLines.slice(0, 200) + " ..."}
        <button
          className="text-blue-500 text-left mr-2 text-dark"
          onClick={handleText}
        >
          {toggleText ? "إخفاء" : "اكمل القراءة"}
        </button>
      </div>

      <PostInteraction category={post.category} />
    </div>
  );
}
