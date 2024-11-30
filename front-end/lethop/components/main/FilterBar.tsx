import React from "react";
import Category from "./filterBar/Category";
import Tags from "./filterBar/Tags";

export default function FilterBar() {
  return (
    <section className=" rounded-t-xl border border-gray bg-white-2">
      <Category />
      {/* <Tags /> */}
    </section>
  );
}
