"use client";
import React, { useState } from "react";
import Image from "next/image";
import profile from "../../../assets/profile.jpg";
import postPicture from "../../../assets/post-pic.png";
import { PostProps } from "@/type/Post";

export default function Post({ post }: { post: PostProps }) {
  const [text, setText] = useState<string>(post.textLines.slice(0, 200));

  const [toggleText, setToggleText] = useState<boolean>(false);

  function handleText() {
    setToggleText((rev) => !rev);
    setText(post.textLines);
  }
  return (
    <div className=" flex flex-col gap-y-6	border p-4 rounded-lg shadow-md bg-white-2 my-8 flex">
      <div className="flex items-center mb-4 gap-x-2 gap-y-2">
        <Image
          src={profile}
          alt="Profile Picture"
          width={50}
          height={50}
          className="rounded-full"
        />
        <div className="ml-4">
          <h2 className="">{post.title}</h2>
          <div className="text-gray-500  font-tag">
            <span>تاريخ: {post.date} - </span>
            <span className="ml-2">الوسم: {post.tag} - </span>
            <span className="ml-2">القسم: تحفيز</span>
          </div>
        </div>
      </div>
      {/* <Image 
		src={postPicture}
    width={600}
		 alt="Profile Picture" 
		 
		  className="rounded-2xl" 
		  /> */}

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
