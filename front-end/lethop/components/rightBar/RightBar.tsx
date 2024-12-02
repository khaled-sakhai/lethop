"use client";

import React from "react";
import { usePathname } from "next/navigation";
import { useState, ReactNode } from "react";
import styles from "./rightBar.module.css";
import MainMenu from "./MainMenu";
import TagsMenu from "./TagsMenu";
import WebMenu from "./WebMenu";
import ClickAwayListener from "react-click-away-listener";
import Search from "../header/Search";
interface BarProps {
  show: boolean;
  setter: (value: boolean) => void;
}

export default function RightBar({ show, setter }: BarProps) {
  return (
    <>
      {/* Backdrop */}
      {show && (
        <div className={styles.backdrop} onClick={() => setter(false)} />
      )}

      {/* Sidebar */}
      <aside
        className={`${styles.rightBar} ${show ? styles.show : ""} ${`bar`}`}
      >
        {/* <MainMenu /> */}
        {show && <Search mobile={true} />}

        <TagsMenu />
        <WebMenu />
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
      </aside>
    </>
  );
}
