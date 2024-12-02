import React from "react";
import styles from "./header.module.css";
import Search from "./Search";
import Navbar from "./navbar/Navbar";
import Logo from "./Logo";
import { Bars3Icon, XMarkIcon } from "@heroicons/react/24/outline";
interface headerProps {
  show: boolean;
  setShow: any;
}
export default function Header({ show, setShow }: headerProps) {
  const classes =
    " size-16 cursor-pointer  text-dark-2 hover:bg-gray-2 hover:text-dark";
  const handleClick = () => {
    setShow((prevState: boolean) => !prevState);
  };
  // xl:hidden
  return (
    <header className={styles.header}>
      {show ? (
        <XMarkIcon className={classes} onClick={handleClick} />
      ) : (
        <Bars3Icon className={classes} onClick={handleClick} />
      )}
      <Logo />
      <Search mobile={false} />

      <Navbar />
    </header>
  );
}
