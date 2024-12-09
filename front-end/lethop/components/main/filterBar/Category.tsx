"use client";

import React, { useState, MouseEvent } from "react";
import styles from "./category.module.css";
import Link from "next/link";
import ExtraDropMenuButton from "./ExtraDropMenu";
import FilterButton from "./FilterButton";

type CategoryType = {
  id: number;
  label: string;
  url?: string;
};

//dummy data
const tags: CategoryType[] = [
  { id: 1, label: "تحفيز", url: "/" },
  { id: 2, label: "أخطاء و ندم", url: "/" },
  { id: 3, label: "أسئلة و اجوبة", url: "/" },
];

export default function Category() {
  const [openMenuId, setOpenMenuId] = useState<number | null>(null); // Tracks the open menu

  const toggleMenu = (id: number) => {
    setOpenMenuId((prevId) => (prevId === id ? null : id)); // Toggle the menu for the clicked button
  };

  return (
    <section className="">
      <ul className="flex p-y-10 flex-wrap ">
        {tags.map((e, i) => {
          return (
            <li key={e.id}>
              <FilterButton
                label={e.label}
                resetFilter={setOpenMenuId}
                onclick={() => {
                  toggleMenu(e.id);
                }}
              />

              {openMenuId === e.id && (
                <ExtraDropMenuButton
                  category={e.label}
                  id={e.id}
                  setMenu={setOpenMenuId}
                />
              )}
            </li>
          );
        })}
      </ul>
    </section>
  );
}
