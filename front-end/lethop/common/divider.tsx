import React from "react";

type dividerText = {
  dividerText: string;
};

export default function Divider({ dividerText }: dividerText) {
  return (
    <div className="flex items-center py-6">
      <hr className="flex-grow border-t border-dark " />
      <span className="px-3 text-gray-500">{dividerText}</span>
      <hr className="flex-grow border-t border-dark" />
    </div>
  );
}
