"use client";
import React, { useState } from "react";
import { FaCaretDown } from "react-icons/fa";
import ClickAwayListener from "react-click-away-listener";

type props = {
  category: string;
  id: number;
  setMenu: any;
};

//dummy data

const filters: CategoryType[] = [
  { id: 1, label: "الافضل" },
  { id: 2, label: "نقاشات" },
  { id: 3, label: "الاحدث" },
  { id: 3, label: "الاقدم" },
];

type CategoryType = {
  id: number;
  label: string;
  url?: string;
};
export default function ExtraDropMenuButton({ category, id, setMenu }: props) {
  function handleClickAway(event: Event): void {
    setMenu(null);
  }

  function handleFilter(filter: string) {
    console.log("menu : " + id + " - " + category + " filter: " + filter);
    setMenu(null);
  }
  return (
    <div className="absolute w-60 h-50 shadow-2xl shadow-gray-2  bg-gray-4 border border-gray-4 flex-col gap-y-2 rounded-lg  flex z-10">
      {/* Dropdown menu items */}
      <ul>
        {filters.map((filtr, i) => {
          return (
            <li
              onClick={() => handleFilter(filtr.label)}
              key={filtr.label}
              className="p-4 hover:bg-gray-2 cursor-pointer"
            >
              {filtr.label}
            </li>
          );
        })}
      </ul>
    </div>
  );
}
