import React from "react";
import styles from "./leftBar.module.css";
import Link from "next/link";

export default function LeftBar() {
  return (
    <aside className={styles.leftBar}>
      <ul>
        <h3 className="mt-4 text-dark px-2">مجموعة صحة نفسية</h3>
        <p>
          مجموعة تهدف الى مساعدة الناس على تحسين صحتهم النفسية و العقلية, من
          خلال مشاركة تجاربهم و نصائحهم و طرح اسئلتهم
        </p>
      </ul>
      <Link href="/">
        <img src="/assets/ad.jpg" alt="Ad Image" className="w-full h-auto" />
      </Link>

      <ul>
        <h3 className="my-4 text-dark bg-gray-2 py-4 px-2">اخر المنشورات:</h3>
        <li>
          <Link href="/"> كيف توقفت عن التدخين و الشيشة في شهر واحد</Link>{" "}
          <Link href="/">بواسطة: Ahmed Mouhamed</Link>
        </li>
        <li>كيف استطعت الحصول على شهادة اللغة الانجليزية</li>
        <li>كيف تحققت من احلامي في العمل </li>
      </ul>
    </aside>
  );
}
