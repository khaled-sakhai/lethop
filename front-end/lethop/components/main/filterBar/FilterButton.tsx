import React from "react";
import { FaCaretDown } from "react-icons/fa";

type props = {
  label: string;
  onclick: any;
  resetFilter: any;
};
export default function FilterButton({ label, onclick, resetFilter }: props) {
  function handleCategory() {
    console.log(label);
    resetFilter(null);
  }
  return (
    <div className="flex items-center  font-bold transition duration-150 text-gray hover:text-dark border-b-[5px] border-transparent hover:border-green  text-nowrap	">
      <button onClick={handleCategory} className=" px-2 py-6">
        {label}
      </button>
      <FaCaretDown
        onClick={onclick}
        className="px-4 py-6 cursor-pointer	size-8"
      />
    </div>
  );
}
