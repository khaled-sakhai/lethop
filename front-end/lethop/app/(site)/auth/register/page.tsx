"use client";

import Divider from "@/common/divider";
import RegisterForm from "@/components/forms/registerForm";
import SocialForm from "@/components/forms/SocialForm";
import styles from "./page.module.css";
import React from "react";
import RequireAuth from "@/common/util/RequiredAuth";
interface Props {
	children: React.ReactNode;
}
export default function Page({ children }: Props) {
  return (
    // <RequireAuth></RequireAuth>
    <div className={styles.register}>
      <section className={styles.intro}>
        <h2>إنشاء حساب في شبكة معافر:</h2>

        <p>سجل الآن وابدأ رحلتك معنا!</p>
        <SocialForm />
      </section>
      <Divider dividerText={"او"} />
     
      <RegisterForm />
    </div>
  );
}
