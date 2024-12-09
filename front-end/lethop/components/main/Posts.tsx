import React from "react";
import Post from "./post/Post";
import styles from "./posts.module.css";
import profile from "../../../assets/profile.jpg";

import { PostProps } from "@/type/Post";
export default function Posts() {
  //dummy data.
  const posts: PostProps[] = [
    {
      id: 1,
      textLines:
        "تعاني الأنظمة الاقتصادية في الوقت الراهن من مشاكل عميقة ليس بسبب ضعف القدرة الإنتاجية أو ندرة في الموارد ولكن نتيجة للتوزيع غير العادل للثروات وتكدس الأموال في شريحة معينة من الناس ..فإن مشكلة الفقر المتزايد في ظل النظام الاقتصادي المعاصر تكمن في التوزيع غير العادل للثروة وفرض ضرائب تساهم في إفقار ذوي الدخل المحدود. لذلك توجد ضرورة مُلحة لتكتل إقليمي أو دولي حقيقي وفعال للدول الفقيرة على الأقل لمواجهة المشكلات الاقتصادية ومنها الفقر، ويوقف هيمنة الدول الغنية ويوصل صوتها إلى المجتمع الدولي بشكل لائق. وهذا قد يتحقق من خلال إرادة شعبية، وقرار سياسي مستمد من تلك الإرادة بتطبيق المعالجات الاقتصادية الملائمة المستوحاة من القيم الإسلامية التي تتسم بالمرونة والواقعية وتراعي المصلحة العامة.",
      title: "كيف توقفت عن التدخين بعد 8 سنوات ادمان",
      date: "قبل 8 ساعات",
      tag: "إدمان",
      category: "تحفيز",
      author: "Mohamed Ahmed",
      authorPictureUrl: "/assets/profile.jpg",
    },
  ];
  return (
    <section className={styles.posts}>
      <Post post={posts[0]} />
      <Post post={posts[0]} />
      <Post post={posts[0]} />
      <Post post={posts[0]} />
    </section>
  );
}
