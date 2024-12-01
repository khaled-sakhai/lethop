import { ImSpinner3 } from "react-icons/im";

interface Props {
  sm?: boolean;
  md?: boolean;
  lg?: boolean;
}

export default function Spinner({ sm, md, lg }: Props) {
  // Base classes
  let className = "animate-spin text-white-300 fill-white-300 mr-2";

  // Conditional classes
  if (sm) className += " w-4 h-4";
  if (md) className += " w-6 h-6";
  if (lg) className += " w-8 h-8";

  return (
    <div role="status">
      <ImSpinner3 className={className} />
      <span className="sr-only">تحميل...</span>
    </div>
  );
}
