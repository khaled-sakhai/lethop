"use client";

import "@/styles/globals.css";
import styles from "../layout.module.css";

import type { Metadata } from "next";
import LeftBar from "@/components/leftBar/LeftBar";
import Header from "@/components/header/Header";
import RightBar from "@/components/rightBar/RightBar";
import { useEffect, useState } from "react";
import React from "react";

import Provider from "../../redux/provider";
import { Setup } from "@/common/util";
import BottomNav from "@/components/BottomNav";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const [showSideBar, setShowSideBar] = React.useState<boolean>(false);

  return (
    <html lang="en" dir="rtl">
      <body className={`'font-tab' ${styles.layout}`}> 
        <Setup />
        <Provider>
          <Header show={showSideBar} setShow={setShowSideBar} />
          <RightBar show={showSideBar} setter={setShowSideBar} />

          
            <main className={` 'font-head' ${styles.main}`}>{children}</main>
          
          <BottomNav setSideBare={setShowSideBar} showSideBare={showSideBar} isLoggedIn={false}/>

        </Provider>
      </body>
    </html>
  );
}
