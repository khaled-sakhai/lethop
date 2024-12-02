import React from "react";

import styles from "./search.module.css";
type props = {
  mobile: boolean;
};
export default function Search({ mobile }: props) {
  return (
    <input
      type="search"
      className={`${
        mobile
          ? "w-[90%] h-[2.5rem] block lg:hidden"
          : "hidden lg:block lg:w-[30%] lg:h-[1.5rem]"
      } ${styles.search}`}
      placeholder={"بحث..."}
    />
  );
}
