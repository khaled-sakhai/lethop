"use client";

import RegisterForm from "@/components/forms/registerForm";
import styles from "./page.module.css";
import React from "react";
import SocialForm from "@/components/forms/SocialForm";
import Divider from "@/common/divider";

export default function Page() {
  return (
    <div className={styles.register}>
      <section className={styles.intro}>
        <h2>إنشاء حساب في شبكة معافر:</h2>
        <p>
          أنشئ حسابًا للوصول إلى المحتوى الحصري، والتواصل مع الآخرين، والاستمتاع
          بجميع الميزات التي نقدمها. الأمر سريع، سهل، ومجاني!
        </p>
        <p>سجل الآن وابدأ رحلتك معنا!</p>
        <SocialForm />
      </section>

      {/* <Divider dividerText={"او"} /> */}
      <RegisterForm />
    </div>
  );
}
