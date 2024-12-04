import Divider from "@/common/divider";
import React from "react";
import { FaGoogle, FaFacebook } from "react-icons/fa";

export default function SocialForm() {
  return (
    <>
      <section className="flex gap-6 text-2xl">
        <button className="border border-gray-2 p-6 bg-pink text-white opacity-90 hover:opacity-100">
          <span className="flex justify-start items-center gap-4">
            <FaGoogle className="text-4xl" /> باستخدام جوجل
          </span>
        </button>

        <button className="border border-gray-2  p-6 bg-blue text-white opacity-90 hover:opacity-100">
          <span className="flex justify-start items-center gap-4">
            <FaFacebook className="text-4xl" /> باستخدام فيسبوك
          </span>
        </button>
      </section>
    </>
  );
}
