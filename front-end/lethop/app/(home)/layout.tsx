"use client";

import "@/styles/globals.css";
import styles from "../layout.module.css";

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
      
      <body className={` ${styles.layout}`}>
        <Setup />
        <Provider>
          <Header show={showSideBar} setShow={setShowSideBar} />

          <RightBar show={showSideBar} setter={setShowSideBar} />

          <section className={styles.content}>
            <main className={`${styles.main}`}>
              {children}
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>

              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
            </main>
            <LeftBar />
          </section>
          <BottomNav setSideBare={setShowSideBar} showSideBare={showSideBar} isLoggedIn={true}/>
        </Provider >
      </body>
    </html>
  );
}
