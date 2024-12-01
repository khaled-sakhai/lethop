"use client";

import { useLazyUserVerifyQuery } from "@/redux/features/authApiSlice";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import { toast } from "react-toastify";

type props = {
  params: {
    code: string;
  };
};

export default function page({ params }: props) {
  const router = useRouter();
  const [userVerify, { error, isLoading }] = useLazyUserVerifyQuery();

  useEffect(() => {
    const { code } = params;

    userVerify(code)
      .unwrap()
      .then(({ message }) => {
        toast.success(message);
      })
      .catch(() => {
        toast.error("رابط التفعيل خاطئ, حاول مجددا");
      })
      .finally(() => {
        router.push("/auth/login");
      });
  }, []);

  return <div>جاري تفعيل حسابك...</div>;
}
